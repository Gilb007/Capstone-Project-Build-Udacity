package com.example.vladkolomysov.capstone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vladkolomysov.capstone.fragment.FavouriteAggregNewsFragment;
import com.example.vladkolomysov.capstone.R;
import com.example.vladkolomysov.capstone.data.FavouritesTable;

/**
 * Created by vladkolomysov
 */

public class FavouriteAggregNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_aggreg_news);

        if (savedInstanceState == null) {
            FavouriteAggregNewsFragment favFragment = new FavouriteAggregNewsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fav_container, favFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete) {
            getContentResolver().delete(FavouritesTable.CONTENT_URI, null, null);
        }
        return super.onOptionsItemSelected(item);
    }
}
