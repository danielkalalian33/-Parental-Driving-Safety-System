package com.ParentalDrivingSafetySystem.Project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_LONG;

public class Signin extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
            .child("users");



    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String email, password;
    EditText mail, passwprd;
    Button forgotpass;
    FirebaseDatabase FBD;
    Long Usertype = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        forgotpass = (Button) findViewById(R.id.forgotpass);
        mail = (EditText) findViewById(R.id.mail);
        passwprd = (EditText) findViewById(R.id.password);

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });

    }

    public void login(View view) {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        email = mail.getText().toString();
        password = passwprd.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Usertype = dataSnapshot.child(mAuth.getUid()).child("usertype").getValue(Long.class);
                                    String id = dataSnapshot.child(mAuth.getUid()).child("id").getValue(String.class);
                                    if(Usertype == 1)
                                    {
                                        Intent intent1 = new Intent(Signin.this, parentPage.class);
                                        startActivity(intent1);
                                    }
                                    else if (Usertype == 0)
                                    {
                                        Intent intent1 = new Intent(Signin.this, MainActivity.class);
                                        startActivity(intent1);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                            });

                            Toast.makeText(Signin.this, "SIGN IN", LENGTH_LONG).show();

                        }
                        else {

                            Toast.makeText(Signin.this, "please check your mail and password", LENGTH_LONG).show();
                        }

                    }

                });
    }

    public void signup (View view)
    {
        Intent i3 = new Intent(getApplicationContext(), Signupas.class);
        startActivity(i3);

    }



}
