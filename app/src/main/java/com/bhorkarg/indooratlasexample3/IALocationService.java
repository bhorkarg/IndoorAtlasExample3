package com.bhorkarg.indooratlasexample3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class IALocationService extends Service {
    public IALocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
