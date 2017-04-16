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
 * Created by vladkolomysov
 */

public class AggregNewsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AggregNewsRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

