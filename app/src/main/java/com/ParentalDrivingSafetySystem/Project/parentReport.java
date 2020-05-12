package com.ParentalDrivingSafetySystem.Project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import android.graphics.drawable.*;


import java.util.ArrayList;
import java.util.Calendar;

public class parentReport extends AppCompatActivity {

    TextView left;
    TextView right;
    TextView acceleration;
    TextView stop;
    TextView name;
    TextView TripNO;
    String Driverid;
    ArrayList<String> Tripids;
    int accCounter;
    int leftCounter;
    int rightCounter;
    int stopCounter;
    Trip trip=new Trip();
    Calendar calender = Calendar.getInstance();
    int day = calender.get(Calendar.DAY_OF_MONTH);
    int month = Integer.valueOf(calender.get(Calendar.MONTH));
    int year = calender.get(Calendar.YEAR);
    String currentDate2= day + "/" + (month + 1) + "/" + year;
    ProgressBar progressBarLeft;
    ProgressBar progressBarRight;
    ProgressBar progressBarStop;
    ProgressBar progressBarAcceleration;
    RatingBar rating;



    DatabaseReference dataReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_report);

        left = findViewById(R.id.LeftNumber);
        right = findViewById(R.id.RightNumber);
        acceleration = findViewById(R.id.AccelerationNumber);
        stop = findViewById(R.id.StopNumber);
        name = findViewById(R.id.ratingWord);
        Tripids = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();
        Driverid = extras.getString("driverid");
        rating=findViewById(R.id.ratingBar);
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dataReference= FirebaseDatabase.getInstance().getReference();
        dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accCounter = 0;
                leftCounter = 0;
                rightCounter = 0;
                stopCounter = 0;
                int Tripcounter=0;


                String Driverfirstname = dataSnapshot.child("users").child(Driverid).child("firstname").getValue().toString();
                String Driverlastname = dataSnapshot.child("users").child(Driverid).child("lastname").getValue().toString();
                String AccumelativeRate = dataSnapshot.child("users").child(Driverid).child("accumelativerate").getValue().toString();

                name.setText(Driverfirstname + " " + Driverlastname);
                Toast.makeText(parentReport.this, currentDate2, Toast.LENGTH_SHORT).show();
                for (DataSnapshot x : dataSnapshot.child("Trip").getChildren())
                {
                    Log.i("DriverID", Driverid);
                    Log.i("driverid", x.child("driverID").getValue().toString());

                    if(Driverid.equals(x.child("driverID").getValue().toString()) && currentDate2.equals(x.child("date").getValue().toString()))
                    {

                        Tripids.add(x.child("id").getValue().toString());
                    }

                }

                for(DataSnapshot x : dataSnapshot.child("Report").getChildren())
                {
                    for (int i = 0; i < Tripids.size(); i++)
                    {

                        if(Tripids.get(i).equals(x.child("tripid").getValue().toString()))
                        {
                            Tripcounter++;
                            stopCounter = stopCounter + Integer.valueOf(x.child("stop").getValue().toString());
                            accCounter = accCounter + Integer.valueOf(x.child("acc").getValue().toString());
                            rightCounter = rightCounter + Integer.valueOf(x.child("right").getValue().toString());
                            leftCounter = leftCounter + Integer.valueOf(x.child("left").getValue().toString());

                        }
                    }

                }
                left.setText(String.valueOf(leftCounter));
                right.setText(String.valueOf(rightCounter));
                acceleration.setText(String.valueOf(accCounter));
                stop.setText(String.valueOf(stopCounter));
                rating.setRating(Float.valueOf(AccumelativeRate));

                Button Back=(Button)findViewById(R.id.Back);

                Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                TripNO =findViewById(R.id.TripNo);
                TripNO.setText(String.valueOf(Tripcounter));
                progressBarLeft=findViewById(R.id.progressBarLeft);
                if(leftCounter<=5)
                {


                    LayerDrawable layerDrawable = (LayerDrawable) progressBarLeft.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);


                    progressBarLeft.setProgress(leftCounter);


                }
                else if(leftCounter>5 &&leftCounter<=10)
                {

                    LayerDrawable layerDrawable = (LayerDrawable) progressBarLeft.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                    progressBarLeft.setProgress(leftCounter);


                }
                else
                {
                    LayerDrawable layerDrawable = (LayerDrawable) progressBarLeft.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


                    progressBarLeft.setProgress(leftCounter);
                }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                progressBarRight=findViewById(R.id.progressBarRight);
                if(rightCounter<=5)
                {


                    LayerDrawable layerDrawable = (LayerDrawable) progressBarRight.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);


                    progressBarRight.setProgress(rightCounter);


                }
                else if(rightCounter>5 &&rightCounter<=10)
                {

                    LayerDrawable layerDrawable = (LayerDrawable) progressBarRight.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                    progressBarRight.setProgress(rightCounter);


                }
                else
                {
                    LayerDrawable layerDrawable = (LayerDrawable) progressBarRight.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


                    progressBarRight.setProgress(rightCounter);
                }



                //////////////////////////////////////////////////////////////////////////////////////////////////



                progressBarStop=findViewById(R.id.progressBarStop);
                if(stopCounter<=5)
                {


                    LayerDrawable layerDrawable = (LayerDrawable) progressBarStop.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);


                    progressBarStop.setProgress(stopCounter);


                }
                else if(stopCounter>5 &&stopCounter<=10)
                {

                    LayerDrawable layerDrawable = (LayerDrawable) progressBarStop.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                    progressBarStop.setProgress(stopCounter);


                }
                else
                {
                    LayerDrawable layerDrawable = (LayerDrawable) progressBarStop.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


                    progressBarStop.setProgress(stopCounter);
                }

/////////////////////////////////////////////////////////////////////////////



                progressBarAcceleration=findViewById(R.id.progressBarAcceleration);
                if(accCounter<=5)
                {


                    LayerDrawable layerDrawable = (LayerDrawable) progressBarAcceleration.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);


                    progressBarAcceleration.setProgress(accCounter);


                }
                else if(accCounter>5 &&accCounter<=10)
                {

                    LayerDrawable layerDrawable = (LayerDrawable) progressBarAcceleration.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                    progressBarAcceleration.setProgress(accCounter);


                }
                else
                {
                    LayerDrawable layerDrawable = (LayerDrawable) progressBarAcceleration.getProgressDrawable();
                    Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


                    progressBarAcceleration.setProgress(accCounter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
