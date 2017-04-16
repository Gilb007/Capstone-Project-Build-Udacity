package com.example.vladkolomysov.capstone.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vladkolomysov.capstone.adapter.MainAggregNewsAdapter;
import com.example.vladkolomysov.capstone.R;
import com.example.vladkolomysov.capstone.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vladkolomysov
 */

// main screen fragment
public class MainAggregNewsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private MainAggregNewsAdapter newsAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    public boolean mTwoPane;
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> description = new ArrayList<String>();
    ArrayList<String> newsurl = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();

    public MainAggregNewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTwoPane = getResources().getBoolean(R.bool.isTablet);
        FragmentManager fm = getActivity().getSupportFragmentManager();

        View rootView = inflater.inflate(R.layout.fragment_aggreg_news, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.newsrecyclerview);
        mRecyclerView.setHasFixedSize(true);

        mEmptyView = (TextView) rootView.findViewById(R.id.main_empty_view);

        if (!mTwoPane) {

            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            } else {
                mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            }
        } else {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            } else {
                mLayoutManager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
            }
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        newsAdapter = new MainAggregNewsAdapter(getActivity(), id, name, description, newsurl, image, mTwoPane, fm);
        mRecyclerView.setAdapter(newsAdapter);

        // load data and put in gridview
        loadData();

        return rootView;

    }


    public void loadData() {
        try {
            final String BASE_URL = getResources().getString(R.string.base_url_news);

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    BASE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String syncresponse = object.getString("sources");
                                JSONArray a1obj = new JSONArray(syncresponse);
                                for (int j = 0; j < a1obj.length(); j++) {
                                    JSONObject obj = a1obj.getJSONObject(j);
                                    id.add(obj.getString("id"));
                                    name.add(obj.getString("name"));
                                    description.add(obj.getString("description"));
                                    newsurl.add(obj.getString("url"));
                                    String s = obj.getString("urlsToLogos");
                                    JSONObject obj2 = new JSONObject(s);
                                    image.add(obj2.getString("large"));
                                    Logger.log("url","urlsToLogos = "+obj2+", "+obj2.getString("large"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            newsAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
