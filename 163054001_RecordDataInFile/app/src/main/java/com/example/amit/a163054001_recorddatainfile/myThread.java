package com.example.amit.a163054001_recorddatainfile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.amit.a163054001_recorddatainfile.login.Age;
import static com.example.amit.a163054001_recorddatainfile.login.Email;
import static com.example.amit.a163054001_recorddatainfile.login.FName;
import static com.example.amit.a163054001_recorddatainfile.login.LName;
import static com.example.amit.a163054001_recorddatainfile.login.Mobilenumber;
import static com.example.amit.a163054001_recorddatainfile.login.Sex;
import static com.example.amit.a163054001_recorddatainfile.record.ActName;
import static com.example.amit.a163054001_recorddatainfile.sensors.Accelerometer;
import static com.example.amit.a163054001_recorddatainfile.sensors.GPS;


/**
 * Created by amitp on 23-03-2018.
 */

public class myThread implements Runnable {


    boolean runningFlag;
    Boolean GPS_checked=false;
    Boolean acc_checked=false;
    Context context;
    public static final String MyPREFERENCES = "MyPrefs" ;

    public void setSensorsData(Bothsensors sensorsData) {
        this.sensorsData = sensorsData;
    }

    Bothsensors sensorsData;

    public void setRunningFlag(boolean flag){
        runningFlag = flag;
    }

    /* Checks if external storage is available for read and write */
    private void writeToFile(String fileName,String text,Context context) throws IOException {

        //Log.d("writeTofile","recordThread");

        //Log.d("writeTofile","record in IF"+checkpermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        if(isExternalStorageWritable()  ){

            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsoluteFile()+ "/cs653");
            Log.d("dir",""+Dir.exists());
            if(!Dir.exists())
            {
                Dir.mkdir();
            }
            File file = new File(Dir, fileName);

            try {

                FileOutputStream stream = new FileOutputStream(file, true);
                stream.write(text.getBytes());

                stream.close();

            }
            catch(IOException e){
                e.printStackTrace();
            }


            Log.d("file pathname",file.getParent());
        }

    }


    public boolean checkpermission(String p)
    {

            int check = ContextCompat.checkSelfPermission(context, p);
            if(check!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity)context, new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 325);}
            return (check == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        else{
            Log.d("isExternal","not available");
            return false;
        }
    }
    public void run() {

        runningFlag = true;

        Context context = sensorsData.getContext();
        SharedPreferences sharedpreferences;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        StringBuilder text= new StringBuilder();

        text.append(sharedpreferences.getString(FName,""));
        text.append(",");
        text.append(sharedpreferences.getString(LName, ""));
        text.append(",");
        text.append(sharedpreferences.getString(Mobilenumber, ""));
        text.append(",");
        text.append(sharedpreferences.getString(Email, ""));
        text.append(",");
        text.append(sharedpreferences.getString(Sex, ""));
        text.append(",");
        text.append(sharedpreferences.getString(Age, ""));
        text.append("\n");

        String filename = "Reading_" + new SimpleDateFormat("HH:mm:ss.SS").format(Calendar.getInstance().getTime()) + ".csv";
        try {
                writeToFile(filename,text.toString(),context);
                //Log.d("Thread","written to file");
        } catch (IOException e) {
                Log.d("Thread","Error writing to file");
        }



        String status1=sharedpreferences.getString(GPS, "");
            if(status1.contains("true"))
            {
                Log.d("GPS Checked","true");
                GPS_checked=true;
            }
            else
            {
                Log.d("GPS Checked","false");
                GPS_checked=false;
            }



        String status2=sharedpreferences.getString(Accelerometer, "");
            if(status2.contains("true"))
            {
                Log.d("Acc Checked","true");
                acc_checked=true;
            }
            else{
                Log.d("Acc Checked","false");
                acc_checked=false;

            }



        text.setLength(0); //clear the string builder

        while(runningFlag) {

            //SharedPreferences sharedpreferences = context.getSharedPreferences("MyPREFERENCES",Context.MODE_PRIVATE);


            text.append(new SimpleDateFormat("HH:mm:ss.SS").format(Calendar.getInstance().getTime()));
            if(GPS_checked){
                Location location = sensorsData.getLocation();
                if(location != null){
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    text.append(","+lat+","+lon);
                }
                else{
                    text.append(",,");
                }
            }
            else {
                text.append(",,");
            }

            if(acc_checked){
                SensorEvent sensorEvent = sensorsData.getSensorEvent();
                if(sensorEvent == null){
                    text.append(",,,");
                }
                else{
                    text.append(","+sensorEvent.values[0] + ","+sensorEvent.values[1] + ","+sensorEvent.values[2]);
                }

            }
            else{
                text.append(",,,");
            }
            text.append(",");
            text.append(sharedpreferences.getString(ActName,""));
            text.append("\n");

            try {
                writeToFile(filename,text.toString(),context);
                Log.d("Thread","Written to file");
            } catch (IOException e) {
                Log.d("Thread","Error writing to file");
            }



            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("Thread value",text.toString());

            text.setLength(0); // clear the string builder
        }

        Log.d("Thread","Exited");
    }






}

