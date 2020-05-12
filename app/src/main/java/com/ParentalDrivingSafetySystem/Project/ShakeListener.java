package com.ParentalDrivingSafetySystem.Project;

import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;


public class ShakeListener implements SensorListener {
    private static final int FORCE_THRESHOLD = 10000;
    private static final int TIME_THRESHOLD = 75;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 150;
    private static final int SHAKE_COUNT = 1;
    public static Coordinates Acc = new Coordinates();
    private SensorManager mSensorMgr;
    private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
    private ArrayList<Float> X,Y,Z;
    private long mLastTime;
    private OnShakeListener mShakeListener;
    private Context mContext;
    private int mShakeCount = 0;
    private long mLastShake;
    private long mLastForce;
    FirebaseAuth mAuth;

    static final float alpha = 0.05f;

    public float[] lowPass(float[] output, float[] input)
    {
        output[0] = output[0] + alpha * (input[0] - output[0]);
        output[1] = output[1] + alpha * (input[1] - output[1]);
        output[2] = output[2] + alpha * (input[2] - output[2]);
        return output;
    }

    public interface OnShakeListener {
        public void onShake();
    }

    public ShakeListener(Context context) {
        mContext = context;

        X=new ArrayList<>();
        Y=new ArrayList<>();
        Z=new ArrayList<>();
        resume();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
    }

    public void resume() {
        mSensorMgr = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorMgr == null) {
            throw new UnsupportedOperationException("Sensors not supported");
        }
        boolean supported = mSensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        if (!supported) {
            mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
            throw new UnsupportedOperationException("Accelerometer not supported");
        }
    }

    public void pause() {
        if (mSensorMgr != null) {
            mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
            mSensorMgr = null;
        }
    }

    public void onAccuracyChanged(int sensor, int accuracy) {
    }

    public void onSensorChanged(int sensor, float[] values) {
        mAuth = FirebaseAuth.getInstance();
        if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
        long now = System.currentTimeMillis();

        if ((now - mLastForce) > SHAKE_TIMEOUT) {

            mShakeCount = 0;
        }

        if ((now - mLastTime) > TIME_THRESHOLD) {
            long diff = now - mLastTime;
            float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
            if (speed > FORCE_THRESHOLD) {
                if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                    mLastShake = now;
                    mShakeCount = 0;
                    if (mShakeListener != null) {
                        mShakeListener.onShake();
                    }
                }
                mLastForce = now;
            }
            mLastTime = now;
            mLastX = values[SensorManager.DATA_X];
            mLastY = values[SensorManager.DATA_Y];
            mLastZ = values[SensorManager.DATA_Z];



           float[] mPoint ={mLastX,mLastY,mLastZ};
           float[] outputPoint= new float[3];
           lowPass(outputPoint,mPoint);
            Acc.X.add(outputPoint[0]);
            Acc.Y.add(outputPoint[1]);
            Acc.Z.add(outputPoint[2]);

        }

    }
}