package com.example.android.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.util.Log;

import java.util.List;

/**
 * Created by Tsultrim on 6/10/18.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsFeed>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    private String mUrl;

    public NewsLoader(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<NewsFeed> loadInBackground() {
        Log.i(LOG_TAG,"Test: loadInBackground() called...");

        if (mUrl == null) {
            return null;
        }

        List<NewsFeed> newsFeeds = QueryUtils.fetchNewsFeed(mUrl);
        return newsFeeds;
    }

    @Override
    protected void onStartLoading() {

        Log.i(LOG_TAG,"Test: onStartLoading called...");
        forceLoad();    }

}
