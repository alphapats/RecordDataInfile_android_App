package com.example.amit.a163054001_recorddatainfile;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.support.v4.util.CircularArray;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.amit.a163054001_recorddatainfile.login.FName;
import static com.example.amit.a163054001_recorddatainfile.sensors.Accelerometer;
import static com.example.amit.a163054001_recorddatainfile.sensors.GPS;


/**
 * Created by Amit on 13-02-2018.
 */

public class record extends Fragment {
    private Switch RecordSwitch;
    private RadioButton rb_stand,rb_walk;

    Boolean GPS_checked=false;
    Boolean acc_checked=false;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ActName = "ActKey";
    Button btnclose;
    View record_view;
    DateFormat df=new SimpleDateFormat("HH:mm:ss.SSS");
    String start_time,stop_time;
    double latitude, longitude;
    double AccX,AccY,AccZ;
    int hr1,min1,sec1;
    int hr2,min2,sec2;
    CircularArray display= new  CircularArray(5);
    long time=0;
    int count=0;


    SharedPreferences sharedpreferences;
    SharedPreferences.Editor myeditor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        record_view=inflater.inflate(R.layout.record, container, false);
        RecordSwitch = (Switch) record_view.findViewById(R.id.RecordSwitch);
        btnclose = (Button) record_view.findViewById(R.id.closebtn);


        RadioGroup radioGroup = (RadioGroup) record_view.findViewById(R.id.enteractivity);
        rb_stand=(RadioButton) record_view.findViewById(R.id.stationary) ;
        rb_walk=(RadioButton) record_view.findViewById(R.id.walking) ;

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        myeditor = sharedpreferences.edit();

        if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        if (sharedpreferences.contains(ActName) ){
            String status1=sharedpreferences.getString(ActName, "");
            if(status1.contains("stand"))
            {
                rb_stand.setChecked(true);
            }
            if(status1.contains("walk"))
            {
                rb_walk.setChecked(true);
            }

        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.stationary:
                        myeditor.putString(ActName, "stand");
                        myeditor.commit();
                        Toast.makeText(getActivity(), "Selected Stationary", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.walking:
                        myeditor.putString(ActName, "walk");
                        myeditor.commit();
                        Toast.makeText(getActivity(), "Selected Walking", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });



        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //Intent intent=new Intent(currentclass.this,statingclass.class);
                getActivity().finish();

            }
        });

        RecordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (sharedpreferences.contains(GPS)) {
                    String status1=sharedpreferences.getString(GPS, "");
                    if(status1.contains("true"))
                    {
                        GPS_checked=true;
                    }
                    else
                    {
                        GPS_checked=false;
                    }

                }

                if (sharedpreferences.contains(Accelerometer)) {
                    String status2=sharedpreferences.getString(Accelerometer, "");
                    if(status2.contains("true"))
                    {
                        acc_checked=true;
                    }
                    else
                        acc_checked=false;

                }



                if (bChecked) {

                    if(GPS_checked) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                        }

                        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Toast.makeText(getContext(), "GPS is not enabled", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    getActivity().startService(new Intent(getActivity(),Recordservice.class));


                } else {
                    //stop_time=System.currentTimeMillis();
                    //getActivity().stopService(new Intent(getActivity(), ServiceBothSensors.class));
                    stop_time = df.format(Calendar.getInstance().getTime());

                    getActivity().stopService(new Intent(getActivity(),Recordservice.class));
                    print_reading();;
                }

                //Toast.makeText(getApplicationContext(), "Timer started", Toast.LENGTH_SHORT).show();
            }
        });



        return record_view;
    }


    private void print_reading()
    {
        final TextView txtreading1,txtreading2,txtreading3,txtreading4,txtreading5;
        txtreading1=(TextView) record_view.findViewById(R.id.reading1);
        txtreading2=(TextView) record_view.findViewById(R.id.reading2);
        txtreading3=(TextView) record_view.findViewById(R.id.reading3);
        txtreading4=(TextView) record_view.findViewById(R.id.reading4);
        txtreading5=(TextView) record_view.findViewById(R.id.reading5);



        //time=(stop_time-start_time)/1000;
        StringBuilder sb = new StringBuilder();
        if(GPS_checked){
            sb.append("GPS,");
        }
        if(acc_checked){
            sb.append("Accerelometer,");
        }


        sb.append(stop_time);


        count++;

        if (count<=5)
        {
            display.addLast(sb);
            if(count ==1){

                txtreading1.setText(sb); //leave this line to assign a specific text
                //txtreading1.setText(R.string.reading1);


            }

            if(count ==2){

                txtreading2.setText(sb); //leave this line to assign a specific text
                //txtreading2.setText(R.string.reading2);
            }

            if(count ==3){

                txtreading3.setText(sb); //leave this line to assign a specific text
                //txtreading3.setText(R.string.reading3);
            }

            if(count ==4){

                txtreading4.setText(sb); //leave this line to assign a specific text
                //txtreading4.setText(R.string.reading4);
            }

            if(count ==5){

                txtreading5.setText(sb); //leave this line to assign a specific text
                // txtreading5.setText(R.string.reading5);
            }
        }
        else
        {
            display.popFirst();
            display.addLast(sb);

            //String temp_val = new String();
            String temp_val=display.get(0).toString();
            txtreading1.setText(temp_val);


            temp_val=display.get(1).toString();
            txtreading2.setText(temp_val);


            temp_val=display.get(2).toString();
            txtreading3.setText(temp_val);


            temp_val=display.get(3).toString();
            txtreading4.setText(temp_val);

            temp_val=display.get(4).toString();
            txtreading5.setText(temp_val);


        }

    }



}
