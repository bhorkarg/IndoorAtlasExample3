package com.bhorkarg.indooratlasexample3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.resources.IAResourceManager;

public class LocationService extends Service {
    private final IBinder mBinder = new MyBinder();
    private IALocationManager mLocationManager;

    public IALocationManager getLocationManager() {
        return mLocationManager;
    }

    public IAResourceManager getResourceManager() {
        return IAResourceManager.create(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManager = IALocationManager.create(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
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
