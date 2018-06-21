package com.example.android.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?";

    // Constant for the API search Key
    private static final String API_KEY = "api-key";

    // Constant value for the API Key
    private static final String KEY = "f1dfc1ea-9071-49cc-b586-005ed71ac92c";

    // Constant value for ordering by date
    private static final String ORDER = "order-by";
    private static final String DATE = "newest";

    // Constant value for No's of articles
    private static final String PAGE = "page-size";
    private static final String PAGES = "20";

    // Constant value for section
    private static final String SECTION = "section";

    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;

    public static final String LOG_TAG = MainActivity.class.getName();

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String searchCategory = sharedPreferences.getString(
                getString(R.string.pick_category1),
                getString(R.string.all));

        //extract news category name from list menu
        String[] categorySep = searchCategory.split(" ");
        String categoryCap = categorySep[0];
        String category = categoryCap.toLowerCase();

        // Create an URI and an URI Builder
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //for all news category tag is not aplly
        if (!category.equals("all")) {
            uriBuilder.appendQueryParameter(SECTION, category);
        }

        // for US new change tag
        if (category.equals(getString(R.string.tagUS))) {
            category = category + getString(R.string.tagForUsUk);
            uriBuilder.appendQueryParameter(SECTION, category);
        }
        // for UK new change tag
        if (category.equals(getString(R.string.tagUK))) {
            category = category + getString(R.string.tagForUsUk);
            uriBuilder.appendQueryParameter(SECTION, category);
        }

        uriBuilder.appendQueryParameter(ORDER, DATE);
        uriBuilder.appendQueryParameter(PAGE, PAGES);
        uriBuilder.appendQueryParameter(API_KEY, KEY);

        return new NewsLoader(this, uriBuilder.toString());
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

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
