package com.doubleknd26.filmgoer.model;

import com.doubleknd26.filmgoer.common.Source;

import java.util.Date;

/**
 * Created by doubleknd26 on 2018-12-05.
 */
public class Review {
    private Source source;
    private String url;
    private String id;
    private Date date;
    private int grade;
    private String comment;

    public Review(Source source, String url, String id, Date date, int grade, String comment) {
        this.source = source;
        this.url = url;
        this.id = id;
        this.date = date;
        this.grade = grade;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Review{" +
                "source=" + source +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                ", date=" + date +
                ", grade=" + grade +
                ", comment='" + comment + '\'' +
                '}';
    }
}
