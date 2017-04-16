package com.example.vladkolomysov.capstone.fragment;


import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vladkolomysov.capstone.adapter.FavouriteAggregNewsAdapter;
import com.example.vladkolomysov.capstone.R;
import com.example.vladkolomysov.capstone.data.FavouriteANDataBaseHelper;
import com.example.vladkolomysov.capstone.data.FavouritesTable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by vladkolomysov
 */
// to see favourite news
public class FavouriteAggregNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private FavouriteAggregNewsAdapter mFavouriteAdapter;
    StaggeredGridLayoutManager mLayoutManager;
    ArrayList<String> mId = new ArrayList<>();
    ArrayList<String> mName = new ArrayList<String>();
    ArrayList<String> mDescription = new ArrayList<String>();
    ArrayList<String> mNewsurl = new ArrayList<String>();
    ArrayList<String> mImage = new ArrayList<String>();


    public FavouriteAggregNewsFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourite_aggreg_news, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.favourite_agregnews_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        // for grid
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFavouriteAdapter = new FavouriteAggregNewsAdapter(getActivity(), mId, mName, mDescription, mNewsurl, mImage, null);
        mRecyclerView.setAdapter(mFavouriteAdapter);

        dataDB();

        return rootView;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), FavouritesTable.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavouriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavouriteAdapter.swapCursor(null);
    }

    public void dataDB() {

        Cursor cursor = getContext().getContentResolver().query(FavouritesTable.CONTENT_URI, null, null, null, null);
        List<FavouriteANDataBaseHelper> testRows = FavouritesTable.getRows(cursor, true);
        for (FavouriteANDataBaseHelper element : testRows) {

            mName.add(element.title);
            mDescription.add(element.description);
            mId.add(element.author);
            mNewsurl.add(element.url);
            mImage.add(element.image);

        }
    }
}
