package com.example.vladkolomysov.capstone.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vladkolomysov.capstone.fragment.MainAggregNewsFragment;
import com.example.vladkolomysov.capstone.R;
import com.example.vladkolomysov.capstone.syncron.AggregNewsSyncronAdapter;

public class MainAggregNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aggreg_news);

        if (savedInstanceState == null) {
            MainAggregNewsFragment newsFragment = new MainAggregNewsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newsFragment)
                    .commit();
        }

        AggregNewsSyncronAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bookmark_an) {
            Intent intent = new Intent(this, FavouriteAggregNewsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
