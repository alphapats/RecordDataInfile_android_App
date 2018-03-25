package com.example.amit.a163054001_recorddatainfile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Recordservice extends Service {
    myThread myThread;
    Thread thread;
    int check;

    public Recordservice() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("RecordService","service start");
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();

        myThread = new myThread();



        Bothsensors sensorsData = new Bothsensors(this);
        myThread.setSensorsData(sensorsData);
        thread = new Thread(myThread);
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.d("RecordService","service destroy");

        myThread.setRunningFlag(false);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
