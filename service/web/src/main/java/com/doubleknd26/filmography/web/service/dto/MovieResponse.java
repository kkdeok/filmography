package com.doubleknd26.filmography.web.service.dto;

import com.doubleknd26.filmography.domain.Movie;
import com.doubleknd26.filmography.proto.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MovieResponse {

    private String id;
    private String title;
    private String ageLimit;
    private Double avgGrade;
    private String imagePath;

    private List<Review> reviews;

    public static MovieResponse of(Movie movie) {
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.id = movie.getId();
        movieResponse.title = movie.getTitle();

        movieResponse.ageLimit = movie.getAgeLimit();

        movieResponse.avgGrade = movie.getAvgGrade();

        movieResponse.imagePath = movie.getImagePaths()
                .stream()
                .findFirst()
                .orElse(null);

        return movieResponse;
    }

    @Builder
    public MovieResponse(String id, String title, String ageLimit, Double avgGrade, String imagePath, List<Review> reviews) {
        this.id = id;
        this.title = title;
        this.ageLimit = ageLimit;
        this.avgGrade = avgGrade;
        this.imagePath = imagePath;
        this.reviews = reviews;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Review {
        private double grade;
        private String comment;
        private String timestamp;
        private String source;

        public static Review of(com.doubleknd26.filmography.proto.Review originReview) {
            Review review = new Review();
            review.grade = originReview.getGrade();
            review.comment = originReview.getComment();
            review.timestamp = originReview.getTimestamp();
            review.source = originReview.getSource().name();
            return review;
        }
    }
}
