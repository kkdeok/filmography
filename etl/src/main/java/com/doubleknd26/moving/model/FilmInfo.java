package com.doubleknd26.moving.model;

import org.apache.commons.collections.ListUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by doubleknd26 on 2018-12-08.
 */
public class FilmInfo implements Serializable {
    private String title;
    private int grade;
    private List<Review> reviews;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGrade() {
        return grade / reviews.size();
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public FilmInfo() {
        this.title = null;
        this.grade = 0;
        this.reviews = null;
    }

    public void updateTitle(String title) {
        if (this.title == null) {
            this.title = title;
        }
    }

    public void addGrade(int grade) {
        this.grade += grade;
    }

    public void addReviews(List<Review> reviews) {
        if (this.reviews == null) {
            this.reviews = reviews;
        } else {
            this.reviews = ListUtils.union(this.reviews, reviews);
        }
    }

    @Override
    public String toString() {
        return "FilmInfo{" +
                "title='" + title + '\'' +
                ", grade=" + grade +
                ", reviews=" + reviews +
                '}';
    }
}
