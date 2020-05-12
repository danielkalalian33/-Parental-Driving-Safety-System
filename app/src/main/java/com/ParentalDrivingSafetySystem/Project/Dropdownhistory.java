package com.ParentalDrivingSafetySystem.Project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dropdownhistory extends AppCompatActivity {
    private Spinner spinner;
    private DatabaseReference mDatabase;
    Button submit;
    ProgressBar progressBar;
    ArrayList<String> Driversnames;
    ArrayList<String> Driversids;
    ArrayList<String> x;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropdownhistory);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        spinner = (Spinner)findViewById(R.id.spinner1);
        submit = (Button)findViewById(R.id.submit1) ;
        progressBar = findViewById(R.id.dropdownhistoryprogressbar);
        Driversnames  = new ArrayList<String>();
        Driversids = new ArrayList<String>();
        x = new ArrayList<String>();
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String Mobile = dataSnapshot.child("users").child(userID).child("mobile").getValue().toString();
                for (DataSnapshot x : dataSnapshot.child("MobileConn").child(Mobile).child("DriverID").getChildren())
                {
                    Driversids.add(x.getValue().toString());
                }


                for (int i = 0; i < Driversids.size(); i++)
                {
                    Log.i("Driverid", Driversids.get(i));
                    Driversnames.add(dataSnapshot.child("users").child(Driversids.get(i)).child("firstname").getValue().toString());
                    Driversnames.add(dataSnapshot.child("users").child(Driversids.get(i)).child("lastname").getValue().toString());

                }
                for (int i = 0; i < Driversnames.size(); i=i+2)
                {
                    String name = Driversnames.get(i) + " " + Driversnames.get(i+1);
                    x.add(name);
                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Dropdownhistory.this, android.R.layout.simple_spinner_item, x);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dropdownhistory.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                counter = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button Back=(Button)findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onDestroy();
                Intent intent = new Intent(getApplicationContext(), parentPage.class);
                startActivity(intent);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dropdownhistory.this, Driversids.get(counter),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), History.class);
                intent.putExtra("driverid" , Driversids.get(counter));
                startActivity(intent);

            }
        });
    }

}
