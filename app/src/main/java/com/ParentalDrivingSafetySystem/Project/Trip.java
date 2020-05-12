package com.ParentalDrivingSafetySystem.Project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Trip {
    public static AccelerometerReadings acc = new AccelerometerReadings();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();
    private String ID,DriverID,AccelerometerReadingsID;
    private String date;
    private boolean sleepy;
    double Startlat,Startlongitude;
    private String StartTime;
    private String EndTime;


    public Trip()
    {
        this.Startlat=Startlat;
        this.Startlongitude=Startlongitude;
        this.Endlat=Endlat;
        this.Endlongitude=Endlongitude;
        this.sleepy=sleepy;
        this.ID = ID;
        this.DriverID = DriverID;
        this.AccelerometerReadingsID=AccelerometerReadingsID;
    }

    public void setStartLat(double Startlat) {
        this.Startlat = Startlat;
    }

    public double getStartLat() {
        return Startlat;
    }
    public void setStartLongitude(double Startlongitude) {
        this.Startlongitude = Startlongitude;
    }

    public double getStartLongitude() {
        return Startlongitude;
    }

    double Endlat,Endlongitude;

    public void setEndLat(double Endlat) {
        this.Endlat = Endlat;
    }

    public double getEndLat() {
        return Endlat;
    }

    public void setEndLongitude(double Endlongitude) {
        this.Endlongitude = Endlongitude;
    }

    public double getEndLongitude() {
        return Endlongitude;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setSleepy(boolean sleepy) {
        this.sleepy = sleepy;
    }

    public boolean isSleepy() {
        return sleepy;
    }

    public void setAccelerometerReadingsID(String accelerometerReadingsID) {
        AccelerometerReadingsID = accelerometerReadingsID;
    }

    public String getAccelerometerReadingsID() {
        return AccelerometerReadingsID;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setDriverID(String driverID) {
        DriverID = driverID;
    }

    public String getDriverID() {
        return DriverID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }



    String writeNewTrip(Trip trip) {

        String id = mDatabase.push().getKey();
        trip.setID(id);
        mDatabase.child("Trip").child(id).setValue(trip);
        return id;
    }
}
