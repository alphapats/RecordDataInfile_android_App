package com.example.amit.a163054001_recorddatainfile;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by Amit on 11-02-2018.
 */

public class sensors extends Fragment {
    private CheckBox cb_GPS,cb_Acc;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String GPS = "GPSKey";
    public static final String Accelerometer = "AccKey";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor myeditor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View sensor_view = inflater.inflate(R.layout.sensors, container, false);
        cb_GPS=(CheckBox) sensor_view.findViewById(R.id.GPS_chkbox);
        cb_Acc=(CheckBox) sensor_view.findViewById(R.id.acc_chkbox);
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        myeditor = sharedpreferences.edit();
        if (sharedpreferences.contains(GPS)) {
            String status1=sharedpreferences.getString(GPS, "");
            if(status1.contains("true"))
             {
                cb_GPS.setChecked(true);
            }
            else
                cb_GPS.setChecked(false);
        }

        if (sharedpreferences.contains(Accelerometer)) {
            String status2=sharedpreferences.getString(Accelerometer, "");
            if(status2.contains("true"))
            {
                cb_Acc.setChecked(true);
            }
            else
                cb_Acc.setChecked(false);

        }

        cb_GPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked())
                {
                    myeditor.putString(GPS, "true");
                    myeditor.commit();
                    Toast.makeText(getActivity(), "Selected GPS", Toast.LENGTH_SHORT).show();
                }

                else
                    myeditor.putString(GPS,"false");
                    myeditor.commit();
                    }
                });

        cb_Acc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked())
                {
                    myeditor.putString(Accelerometer, "true");
                    myeditor.commit();
                    Toast.makeText(getActivity(), "Selected Accelerometer", Toast.LENGTH_SHORT).show();
                }
                else
                    myeditor.putString(Accelerometer, "false");
                    myeditor.commit();
            }
        });

        return sensor_view;
    }



}