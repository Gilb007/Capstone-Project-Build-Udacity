package com.example.vladkolomysov.capstone.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.vladkolomysov.capstone.R;
import com.example.vladkolomysov.capstone.data.FavouriteANDataBaseHelper;
import com.example.vladkolomysov.capstone.data.FavouritesTable;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav_kolomysov
 */

class AggregNewsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<String> mWidItems = new ArrayList<String>();
    private ArrayList<String> mImages = new ArrayList<String>();
    private ArrayList<String> mUrls = new ArrayList<String>();
    private Context mContext;
    private int mWidgetId;

    public AggregNewsRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    // init connection / cursor
    public void onCreate() {
        Cursor cursor = mContext.getContentResolver().query(FavouritesTable.CONTENT_URI, null, null, null, null);
        List<FavouriteANDataBaseHelper> testRows = FavouritesTable.getRows(cursor, true);
        for (FavouriteANDataBaseHelper element : testRows) {
            mWidItems.add(element.title);
            mImages.add(element.image);
            mUrls.add(element.url);
        }

        // sleep 3 sec
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        mWidItems.clear();
        mImages.clear();
    }

    // size
    public int getCount() {
        return mWidItems.size();
    }

    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_aggreg_news);

        // load pics
        try {
            Bitmap b = Picasso.with(mContext).load(mImages.get(position)).resize(400, 400 ).centerCrop().get();
            rv.setImageViewBitmap(R.id.widget_item, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        rv.setTextViewText(R.id.widget_text, mWidItems.get(position));

        // extras
        Bundle extras = new Bundle();
        extras.putString(AggregNewsWidget.EXTRA_ITEM, mWidItems.get(position));
        extras.putString(AggregNewsWidget.IMAGE_ITEM, mImages.get(position));
        extras.putString(AggregNewsWidget.URL_ITEM, mUrls.get(position));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // listiner
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        // sleep 4 sec
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rv;
    }

    public RemoteViews getLoadingView() {

        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    // trigger widget changes
    public void onDataSetChanged() {

    }
}
