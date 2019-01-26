package com.doubleknd26.filmography.indexer.model;

import com.doubleknd26.filmography.indexer.common.Source;

/**
 * Created by Kideok Kim on 2019-01-26.
 */
public class Review {
    private String title;
    private String content;
    private int grade;
    private long timestamp;
    private Source source;

    public Review(String title, String content, int grade, long timestamp, Source source) {
        this.title = title;
        this.content = content;
        this.grade = grade;
        this.timestamp = timestamp;
        this.source = source;
    }

    @Override
    public String toString() {
        return "Review{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", grade=" + grade +
                ", timestamp=" + timestamp +
                ", source=" + source +
                '}';
    }
}
