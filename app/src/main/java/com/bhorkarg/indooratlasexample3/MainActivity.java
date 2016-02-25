package com.bhorkarg.indooratlasexample3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stopService(new Intent(this, LocationService.class));
    }
}
