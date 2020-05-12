package com.ParentalDrivingSafetySystem.Project;

import java.util.ArrayList;

public class Coordinates {
    public String ID;
    public ArrayList<Float> X, Y, Z;

    public Coordinates() {
        X=new ArrayList<>();
        Y=new ArrayList<>();
        Z=new ArrayList<>();
        ID="";
    }
}
