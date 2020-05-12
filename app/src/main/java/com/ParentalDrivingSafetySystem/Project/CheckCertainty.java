package com.ParentalDrivingSafetySystem.Project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import android.os.CountDownTimer;
//import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class CheckCertainty extends Activity implements LocationListener {
    private LocationManager lm;
    public double latitude, longitude;
    public String No1;
    public  boolean check =false;



    public int counter=8;
    Button Dont;
    TextView Counter;

    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown);


        Dont= (Button) findViewById(R.id.cancel);
        Counter= (TextView) findViewById(R.id.counter);
        new CountDownTimer(9000, 1000){
            public void onTick(long millisUntilFinished){
                Counter.setText(String.valueOf(counter));
                counter--;
            }
            public  void onFinish(){
                AccelerometerReadings x=new AccelerometerReadings();
                String AccID=x.writeNewAccelerometer(ShakeListener.Acc);
                MainActivity.trip.setAccelerometerReadingsID(AccID);
                MainActivity.trip.writeNewTrip(MainActivity.trip);
                accident();

            }
        }.start();


            Dont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccelerometerReadings x=new AccelerometerReadings();
                    String AccID=x.writeNewAccelerometer(ShakeListener.Acc);
                    MainActivity.trip.setAccelerometerReadingsID(AccID);
                    MainActivity.trip.writeNewTrip(MainActivity.trip);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                 System.exit(1);


                }
            });

        }


    public void accident ()
    {
        try {
            File myFile = new File("/sdcard/.emergencyNumbers.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            No1 = myReader.readLine();
            // No2 = myReader.readLine();
            myReader.close();

            Toast.makeText(getApplicationContext(),
                    No1,
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        final SmsManager sms = SmsManager.getDefault();
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMessage(No1, null, "Help! I've met with an accident at http://maps.google.com/?q=" + String.valueOf(latitude) + "," + String.valueOf(longitude), null, null);
                sms.sendTextMessage(No1, null, "Nearby Hospitals http://maps.google.com/maps?q=hospital&mrt=yp&sll=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&output=kml", null, null);
                //  sms.sendTextMessage(No2, null, "Help! I've met with an accident at http://maps.google.com/?q="+ String.valueOf(latitude)+","+ String.valueOf(longitude), null, null);
                // sms.sendTextMessage(No2, null, "Nearby Hospitals http://maps.google.com/maps?q=hospital&mrt=yp&sll="+ String.valueOf(latitude)+","+ String.valueOf(longitude)+"&output=kml", null, null);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Signin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 8200);
    }
    @Override
    public void onLocationChanged(Location location){
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        Toast.makeText(getApplicationContext(),"Lat and Long extracted", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProviderDisabled(String provider){
    }
    @Override
    public void onProviderEnabled(String provider){
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}