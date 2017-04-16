package com.example.vladkolomysov.capstone.syncron;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import com.example.vladkolomysov.capstone.R;

/**
 * Created by vladkolomysov
 */

public class AggregNewsSyncronAdapter extends AbstractThreadedSyncAdapter {

    // 60 seconds (1 minute) * 60 = 1 hour
    private static final int SYNCRONIZE_INTERVAL = 60 * 180;
    private static final int SYNCRONIZE_FLEXTIME = SYNCRONIZE_INTERVAL / 3;

    AggregNewsSyncronAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    // start syncronization
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

    }

    private static void configurePeriodicSyncronization(Context context, int syncInterval, int flexTime) {
        Account account = getSyncronizationAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //timer in periodic syncronization like in popular movies
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void syncronizeImmediately(Context context) {
        // set bundle
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncronizationAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    private static Account getSyncronizationAccount(Context context) {

        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if (null == accountManager.getPassword(newAccount)) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        AggregNewsSyncronAdapter.configurePeriodicSyncronization(context, SYNCRONIZE_INTERVAL, SYNCRONIZE_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncronizeImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncronizationAccount(context);
    }
}
