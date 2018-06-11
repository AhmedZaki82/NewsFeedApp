package com.example.android.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsFeed>>{

    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?from-date=2018-06-10&to-date=2018-06-11&use-date=published&order-by=oldest&api-key=test";

    private TextView mEmptyStateTextView;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar  = (ProgressBar) findViewById(R.id.progressBar);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            Log.i(LOG_TAG,"Test: Calling initLoader ......");
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            Toast.makeText(this, "You are Connected", Toast.LENGTH_SHORT).show();

        } else {

            progressBar.setVisibility(View.GONE);

            mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);

            mEmptyStateTextView.setText(R.string.no_internet_connection);

            Toast.makeText(this, "No Connection here", Toast.LENGTH_SHORT).show();
        }

        ListView newsListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        newsListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<NewsFeed>());

        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsFeed currentEarthquake = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(websiteIntent);
            }
        });

    }

    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int i, Bundle bundle) {

        Log.i(LOG_TAG,"Test: onCreateLoader is Called.....");
        return new NewsLoader(this,GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsFeeds) {

        progressBar.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setText(R.string.no_news);
        Log.i(LOG_TAG,"Test: onLoadFinished is called.....");
        mAdapter.clear();

        if (newsFeeds != null && !newsFeeds.isEmpty()) {
            mAdapter.addAll(newsFeeds);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {

        Log.i(LOG_TAG,"Test: onLoaderReset is called.....");
        mAdapter.clear();
    }
}
