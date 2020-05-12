package com.ParentalDrivingSafetySystem.Project;

import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.vision.face.Face;

import java.util.Calendar;
import java.util.Date;

import static com.ParentalDrivingSafetySystem.Project.VideoFaceDetectionActivity.context;

public class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;
    private Boolean playing = false;
    Date startTime=null;
    public boolean sleepy= false;
    Date endTime=null ;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void setId(int id) {
        mFaceId = id;
    }

    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }
        Paint p = new Paint();
        VideoFaceDetectionActivity.mediaPlayer = new MediaPlayer();
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);

        canvas.drawText("Left Eye" + face.getIsLeftEyeOpenProbability(), 0, canvas.getHeight()/4, mIdPaint);
        canvas.drawText("Right Eye" + face.getIsRightEyeOpenProbability(), 0, canvas.getHeight(), mIdPaint);
        if(((face.getIsRightEyeOpenProbability()<0.2) && (face.getIsRightEyeOpenProbability()> -1.0))&&((face.getIsLeftEyeOpenProbability()<0.2)&& (face.getIsLeftEyeOpenProbability()>-1.0))&&startTime==null){

            startTime= Calendar.getInstance().getTime();
            Log.d("StartTime", " "+startTime.getTime());

        }
        else if(((face.getIsRightEyeOpenProbability()<0.2) && (face.getIsRightEyeOpenProbability()> -1.0))&&((face.getIsLeftEyeOpenProbability()<0.2)&& (face.getIsLeftEyeOpenProbability()>-1.0)))
        {
            endTime= Calendar.getInstance().getTime();
            long s=endTime.getTime()-startTime.getTime();
            Log.d("EndTime", " "+endTime.getTime());
            Log.d("TotalTime", " "+ s);

            if (endTime.getTime()-startTime.getTime()>2000&&endTime.getTime()-startTime.getTime()<3000)
            {
                sleepy=true;
                MainActivity.trip.setSleepy(true);
                p.setColor(Color.RED);
                p.setTextSize(280);
                canvas.drawText("SLEEPY", 0,canvas.getHeight()/2, p);
                try{
                    if(playing){
//                    VideoFaceDetectionActivity.mediaPlayer.stop();
//                    playing = false;
                    }else {
                        playing = true;
                        AssetFileDescriptor as = context.getAssets().openFd("alarm.mp3");
                        VideoFaceDetectionActivity.mediaPlayer.setDataSource(as.getFileDescriptor(), as.getStartOffset(), as.getLength());
                        as.close();
                        VideoFaceDetectionActivity.mediaPlayer.prepare();
                        VideoFaceDetectionActivity.mediaPlayer.start();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playing = false;
                            }
                        }, 2000                                                                        );
                    }

                }catch (Exception e){

                };
            }
        }
        if(((face.getIsRightEyeOpenProbability()>0.2))&&((face.getIsLeftEyeOpenProbability()>0.2))&&startTime!=null&&sleepy==true)
        {startTime= Calendar.getInstance().getTime();}

        else if(((face.getIsRightEyeOpenProbability()>0.2))&&((face.getIsLeftEyeOpenProbability()>0.2))&&startTime!=null&&sleepy==false)
        {startTime= Calendar.getInstance().getTime();}


        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, mBoxPaint);

         }

    public boolean getSlee()
    {
        return sleepy;
    }


}
