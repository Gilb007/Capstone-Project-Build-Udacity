package com.example.vladkolomysov.capstone.syncron;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by vladkolomysov
 */

// Service
public class AggregNewsSyncronService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static AggregNewsSyncronAdapter sSunshineSyncAdapter = null;

    @Override
    public void onCreate() {
        // like sunshine
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new AggregNewsSyncronAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}
