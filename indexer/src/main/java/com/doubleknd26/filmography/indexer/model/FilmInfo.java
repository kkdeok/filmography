package com.doubleknd26.filmography.indexer.model;

import com.doubleknd26.filmography.indexer.common.Genre;

import java.util.List;

/**
 * Created by Kideok Kim on 2019-01-26.
 */
public class FilmInfo {
    private String title;
    private String director;
    private String ageLimit;
    private String imgSrc;
    private Genre genre;
    private long releaseTime;
    private int runningTime;
    private List<Review> reviews;

    public FilmInfo(String title, String director, String ageLimit, String imgSrc, Genre genre, long releaseTime, int runningTime, List<Review> reviews) {
        this.title = title;
        this.director = director;
        this.ageLimit = ageLimit;
        this.imgSrc = imgSrc;
        this.genre = genre;
        this.releaseTime = releaseTime;
        this.runningTime = runningTime;
        this.reviews = reviews;
    }
}
