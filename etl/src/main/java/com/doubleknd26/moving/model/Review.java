package com.doubleknd26.moving.model;

import com.doubleknd26.moving.common.Source;

import java.io.Serializable;

/**
 * Created by doubleknd26 on 2018-12-05.
 */
public class Review implements Serializable {
    private Source source;
    private String url;
    private String title;
    private int grade;
    private String comment;
    private long timestamp;
    private String id;

    public Review(Source source, String title, int grade, String comment, long timestamp, String id) {
        this.source = source;
        this.title = title;
        this.grade = grade;
        this.comment = comment;
        this.timestamp = timestamp;
        this.id = id;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Review{" +
                "source=" + source +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", grade=" + grade +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                ", id='" + id + '\'' +
                '}';
    }
}
