package com.ParentalDrivingSafetySystem.Project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class History extends AppCompatActivity {

    ListView listView;
    ArrayList<String> mleft;
    ArrayList<String> macc;
    ArrayList<String> mstop;
    ArrayList<String> mright;
    ArrayList <String> Tripids;
    ArrayList <String> St;
    ArrayList <String> Ed;
    ArrayList <String> Sleepy;
    ArrayList <String> SL;
    ArrayList <String> EL;

    String Driverid;
    Button show;
    EditText date;
    DatePickerDialog picker;
    DatabaseReference dataReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mleft = new ArrayList<String>();
        macc = new ArrayList<String>();
        mstop = new ArrayList<String>();
        mright = new ArrayList<String>();
        St = new ArrayList<String>();
        Ed = new ArrayList<String>();
        Sleepy = new ArrayList<String>();
        SL = new ArrayList<String>();
        EL = new ArrayList<String>();
        Tripids = new ArrayList<String>();
        show = (Button) findViewById(R.id.button);
        date = (EditText) findViewById(R.id.date);
        date.setInputType(InputType.TYPE_NULL);
        listView = findViewById(R.id.listView);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(History.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Button Back=(Button)findViewById(R.id.Back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mleft = new ArrayList<String>();
                macc = new ArrayList<String>();
                mstop = new ArrayList<String>();
                mright = new ArrayList<String>();
                St = new ArrayList<String>();
                Ed = new ArrayList<String>();
                Sleepy = new ArrayList<String>();
                SL = new ArrayList<String>();
                EL = new ArrayList<String>();
                Tripids = new ArrayList<String>();
                dataReference= FirebaseDatabase.getInstance().getReference();
                dataReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Bundle extras = getIntent().getExtras();
                        Driverid = extras.getString("driverid");

                        for (DataSnapshot x : dataSnapshot.child("Trip").getChildren())
                        {
                            Log.i("DriverID", Driverid);
                            Log.i("driverid", x.child("driverID").getValue().toString());

                            if(Driverid.equals(x.child("driverID").getValue().toString()) && date.getText().toString().equals(x.child("date").getValue().toString()))
                            {
                                Tripids.add(x.child("id").getValue().toString());
                                St.add(x.child("startTime").getValue().toString());
                                Ed.add(x.child("endTime").getValue().toString());
                                Sleepy.add(x.child("sleepy").getValue().toString());

                            }

                        }

                        for(DataSnapshot x : dataSnapshot.child("Report").getChildren())
                        {
                            for (int i = 0; i < Tripids.size(); i++)
                            {
                                if(Tripids.get(i).equals(x.child("tripid").getValue().toString()))
                                {
                                    mleft.add(x.child("left").getValue().toString());
                                    macc.add(x.child("acc").getValue().toString());
                                    mstop.add(x.child("stop").getValue().toString());
                                    mright.add(x.child("right").getValue().toString());
                                    SL.add(x.child("startlocation").getValue().toString());
                                    EL.add(x.child("endlocation").getValue().toString());
                                }
                            }

                        }
                        MyAdapter adapter = new MyAdapter(getApplicationContext(),mleft, macc, mstop, mright,St,Ed,Sleepy,SL,EL);
                        listView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> rleft;
        ArrayList<String> racc;
        ArrayList<String> rstop;
        ArrayList<String> rright;
        ArrayList<String> Start;
        ArrayList<String> End;
        ArrayList<String> Sleepy;
        ArrayList<String> Startlocation;
        ArrayList<String> Endlocation;



        MyAdapter(Context c, ArrayList<String> left, ArrayList<String> acc, ArrayList<String> stop, ArrayList<String> right,ArrayList<String> Start,ArrayList<String> End,ArrayList<String> Sleepy,ArrayList<String> Startlocation, ArrayList<String> Endlocation) {
            super(c, R.layout.row, R.id.left, left);
            this.context = c;
            this.rleft = left;
            this.rright = right;
            this.racc = acc;
            this.rstop = stop;
            this.Start = Start;
            this.End = End;
            this.Sleepy = Sleepy;
            this.Startlocation = Startlocation;
            this.Endlocation = Endlocation;

        }

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myleft = row.findViewById(R.id.LeftNumberc);
            TextView myacc = row.findViewById(R.id.AccelerationNumberc);
            TextView mystop = row.findViewById(R.id.StopNumberc);
            TextView myright = row.findViewById(R.id.RightNumberc);
            TextView StartTime = row.findViewById(R.id.StartTime);
            TextView EndTime = row.findViewById(R.id.EndTime);
            TextView Sleepyy = row.findViewById(R.id.Sleep);
            TextView StartDestination = row.findViewById(R.id.StartLocation);
            TextView EndDestination= row.findViewById(R.id.EndLocation);

            myleft.setText(rleft.get(position));
            myright.setText(rright.get(position));
            myacc.setText(racc.get(position));
            mystop.setText(rstop.get(position));
            StartTime.setText(Start.get(position));
            EndTime.setText(End.get(position));
            Sleepyy.setText(Sleepy.get(position));
            StartDestination.setText(Startlocation.get(position));
            EndDestination.setText(Endlocation.get(position));

            int lefts=Integer.parseInt(rleft.get(position));
            ProgressBar progressBarLeft=row.findViewById(R.id.progressBarLeftc);
            if(lefts<=5)
            {


                LayerDrawable layerDrawable = (LayerDrawable) progressBarLeft.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

                progressBarLeft.setProgress(lefts);

            }
            else if(lefts>5 &&lefts<=10)
            {

                LayerDrawable layerDrawable = (LayerDrawable) progressBarLeft.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                progressBarLeft.setProgress(lefts);


            }
            else
            {
                LayerDrawable layerDrawable = (LayerDrawable) progressBarLeft.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


                progressBarLeft.setProgress(lefts);
            }
            int rights=Integer.parseInt(rright.get(position));
            ProgressBar progressBarRight=row.findViewById(R.id.progressBarRightc);

            if(rights<=5)
            {


                LayerDrawable layerDrawable = (LayerDrawable) progressBarRight.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);


                progressBarRight.setProgress(rights);


            }
            else if(rights>5 &&rights<=10)
            {

                LayerDrawable layerDrawable = (LayerDrawable) progressBarRight.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                progressBarRight.setProgress(rights);


            }
            else
            {
                LayerDrawable layerDrawable = (LayerDrawable) progressBarRight.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


                progressBarRight.setProgress(rights);
            }
            int accs=Integer.parseInt(racc.get(position));
            ProgressBar progressBarAcceleration=row.findViewById(R.id.progressBarAccelerationc);
            if(accs<=5)
            {


                LayerDrawable layerDrawable = (LayerDrawable) progressBarAcceleration.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);


                progressBarAcceleration.setProgress(accs);


            }
            else if(accs>5 &&accs<=10)
            {

                LayerDrawable layerDrawable = (LayerDrawable) progressBarAcceleration.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                progressBarAcceleration.setProgress(accs);


            }
            else
            {
                LayerDrawable layerDrawable = (LayerDrawable) progressBarAcceleration.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


                progressBarAcceleration.setProgress(accs);
            }
            int stops=Integer.parseInt(rstop.get(position));
            ProgressBar progressBarStop=row.findViewById(R.id.progressBarStopc);

            if(stops<=5)
            {


                LayerDrawable layerDrawable = (LayerDrawable) progressBarStop.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

                progressBarStop.setProgress(stops);
            }
            else if(stops>5 &&stops<=10)
            {

                LayerDrawable layerDrawable = (LayerDrawable) progressBarStop.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);


                progressBarStop.setProgress(stops);


            }
            else
            {
                LayerDrawable layerDrawable = (LayerDrawable) progressBarStop.getProgressDrawable();
                Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


                progressBarStop.setProgress(stops);
            }
            return row;
        }
    }
}
