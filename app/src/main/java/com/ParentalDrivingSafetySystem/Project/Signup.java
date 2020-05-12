package com.ParentalDrivingSafetySystem.Project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private EditText editFirstName, editLastName, editEmail, editMobile,
            age, editPassword, editConfirm, editdriver;
    private Button buttonSubmit;

    private AwesomeValidation awesomeValidation;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth=FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        editFirstName = (EditText) findViewById(R.id.FirstName);
        editLastName = (EditText) findViewById(R.id.LastName);
        editEmail = (EditText) findViewById(R.id.Email);
        editMobile = (EditText) findViewById(R.id.Mobile);
        editPassword = (EditText) findViewById(R.id.Password);
        editConfirm = (EditText) findViewById(R.id.ConfirmPassword);
        age = (EditText) findViewById(R.id.Age);

        buttonSubmit = (Button) findViewById(R.id.buttonNext);
        awesomeValidation.addValidation(this, R.id.FirstName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.LastName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.Email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        String mob = ".{11,11}";
        awesomeValidation.addValidation(this, R.id.Mobile, mob, R.string.mobileerror);
        String passw = ".{8,20}";
        awesomeValidation.addValidation(this, R.id.Password, passw, R.string.passworderror);
        awesomeValidation.addValidation(this, R.id.ConfirmPassword, R.id.Password, R.string.conpassworderror);
        awesomeValidation.addValidation(this, R.id.Age, Range.closed(21, 85), R.string.ageerror);
        String number = ".{11,11}";
        Button Back=(Button)findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                Intent intent = new Intent(getApplicationContext(), Signin.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSubmit.setOnClickListener(this);
    }



    public void submitForm(View view) {
        if (awesomeValidation.validate()) {
            Toast.makeText(this, "Validation Successfull", Toast.LENGTH_LONG).show();

            final String firstname = editFirstName.getText().toString();
            final String lastname = editLastName.getText().toString();
            final String mail = editEmail.getText().toString();
            final String phone = editMobile.getText().toString();
            final String password = editPassword.getText().toString();
            final String confirmpass = editConfirm.getText().toString();
            final String age2 = age.getText().toString();

            mAuth.createUserWithEmailAndPassword(mail,password).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Toast.makeText(Signup.this,mAuth.getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                            mAuth.getCurrentUser().getUid();
                            Parentuser user=new Parentuser();
                            String userid=mAuth.getCurrentUser().getUid();
                            user.writeNewUser(firstname,lastname,mail,phone,age2, password,userid,1);
                            Intent intent1 = new Intent(getApplicationContext(), Signin.class);
                            startActivity(intent1);

                        }
                    });


        }

    }


    @Override
    public void onClick(View v) {
        if(v == buttonSubmit)
        {
            submitForm(v);

        }

    }
}
