
# Parental-Driving-Safety-System.
   ![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/last%20logo.png)
## Project Description
Road accidents are a worldwide disaster with an ever-rising pattern.  Absence of consideration, foolish driving, disregardingdriving rules take numerous lives every day. In this project, we propose a solution to diminish the number of accidents, to follow the drivingconduct through the use of F- DTW, drowsiness detection and a concentration level detector for sibling drivers to create a detailed reportwhich will be sent to the concerned person. This goal will be achieved through the use of a mobile application that uses the built-in phoneaccelerometer, GPS and GSM, a Drowsiness detection system, built-in speaker.  To sum it all up, the goal is to notify concerned personaland emergency services as quickly as possible, to reduce the number of fatalities as much as possible using all the mentioned components.


### Features

            1. Drowsiness Recognition

            2.Accident Detection

            3.Driving Behavior Classification
#  GUI
## Parent
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Gui/1st%20Parent.png)
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Gui/2nd%20Parent.png)
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Gui/3rd%20Parent.png)
## Driver
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Gui/1st.png)
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Gui/2nd.png)
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Gui/3rd.png)



##    System Overview
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/System%20Overview/Overview.jpeg)
## Input &Preprocessing

we get the accelerometer readings in the coordinates( X, Y, Z) from the mobile device and pass the readings to a low pass filter algorithm to reduce noise and remove
gravity for getting accurate readings to classify the behaviour
of the driver.Also,we usedWindow Sliding Technique to perform
the required classification on the explicit window size
of given enormous array.
# Images(Before Low Pass Filter)
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Preprocessing/Before%20Low%20Pass%20Filter.png)
# Images(Before Low Pass Filter)
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Preprocessing/No%20Low%20Pass%20Filter.png)           

# Processing
After pre-preparing the information, Fast-DTW (Fast DynamicTime Warping) was used to differentiate between thebehaviours taken by the driver using the accelerometer thatis built-in in the smartphone to make the parent notified withthe driver behaviors and to be as accurate as possible Dataset Sample for our dataset 
## Left Aggressive
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Dataset/Left%20Aggressive.png)
## right aggressive         
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Dataset/right%20aggressive.png)
## Suddenly Stop
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Dataset/Suddenly%20Stop.png)
## Sudden Acceleration
![Test Image 4](https://github.com/danielkalalian33/-Parental-Driving-Safety-System/blob/master/image/Dataset/Sudden%20Acceleration.png)
