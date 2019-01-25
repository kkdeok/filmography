package com.doubleknd26.filmography.indexer.model;

import com.doubleknd26.filmography.indexer.common.Genre;

import java.util.List;

/**
 * Created by Kideok Kim on 2019-01-26.
 */
public class Film {
    private String title;
    private String director;
    private int grade;
    private Genre genre;
    private long releaseTime;
    private List<Review> reviews;
}
