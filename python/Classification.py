from firebase import firebase
import csv
import os
import re
import math
from scipy.spatial.distance import euclidean
from fastdtw import fastdtw
from geopy.exc import GeocoderTimedOut
from itertools import groupby
import datetime
import time
import numpy as np
from geopy.geocoders import Nominatim
import mpu
import matplotlib.pyplot as plt
from dateutil.relativedelta import relativedelta
def date():
    one_year_from_now = datetime.datetime.now() + relativedelta()
    date_formated_month = one_year_from_now.strftime("%m")
    month=int(date_formated_month)
    date_formated_day = one_year_from_now.strftime("%d")
    day=int(date_formated_day)
    date_formated_year = one_year_from_now.strftime("%Y")
    year=int(date_formated_year)
    date_formated=(str(day)+"/"+str(month)+"/"+str(year))
    return date_formated
def windowing(array,steps,windowsize):
    h=[]
    h3=[]
    for i in range (0,(len(array)-windowsize),steps):

        for j in range (windowsize):
            h.append(array[j+i])
        h3.append(h)
        h=[]
    return h3
geolocator = Nominatim(user_agent="geoapiExercises")
def city_state_country(coord):
    # datetime.time.sleep(.300)
    geolocator = Nominatim(user_agent="driversystem")
    location = geolocator.reverse(coord, exactly_one=True)
    address = location.raw['address']
    state = address.get('state', '')
    road = address.get('road', '')
    town = address.get('town', '')
    suburb = address.get('suburb', '')
    country = "مصر/ "
    city = address.get('city', '')

    lo = road, country, city, town, state, suburb

    l = lo[1] + lo[2] + "," + lo[0] + " " + lo[3] + " " + lo[4] + "," + lo[5]
    return l
firebase = firebase.FirebaseApplication('https://grad-9b9d6.firebaseio.com/', None)
Trip = firebase.get('/Trip',None)
Accelerometer = firebase.get('/Accelerometer',None)
user = firebase.get('/users',None)
date_formated=date()
print("date",date_formated)
TempTrainingArray = []
TempTestArray = []
AllTest = []
AllTraining = []
AllDatasetNames = []

for x in(Trip):
    if(Trip.get(x).get('date')==date_formated):
        accid = Trip.get(x).get('accelerometerReadingsID')
        driver_id = Trip.get(x).get('driverID')
        print ("trip id :",x)
        print("driver id :",driver_id)
        startLongitude = Trip.get(x).get('startLongitude')
        startLat = Trip.get(x).get('startLat')
        endLongitude = Trip.get(x).get('endLongitude')
        endLat = Trip.get(x).get('endLat')
        print("driver id" + str(driver_id))
        print("acc id of trip" + str(accid))
        locationindex = float(startLat), float(startLongitude)
        endlocationindex = float(endLat), float(endLongitude)
        print(locationindex)
        print(endlocationindex)
        getlocationstart=city_state_country(locationindex)
        print(getlocationstart)
        time.sleep(10)

        # if(endlocationindex=="(0.0, 0.0)"):
        #     endlocationindex=locationindex
        getendlocation = city_state_country(endlocationindex)
        print(getendlocation)
        q = len(Accelerometer.get(accid).get('X'))
        testarry = []
        XArry = []
        YArry = []
        ZArry = []
        meanarray=[]
        for i in range(q):
            row = []
            XArry.append(Accelerometer.get(accid).get('X')[i])
            YArry.append(Accelerometer.get(accid).get('Y')[i])
            ZArry.append(Accelerometer.get(accid).get('Z')[i])
            row.append(XArry[-1])
            row.append(YArry[-1])
            row.append(ZArry[-1])
            testarry.append(row)
        finalarray=np.array(testarry)
        lenarray=(len(testarry))
        arraywindow=[]
        windowfinal=[]
        pn=windowing(finalarray, 7, 21)
        p=np.array(pn)
        print(p)
        Path = "C:\\Users\\luka\\Documents\\gp code+dataset\\code\\gp project\\Dataset\\Training2\\"
        filelist = os.listdir(Path)
        for file in filelist:
            AllDatasetNames.append(file[:file.index("all", ) - 1])

            with open(Path + file, 'r') as f:
                csvreader = csv.reader(f)
                fields = next(csvreader)
                data = np.array(list(csvreader))
            AllTraining.append(data)
            TempTrainingArray = []
        alltest2 = np.array(p)
        alltarining2 = np.array(AllTraining)
        i = 0
        TDSi = 0
        SmallestIndex = 0
        SmallestDistance, smallestpath = fastdtw(alltest2[0], alltarining2[0], dist=euclidean)
        SuddenStop = 0
        SuddenAcceleration = 0
        Left = 0
        right = 0
        suddennormal = 0
        rightnormal = 0
        leftnormal = 0
        normalacceleration = 0
        action = []
        a = []
        for TDS in alltest2:
            i = 0
            for DS in alltarining2:
                # print(DS)
                distance, path = fastdtw(TDS, DS, dist=euclidean)

                if distance < SmallestDistance:
                    SmallestDistance = distance
                    SmallestIndex = i

                i = i + 1

            print(" Behavior " + str(TDSi) + " Matches: " + AllDatasetNames[SmallestIndex] + ".  With Distance: " + str(
                SmallestDistance))
            print(TDS)
            SmallestDistance, smallestpath = fastdtw(TDS, alltarining2[0], dist=euclidean)
            TDSi += 1
            ini_string = (AllDatasetNames[SmallestIndex])
            res = ''.join([i for i in ini_string if not i.isdigit()])
            a.append(res)
        action = [i[0] for i in groupby(a)]
        print(action)

        for i in range(len(action)):
            if (action[i] == "stop"):
                SuddenStop = SuddenStop + 1
            elif (action[i] == "acceleration"):
                SuddenAcceleration = SuddenAcceleration + 1
            elif (action[i] == "left"):
                Left = Left + 1
            elif (action[i] == "right"):
                right = right + 1
            elif (action[i] == "suddennormal"):
                suddennormal = suddennormal + 1
            elif (action[i] == "rightnormal"):
                rightnormal = rightnormal + 1
            elif (action[i] == "left normal"):
                leftnormal = leftnormal + 1
            elif (action[i] == "normalacceleration"):
                normalacceleration = normalacceleration + 1

        print("Number of SuddenStop :" + str(SuddenStop))
        print("Number of SuddenAcceleration :" + str(SuddenAcceleration))
        print("Number of Left :" + str(Left))
        print("Number of right :" + str(right))
        print("Number of suddennormal :" + str(suddennormal))
        print("Number of rightnormal :" + str(rightnormal))
        print("Number of leftnormal :" + str(leftnormal))
        print("Number of normalacceleration :" + str(normalacceleration))
        X = Left + right + SuddenAcceleration + SuddenStop
        rate = 1.3 + 3.7 * math.exp(-0.1 * X)
        rating = round(rate, 2)

        rate2 = user.get(driver_id).get('accumelativerate')

        final_rate = round((rating + rate2) / 2, 2)
        #########################################################

        firebase.put('users/' + driver_id, "accumelativerate", final_rate)
        data_to_upload = dict(acc=SuddenAcceleration, stop=SuddenStop, left=Left, right=right, rate=rating ,tripid=x,startlocation=getlocationstart,endlocation=getendlocation)
        result = firebase.post('/Report/', data_to_upload)
        print(result)
