package com.ParentalDrivingSafetySystem.Project;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class addDriver extends AppCompatActivity {
    int x=0;
    List<EditText> allEds = new ArrayList<EditText>();
    ArrayList<String> Driverno = new ArrayList<String>();
    ArrayList<String> Driverid = new ArrayList<String>();
    FirebaseDatabase firebaseDatabase;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        final EditText edit1=findViewById(R.id.edit1);
        allEds.add(edit1);
        Button S=findViewById(R.id.addnum);

        final LinearLayout ll = (LinearLayout) findViewById(R.id.main);
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

        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                if (x < 5) {

                    EditText editText = new EditText(addDriver.this);
                    allEds.add(editText);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        editText.setId(View.generateViewId());
                    }
                    editText.setHint("Enter Mobile Number");
                    final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(470,130);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText.setLayoutParams(lparams);

                    ll.addView(editText, 3);
                }

                else {
                    Toast.makeText(getApplicationContext(), "Only 5 numbers are allowed", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                dataReference= FirebaseDatabase.getInstance().getReference();
                dataReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String Mobile = dataSnapshot.child("users").child(userID).child("mobile").getValue().toString();
                        for (int i = 0; i <= allEds.size(); i++)
                        {

                            Driverno.add(allEds.get(i).getText().toString());

                            for (DataSnapshot x : dataSnapshot.child("users").getChildren()) {

                                if (Driverno.get(i).equals(x.child("mobile").getValue().toString()))
                                {
                                    Driverid.add(x.child("id").getValue().toString());
                                    mDatabase.child("MobileConn").child(Mobile).child("ParentID").setValue(userID);
                                    mDatabase.child("MobileConn").child(Mobile).child("DriverID").setValue(Driverid);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), " not valid driver", Toast.LENGTH_LONG).show();
                                }


                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }


        });
    }
}
