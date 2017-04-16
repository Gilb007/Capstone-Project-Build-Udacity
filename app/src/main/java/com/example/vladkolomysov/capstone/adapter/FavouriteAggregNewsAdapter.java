package com.example.vladkolomysov.capstone.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.example.vladkolomysov.capstone.R;
import com.example.vladkolomysov.capstone.activity.BrowserIntentActivity;
import com.example.vladkolomysov.capstone.data.FavouriteANDataBaseHelper;
import com.example.vladkolomysov.capstone.data.FavouriteANContract;
import com.example.vladkolomysov.capstone.data.FavouritesTable;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladkolomysov
 */

public class FavouriteAggregNewsAdapter extends RecyclerView.Adapter<FavouriteAggregNewsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<String> mId, mName, mDescription, mNewsurl, mImage;
    private MaterialFavoriteButton mButtonBookmark;
    Cursor mDataCursor;

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView imageView;
        TextView newstitle;

        MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.favourite_card_view);
            newstitle = (TextView) v.findViewById(R.id.favourite_news_title);
            imageView = (ImageView) v.findViewById(R.id.favourite_grid_image);
            mButtonBookmark = (MaterialFavoriteButton) v.findViewById(R.id.favourite_bookmark);
        }
    }


    public FavouriteAggregNewsAdapter(Context c, ArrayList<String> id, ArrayList<String> name,
                                      ArrayList<String> description, ArrayList<String> newsurl,
                                      ArrayList<String> image, Cursor cursor) {
        mContext = c;
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mNewsurl = newsurl;
        this.mImage = image;
        this.mDataCursor = cursor;

    }

    public Cursor swapCursor(Cursor cursor) {
        if (mDataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mDataCursor;
        this.mDataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }


    @Override
    public FavouriteAggregNewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        FavouriteAggregNewsAdapter.MyViewHolder holder;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_aggreg_news_grid, parent, false);
        holder = new FavouriteAggregNewsAdapter.MyViewHolder(v);
        holder.mCardView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FavouriteAggregNewsAdapter.MyViewHolder holder, final int position) {
        try {
            mDataCursor.moveToPosition(position);
            Glide.clear(holder.imageView);
            Glide.with(holder.imageView.getContext())
                    .load(mDataCursor.getString(mDataCursor.getColumnIndex(FavouritesTable.FIELD_URLTOIMAGE)))
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
                            int defaultColor = 0xFF333333;
                            int color = palette.getMutedColor(defaultColor);
                            holder.newstitle.setBackgroundColor(color);
                            return false;
                        }
                    })
                    .into(holder.imageView);

            holder.newstitle.setText(mDataCursor.getString(mDataCursor.getColumnIndex(FavouritesTable.FIELD_TITLE)));
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, BrowserIntentActivity.class);
                    intent.putExtra("URL", mNewsurl.get(position));
                    intent.putExtra("SOURCE", mName.get(position));
                    mContext.startActivity(intent);
                }
            });

            final String newsUrl = mDataCursor.getString(mDataCursor.getColumnIndex(FavouritesTable.FIELD_URL));

            ArrayList<String> check = queryFavouritesAggregNews();
            if (check.contains(newsUrl)) {
                mButtonBookmark.setFavorite(true);
            } else {
                mButtonBookmark.setFavorite(false);
            }

            mButtonBookmark.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                    mContext.getContentResolver().delete(FavouritesTable.CONTENT_URI, FavouriteANContract.COLUMN_AN_URL + " = ?", new String[]{"" + newsUrl});
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> queryFavouritesAggregNews() {

        Cursor c = mContext.getContentResolver().query(FavouritesTable.CONTENT_URI, null, null, null, null);
        List<FavouriteANDataBaseHelper> list = FavouritesTable.getRows(c, true);
        ArrayList<String> idList = new ArrayList<>();
        for (FavouriteANDataBaseHelper element : list) {
            idList.add(element.url);
        }
        return idList;
    }

    @Override
    public int getItemCount() {
        return (mDataCursor == null) ? 0 : mDataCursor.getCount();
    }
}

