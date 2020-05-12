package com.ParentalDrivingSafetySystem.Project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    ProgressBar progressbar;
    EditText useremail;
    Button userpass;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        progressbar = findViewById(R.id.progressbar);
        useremail = findViewById(R.id.useremail);
        userpass = findViewById(R.id.buttonforgotpass);


        firebaseAuth = FirebaseAuth.getInstance();

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

        userpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);

              firebaseAuth.sendPasswordResetEmail(useremail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful())
                      {
                          progressbar.setVisibility(View.GONE);
                          Toast.makeText(ForgotPassword.this, "Password sent to your email :)" ,Toast.LENGTH_LONG).show();

                      }
                      else
                      {
                          Toast.makeText(ForgotPassword.this, task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                      }

                  }
              });
            }
        });

    }
}
