package com.example.android.newsfeedapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Tsultrim on 6/10/18.
 */

public class NewsAdapter extends ArrayAdapter<NewsFeed> {


    public NewsAdapter(@NonNull Context context, @NonNull List<NewsFeed> newsFeeds) {
        super(context, 0, newsFeeds);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;
        NewsFeed currentNews = getItem(position);

        if (listView == null) {

            listView = LayoutInflater.from(getContext()).inflate(R.layout.news_sample,parent,
                    false);
        }

        TextView sectionV = (TextView) listView.findViewById(R.id.section);
        TextView subjectV = (TextView) listView.findViewById(R.id.subject);

        String section = currentNews.getSection();
        String subject = currentNews.getSubject();


        TextView dateView = null;
        TextView timeView = null;
        if (currentNews.getDate() != null) {
            dateView = listView.findViewById(R.id.date);
            String formattedDate = dateFormat(currentNews.getDate()).concat(",");
            dateView.setText(formattedDate);

            timeView = listView.findViewById(R.id.time);
            String formattedTime = timeFormat(currentNews.getDate());
            timeView.setText(formattedTime);

            dateView.setVisibility(View.VISIBLE);
            timeView.setVisibility(View.VISIBLE);
        } else {
            dateView.setVisibility(View.GONE);
            timeView.setVisibility(View.GONE);
        }

        sectionV.setText(section);
        subjectV.setText(subject);

        return listView;
    }

    private String dateFormat(Date date) {

        SimpleDateFormat formatDate = new SimpleDateFormat("LLL dd, yyyy");
        return formatDate.format(date);
    }

    private String timeFormat(Date date) {

        SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
        return formatTime.format(date);
    }
}
