package com.bhorkarg.indooratlasexample3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "Starting location service...", Toast.LENGTH_SHORT).show();

        //Start LocationService
        Intent intentService = new Intent(this, LocationService.class);
        startService(intentService);
    }

    public void onButtonClick (View v) {
        Intent intentActivity;

        switch (v.getId()) {
            case R.id.btnLocation:
                intentActivity = new Intent(this, LocationActivity.class);
                startActivity(intentActivity);
                break;

            case R.id.btnFloorPlan:
                intentActivity = new Intent(this, FloorPlanActivity.class);
                startActivity(intentActivity);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intentService = new Intent(this, LocationService.class);
        stopService(intentService);
    }
}
