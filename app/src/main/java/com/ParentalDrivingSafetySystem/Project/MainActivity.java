package com.ParentalDrivingSafetySystem.Project;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity implements LocationListener {

    String mCurrentPhotoPath;
    Calendar calender = Calendar.getInstance();
    int day = calender.get(Calendar.DAY_OF_MONTH);
    int month = calender.get(Calendar.MONTH);
    int year = calender.get(Calendar.YEAR);
    private LocationManager lm;
    public double latitude, longitude;

    public static CheckCertainty ch=new CheckCertainty();
    Date startTime=null;
    public static Trip trip=new Trip();

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    public static AccelerometerReadings Acc = new AccelerometerReadings();
///////////////////////////////////////////////////////////////////////////////////////
public static String firstN,secondN;
    public int flag;
    public EditText edT1;
    public EditText edT2;


     DatabaseReference dataReference;
    DatabaseReference reference;
     FirebaseAuth mAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activation);
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, 10, this);
         Button serviceB=(Button)findViewById(R.id.serviceB);

        serviceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTime= Calendar.getInstance().getTime();
                    String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                String strDate = dateFormat.format(startTime);
                   double lat=latitude;
                   double lon=longitude;
                   trip.setStartLongitude(lon);
                   trip.setStartLat(lat);
                    trip.setStartTime(strDate);
                    trip.setDriverID(id);
                    trip.setDate(day + "/" + (month + 1) + "/" + year);

                    startService(new Intent(MainActivity.this, ShakeService.class));
                    finish();

            }
        });

        Button Driverreport=(Button)findViewById(R.id.DriverReport);
        final String userID =FirebaseAuth.getInstance().getCurrentUser().getUid();


        Driverreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataReference= FirebaseDatabase.getInstance().getReference();
                dataReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String firstName=dataSnapshot.child("users").child(userID).child("firstname").getValue().toString();
                        String lastName=dataSnapshot.child("users").child(userID).child("lastname").getValue().toString();
                        String Mobile=dataSnapshot.child("users").child(userID).child("mobile").getValue().toString();
                        String Parentid = dataSnapshot.child("MobileConn").child(Mobile).child("parentid").getValue().toString();
                        String Parentfirstname = dataSnapshot.child("users").child(Parentid).child("firstname").getValue().toString();
                        String Parentlastname = dataSnapshot.child("users").child(Parentid).child("lastname").getValue().toString();
                        String Parentmobile = dataSnapshot.child("users").child(Parentid).child("mobile").getValue().toString();
                        String accumelativeRate = dataSnapshot.child("users").child(userID).child("accumelativerate").getValue().toString();

                        Intent intent = new Intent(getApplicationContext(), driverReport.class);
                        intent.putExtra("firstName" , firstName);
                        intent.putExtra("lastName" , lastName);
                        intent.putExtra("Mobile" , Mobile);
                        intent.putExtra("Parentfirstname" , Parentfirstname);
                        intent.putExtra("Parentlastname" , Parentlastname);
                        intent.putExtra("Parentmobile" , Parentmobile);
                        intent.putExtra("accumelativeRate" , accumelativeRate);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        final Button Logout =(Button)findViewById(R.id.Logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), Signin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    public void onVideoFromCameraClick(View view) {
        Intent intent = new Intent(this, VideoFaceDetectionActivity.class);
        startActivity(intent);
    }




///////////////////////////////////Accident//////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aboutM) {
            startActivity(new Intent(MainActivity.this,About.class));
        }
        else if(id == R.id.close){
            System.exit(1);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onImageFromCameraClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), LENGTH_LONG);
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
        } else {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            mCurrentPhotoPath = image.getAbsolutePath();

            return image;
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, FaceDetectionActivity.class);
            intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
            startActivity(intent);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
