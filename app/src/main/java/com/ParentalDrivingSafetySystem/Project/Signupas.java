package com.ParentalDrivingSafetySystem.Project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Signupas extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupas);

        Button Back=(Button)findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signin.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void signupparent (View view)
    {
        Intent i3 = new Intent(getApplicationContext(), Signup.class);
        startActivity(i3);

    }
    public void signupdriver (View view)
    {
        Intent i3 = new Intent(getApplicationContext(), Signupdriver.class);

        startActivity(i3);

    }


}
