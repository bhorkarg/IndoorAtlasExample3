/*

On Resume, connect to LocationService. On Pause, disconnect.
When connected to LocationService, get the LocationManager object.

*/

package com.bhorkarg.indooratlasexample3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

public class LocationActivity extends AppCompatActivity {

    private final String TAG = "IAExample3";

    LocationService mLocationService;

    IALocationManager mLocationManager; //Set when service is connected

    boolean mGettingLocation = false;

    //Declare required views
    TextView txtLat;
    TextView txtLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //Get references to views
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLng = (TextView) findViewById(R.id.txtLng);
    }


    @Override
    protected void onResume () {
        super.onResume();

        //connect to the location service
        Intent intentService = new Intent(this, LocationService.class);
        bindService(intentService, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mGettingLocation) {
            stopLocationUpdates();
        }

        unbindService(mServiceConnection);
    }


    //Click handler for all buttons
    public void onButtonClick(View v) {
        boolean result;

        switch (v.getId()) {

            case R.id.btnStartLoc:
                startLocationUpdates();
                break;

            case R.id.btnStopLoc:
                stopLocationUpdates();
                break;
        }

    }


    //Starts requesting location updates
    private void startLocationUpdates() {
        boolean result;
        if (!mGettingLocation) {
            result = mLocationManager.requestLocationUpdates(IALocationRequest.create(), mLocationListener);
            if (result) {
                mGettingLocation = true;
                Toast.makeText(LocationActivity.this, "Getting location...", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Already requested
            Toast.makeText(LocationActivity.this, "Already started...", Toast.LENGTH_SHORT).show();
        }
    }


    //Stops requesting location updates
    private void stopLocationUpdates() {
        boolean result;
        result = mLocationManager.removeLocationUpdates(mLocationListener);
        if (result) {
            Toast.makeText(LocationActivity.this, "Stopped...", Toast.LENGTH_SHORT).show();
            mGettingLocation = false;
            txtLat.setText("Lat:");  //Reset the textView
            txtLng.setText("Lng:");
        }
    }


    //Listener for location updates. Receives callbacks.
    private IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            txtLat.setText("Lat: " + iaLocation.getLatitude());
            txtLng.setText("Lng: " + iaLocation.getLongitude());

            Log.v(TAG, iaLocation.getLatitude() + ", " + iaLocation.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //nothing
        }
    };


    //Connection to the Location Service
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //When LocationService is Connected, set mLocationService
            LocationService.MyBinder binder = (LocationService.MyBinder) iBinder;
            mLocationService = binder.getService();

            //set LocationManager
            mLocationManager = mLocationService.getLocationManager();

            Toast.makeText(LocationActivity.this, "Bound to location service", Toast.LENGTH_SHORT).show();

            //Enable location Start/Stop buttons now
            Button btnStart = (Button) findViewById(R.id.btnStartLoc);
            Button btnStop = (Button) findViewById(R.id.btnStopLoc);
            btnStart.setEnabled(true);
            btnStop.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //nothing
        }
    };
}
