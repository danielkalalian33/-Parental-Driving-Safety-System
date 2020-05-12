package com.ParentalDrivingSafetySystem.Project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class driverReport extends AppCompatActivity {

    TextView name;
    TextView mobile;
    TextView parentname;
    TextView parentmobile;
    RatingBar rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_report);
        name = findViewById(R.id.Name);
        mobile = findViewById(R.id.driverMobileNo);
        parentname = findViewById(R.id.parentName);
        parentmobile = findViewById(R.id.parentMobileNo);
        rating = findViewById(R.id.Rating);

        Bundle extras = getIntent().getExtras();
        String Driverfirstname = extras.getString("firstName");
        String Driverlastname = extras.getString("lastName");
        String Drivermobile = extras.getString("Mobile");
        String Parentfirstname = extras.getString("Parentfirstname");
        String Parentlastname = extras.getString("Parentlastname");
        String Parentmobile = extras.getString("Parentmobile");
        String AccumelativeRate = extras.getString("accumelativeRate");
        Button Back=(Button)findViewById(R.id.Back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        name.setText(Driverfirstname + " " + Driverlastname);
        mobile.setText(Drivermobile);
        parentname.setText(Parentfirstname + " " + Parentlastname);
        parentmobile.setText(Parentmobile);
        rating.setRating(Float.valueOf(AccumelativeRate));
    }
}
