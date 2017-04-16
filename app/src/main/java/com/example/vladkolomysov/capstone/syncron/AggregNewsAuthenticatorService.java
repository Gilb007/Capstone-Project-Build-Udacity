package com.example.vladkolomysov.capstone.syncron;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by vladkolomysov
 */

public class AggregNewsAuthenticatorService extends Service {

    private AggregNewsAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new AggregNewsAuthenticator(this);
    }

    // return the authenticator
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}

