package com.bhorkarg.indooratlasexample3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.resources.IAResourceManager;

public class LocationService extends Service {
    private final IBinder mBinder = new MyBinder();

    private IALocationManager mLocationManager; //Single instance
    private IAResourceManager mResourceManager;


    public IALocationManager getLocationManager() {
        return mLocationManager;
    }


    //Return the Resource Manager
    public IAResourceManager getResourceManager() {
        return mResourceManager;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManager = IALocationManager.create(this);
        mResourceManager = IAResourceManager.create(this);

        Toast.makeText(LocationService.this, "Location Manager created", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        Toast.makeText(LocationService.this, "Destroying Location Manager...", Toast.LENGTH_SHORT).show();
        mLocationManager.destroy();
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY; //Keep service running
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    //Binder to return service instance
    public class MyBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }
}
