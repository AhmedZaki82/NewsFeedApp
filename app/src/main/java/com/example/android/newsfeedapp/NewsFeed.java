package com.example.android.newsfeedapp;

import java.util.Date;

/**
 * Created by Tsultrim on 6/10/18.
 */

public class NewsFeed {

    private String section;
    private Date date;
    private String subject;
    private String url;

    public NewsFeed(String section, Date date, String subject, String url) {
        this.section = section;
        this.date = date;
        this.subject = subject;
        this.url = url;
    }

    public String getSection() {
        return section;
    }

    public Date getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }

    public String getUrl() {
        return url;
    }
}



