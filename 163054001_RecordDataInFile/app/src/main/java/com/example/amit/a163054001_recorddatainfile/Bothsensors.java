package com.example.amit.a163054001_recorddatainfile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by amitp on 23-03-2018.
 */

public class Bothsensors implements SensorEventListener, LocationListener {


    SensorEvent sensorEvent;
    Location location;
    Context context;

    public Context getContext() {
        return context;
    }



    public SensorEvent getSensorEvent() {
        return sensorEvent;
    }

    public Location getLocation() {

        return location;
    }

    Bothsensors(Context context){
        this.context = context;

        SensorManager sm = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);
                this.location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        this.sensorEvent = sensorEvent;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
     public void onStatusChanged(String provider, int status, Bundle extras) {
        String state = "";
        switch (status) {

            case LocationProvider.AVAILABLE:
                state = "AVAILABLE";

            case LocationProvider.OUT_OF_SERVICE:
                state = "OUT_OF_SERVICE";

            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                state = "TEMPORARILY_UNAVAILABLE";
        }

        Toast.makeText(getContext(), state, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
