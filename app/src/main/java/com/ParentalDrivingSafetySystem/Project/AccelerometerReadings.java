package com.ParentalDrivingSafetySystem.Project;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccelerometerReadings {
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    public ShakeListener mShaker;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public String TripID;

    public String getTripID() {
        return TripID;
    }

    public void setTripID(String tripID) {
        TripID = tripID;
    }

    String writeNewAccelerometer(Coordinates acc) {
      String id = mDatabase.push().getKey();
      acc.ID=id;
      mDatabase.child("Accelerometer").child(id).setValue(acc);
        return id;
    }
}
