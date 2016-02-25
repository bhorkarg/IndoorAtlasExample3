/*

On Resume, connect to LocationService. On Pause, disconnect.
When connected to LocationService, start listening to location, region.
When enterRegion, get floorplan task, run it and wait for callback.
Upon getting the floorplan, load the url in ImageView

*/

package com.bhorkarg.indooratlasexample3;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.squareup.picasso.Picasso;

public class FloorPlanActivity extends AppCompatActivity {

    LocationService mLocationService; //bind in onResume

    IALocationManager mLocationManager; //set when service is connected
    IAResourceManager mResourceManager;

    IATask<IAFloorPlan> mIATask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intentService = new Intent(this, LocationService.class);
        bindService(intentService, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManager != null) {
            mLocationManager.unregisterRegionListener(mRegionListener);
            mLocationManager.removeLocationUpdates(mLocationListener);
        }

        unbindService(mServiceConnection);
    }



    private IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {} //not required

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {} //not required
    };


    private IARegion.Listener mRegionListener = new IARegion.Listener() {
        @Override
        public void onEnterRegion(IARegion iaRegion) {
            //Cancel earlier stuck task if any
            if (mIATask != null && !mIATask.isCancelled()) {
                mIATask.cancel();
            }

            //Get FloorPlan loading task
            mIATask = mResourceManager.fetchFloorPlanWithId(iaRegion.getId());
            mIATask.setCallback(floorPlanResultCallback, Looper.getMainLooper());
        }

        @Override
        public void onExitRegion(IARegion iaRegion) {
            //Clear the image
            ImageView imageFloorPlan = (ImageView) findViewById(R.id.imageFloorPlan);
            imageFloorPlan.setImageURI(null);
            Toast.makeText(FloorPlanActivity.this, "Moved out of region", Toast.LENGTH_SHORT).show();
        }
    };


    IAResultCallback<IAFloorPlan> floorPlanResultCallback = new IAResultCallback<IAFloorPlan>() {
        @Override
        public void onResult(IAResult<IAFloorPlan> iaResult) {
            if (iaResult.isSuccess()) {
                //get the image and load it into ImageView
                IAFloorPlan iaFloorPlan = iaResult.getResult();

                ImageView imageFloorPlan = (ImageView) findViewById(R.id.imageFloorPlan);
                Picasso.with(FloorPlanActivity.this).load(iaFloorPlan.getUrl()).into(imageFloorPlan);
            }
        }
    };


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //When LocationService is Connected, set mLocationService
            LocationService.MyBinder binder = (LocationService.MyBinder) iBinder;
            mLocationService = binder.getService();

            //set locationManager and resourceManager
            mLocationManager = mLocationService.getLocationManager();
            mResourceManager = mLocationService.getResourceManager();

            Toast.makeText(FloorPlanActivity.this, "Bound to location service", Toast.LENGTH_SHORT).show();

            //Start listening to location updates
            mLocationManager.registerRegionListener(mRegionListener);
            mLocationManager.requestLocationUpdates(IALocationRequest.create(), mLocationListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //Nothing
        }
    };
}