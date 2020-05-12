package com.ParentalDrivingSafetySystem.Project;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class VideoFaceDetectionActivity extends AppCompatActivity implements LocationListener {

    private CameraPreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private CameraSource mCameraSource = null;
    FaceGraphic face;
    public static CheckCertainty ch=new CheckCertainty();
    private static final String TAG = "VideoFaceDetection";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int RC_HANDLE_GMS = 2;
    public static Context context;
    public static MediaPlayer mediaPlayer;
    Date EndTime=null;
    public static Trip trip=new Trip();
    public double latitude, longitude;
    private LocationManager lm;
    public static Coordinates co=new Coordinates();
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        setContentView(R.layout.activation);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_face_detection);
        context = getBaseContext();
        mPreview = findViewById(R.id.preview);
        mGraphicOverlay = findViewById(R.id.faceOverlay);
        face=new FaceGraphic(mGraphicOverlay);

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, 10, this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            createCameraSource();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_PERMISSION && resultCode == RESULT_OK) {
            createCameraSource();
        }
    }

    private void createCameraSource() {
        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());
        Canvas c = new Canvas();
        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480 )
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }


    private void startCameraSource() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }

    }


   public void EndTrip(View view) {

      boolean ss=MainActivity.trip.isSleepy();
       EndTime= Calendar.getInstance().getTime();
       String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
       DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
       String EndDate = dateFormat.format(EndTime);
       AccelerometerReadings x=new AccelerometerReadings();
       String AccID=x.writeNewAccelerometer(ShakeListener.Acc);
       MainActivity.trip.setAccelerometerReadingsID(AccID);
       MainActivity.trip.setSleepy(ss);
       MainActivity.trip.setEndTime(EndDate);
       double lat=latitude;
       double lon=longitude;
       MainActivity.trip.setEndLongitude(lon);
       MainActivity.trip.setEndLat(lat);
        MainActivity.trip.writeNewTrip(MainActivity.trip);
       // onDestroy();
        Intent in = new Intent(this, MainActivity.class);
         startActivity(in);

    }


    public void Stop(View view) {
        float[] dany ={9,9,9};
        ShakeListener.Acc.X.add(dany[0]);
        ShakeListener.Acc.Y.add(dany[1]);
        ShakeListener.Acc.Z.add(dany[2]);
    }

    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
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
