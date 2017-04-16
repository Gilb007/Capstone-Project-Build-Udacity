package com.example.vladkolomysov.capstone.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vladkolomysov.capstone.R;
import com.example.vladkolomysov.capstone.activity.BrowserIntentActivity;
import com.example.vladkolomysov.capstone.data.FavouriteANDataBaseHelper;
import com.example.vladkolomysov.capstone.data.FavouriteANContract;
import com.example.vladkolomysov.capstone.data.FavouritesTable;
import com.example.vladkolomysov.capstone.widget.AggregNewsWidget;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladkolomysov
 */

public class DetailsAggregNewsFragment extends android.support.v4.app.Fragment{

    TextView mTextView, mAuthorText, mAuthorTitle;
    ImageView mImageView, mButtonBrowse, mButtonShare;
    CardView mBottomView, mCardView;
    MaterialFavoriteButton mButtonBookmark;
    Animation mSlideRightAnimation, mSlideLeftAnimation;
    private AdView mAdMobView;
    private FirebaseAnalytics mFirebaseAnalytics;


    public DetailsAggregNewsFragment() {

    }

    public static DetailsAggregNewsFragment newInstance(String source, String name, String image, String description, String author, String newsurl) {
        DetailsAggregNewsFragment fragment = new DetailsAggregNewsFragment();
        Bundle args = new Bundle();
        args.putString("source", source);
        args.putString("mName", name);
        args.putString("image", image);
        args.putString("mDescription", description);
        args.putString("author", author);
        args.putString("mNewsurl", newsurl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add mId for ads block
        MobileAds.initialize(getContext(), "ca-app-pub-5040891831252267/7395636234");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mRootView = inflater.inflate(R.layout.fragment_details_aggreg_news, container, false);

        sendUpdateIntentAN(getContext());

        // get extras
        final String source = getArguments().getString("source");
        final String title = getArguments().getString("mName");
        final String image = getArguments().getString("image");
        final String description = getArguments().getString("mDescription");
        final String author = getArguments().getString("author");
        final String newsurl = getArguments().getString("mNewsurl");

        // set bundle
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, source);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, newsurl);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        // adMob
        mAdMobView = (AdView) mRootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        // load ad mob
        mAdMobView.loadAd(adRequest);

        // for sliding
        mSlideRightAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_right);
        mSlideLeftAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_left);

        mCardView = (CardView) mRootView.findViewById(R.id.card_detail_news);
        mBottomView = (CardView) mRootView.findViewById(R.id.buttom_view);
        mAuthorTitle = (TextView) mRootView.findViewById(R.id.article_title_news);

        mAuthorTitle.setText(title);

        mAuthorText = (TextView) mRootView.findViewById(R.id.article_author_news);
        try {
            String s = "By " + author.substring(0, 1).toUpperCase() + author.substring(1).toLowerCase();
            mAuthorText.setText(s);
        } catch (Exception e) {
            System.out.println(e);
        }

        mTextView = (TextView) mRootView.findViewById(R.id.article_body_news);
        mTextView.setText(description);

        mButtonBrowse = (ImageView) mRootView.findViewById(R.id.button_browse);
        mButtonShare = (ImageView) mRootView.findViewById(R.id.button_share);

        // listener for browse
        mButtonBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), BrowserIntentActivity.class);
                intent.putExtra("URL", newsurl);
                intent.putExtra("SOURCE", source);
                startActivity(intent);

            }
        });

        // listiner for share
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, newsurl);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });

        mButtonBookmark = (MaterialFavoriteButton) mRootView.findViewById(R.id.button_bookmark);

        // checking if news already added
        ArrayList<String> check = queryFavouritesAggregNews();
        if (check.contains(newsurl)) {
            //set favourite
            mButtonBookmark.setFavorite(true);
        } else {
            mButtonBookmark.setFavorite(false);
        }


        // listener for bookmark
        mButtonBookmark.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite) {
                    // add new instanse for db
                    FavouriteANDataBaseHelper instanceAggregNews = new FavouriteANDataBaseHelper();
                    instanceAggregNews.title = title;
                    instanceAggregNews.description = description;
                    instanceAggregNews.author = author;
                    instanceAggregNews.image = image;
                    instanceAggregNews.url = newsurl;
                    instanceAggregNews.date = "";

                    // update db
                    try {
                        getActivity().getContentResolver().insert(FavouritesTable.CONTENT_URI, FavouritesTable.getContentValues(instanceAggregNews, true));
                        sendUpdateIntentAN(getContext());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    getActivity().getContentResolver().delete(FavouritesTable.CONTENT_URI, FavouriteANContract.COLUMN_AN_URL + " = ?", new String[]{"" + newsurl});
                    sendUpdateIntentAN(getContext());
                }
            }
        });

        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mImageView = (ImageView) mRootView.findViewById(R.id.photo_aggreg_news);

        // load pics
        Picasso.with(getContext())
                .load(image)
                .into(mImageView);

        return mRootView;
    }

    public static void sendUpdateIntentAN(Context context)
    {
        Intent i = new Intent(context, AggregNewsWidget.class);
        i.setAction(AggregNewsWidget.DATABASE_CHANGED);
        context.sendBroadcast(i);
    }

    private ArrayList<String> queryFavouritesAggregNews() {

        Cursor c = getActivity().getContentResolver().query(FavouritesTable.CONTENT_URI, null, null, null, null);
        List<FavouriteANDataBaseHelper> list = FavouritesTable.getRows(c, true);
        ArrayList<String> idList = new ArrayList<>();
        for (FavouriteANDataBaseHelper element : list) {
            idList.add(element.url);
        }
        return idList;
    }

    @Override
    public void onPause() {
        if (mAdMobView != null) {
            mAdMobView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdMobView != null) {
            mAdMobView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdMobView != null) {
            mAdMobView.destroy();
        }
        super.onDestroy();
    }
}
