package com.example.vladkolomysov.capstone.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.vladkolomysov.capstone.activity.DetailsAggregNewsActivity;
import com.example.vladkolomysov.capstone.R;

import java.util.ArrayList;

/**
 * Created by vladkolomysov
 */

public class MainAggregNewsAdapter extends RecyclerView.Adapter<MainAggregNewsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<String> id, name, description, newsurl, image;
    private boolean mTwoPane;
    private FragmentManager fm;

    // view holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView mImageView;
        TextView mNewsTitle;

        MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.main_card_view);
            mNewsTitle = (TextView) v.findViewById(R.id.main_news_title);
            mImageView = (ImageView) v.findViewById(R.id.main_grid_image);
        }
    }


    public MainAggregNewsAdapter(Context c, ArrayList<String> id, ArrayList<String> name,
                                 ArrayList<String> description, ArrayList<String> newsurl,
                                 ArrayList<String> image, boolean mTwoPane, FragmentManager fm) {
        mContext = c;
        this.id = id;
        this.name = name;
        this.description = description;
        this.newsurl = newsurl;
        this.image = image;
        this.mTwoPane = mTwoPane;
        this.fm = fm;
    }


    @Override
    public MainAggregNewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        MyViewHolder holder;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news, parent, false);
        holder = new MyViewHolder(v);
        holder.mCardView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
           Glide.clear(holder.mImageView);
            Glide.with(holder.mImageView.getContext())
                    .load(image.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                            Palette palette = Palette.generate(bitmap);
                            int defaultColor = 0xFF333444;
                            int color = palette.getMutedColor(defaultColor);
                            holder.mNewsTitle.setBackgroundColor(color);
                            return false;
                        }
                    })
                    .into(holder.mImageView);

            com.example.vladkolomysov.capstone.utils.Logger.log("url"," url = "+newsurl.get(position));
            com.example.vladkolomysov.capstone.utils.Logger.log("url"," image = "+image.get(position));

            /*Picasso.with(holder.mImageView.getContext())
                    .load(newsurl.get(position))
                    .resize(50, 50)
                    .centerCrop()
                    .into(holder.mImageView);*/

            holder.mNewsTitle.setText(name.get(position));
            // listener for grid card to start intent
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle extras = new Bundle();
                    extras.putString("SOURCE_NAME", id.get(holder.getAdapterPosition()));
                    Intent intent = new Intent(mContext, DetailsAggregNewsActivity.class);
                    intent.putExtra("BUNDLE", extras);
                    mContext.startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}
