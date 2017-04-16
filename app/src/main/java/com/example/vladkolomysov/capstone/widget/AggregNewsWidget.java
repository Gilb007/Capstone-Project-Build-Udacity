package com.example.vladkolomysov.capstone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.widget.RemoteViews;

import com.example.vladkolomysov.capstone.activity.BrowserIntentActivity;
import com.example.vladkolomysov.capstone.R;

/**
 * Created by vladkolomysov
 */

// widget
public class AggregNewsWidget extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";
    public static final String IMAGE_ITEM = "com.example.android.stackwidget.IMAGE_ITEM";
    public static final String URL_ITEM = "com.example.android.stackwidget.URL_ITEM";
    public static final String DATABASE_CHANGED = "android.appwidget.action.APPWIDGET_UPDATE";
    String mTitle;
    String mUrl;
    String mImage;

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            mTitle = intent.getStringExtra(EXTRA_ITEM);
            mImage = intent.getStringExtra(IMAGE_ITEM);
            mUrl = intent.getStringExtra(URL_ITEM);

            Intent intent1 = new Intent(context, BrowserIntentActivity.class);
            intent1.putExtra("SOURCE", mTitle);
            intent1.putExtra("URL", mUrl);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        if (intent.equals(DATABASE_CHANGED))
        {
            final AppWidgetManager manager = AppWidgetManager.getInstance
                    (context);
            onUpdate(context, manager,
                    manager.getAppWidgetIds(new ComponentName(
                            context, AggregNewsWidget.class)
                    )
            );
        }
        else
            super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; ++i) {

            // intent
            Intent intent = new Intent(context, AggregNewsWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.news_aggreg_widget);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);

            rv.setEmptyView(R.id.stack_view, R.id.empty_view_news);

            Intent tIntent = new Intent(context, AggregNewsWidget.class);
            // action for the intent
            tIntent.setAction(AggregNewsWidget.TOAST_ACTION);
            tIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, tIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

