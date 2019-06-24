package com.doubleknd26.filmography.web.service;

import com.doubleknd26.filmography.domain.Movie;
import com.doubleknd26.filmography.repository.MovieRepository;
import com.doubleknd26.filmography.web.service.dto.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieResponse> getMovies(Pageable pageable) {
        return movieRepository.findMovies(pageable)
                .stream()
                .filter(Objects::nonNull)
                .map(MovieResponse::of)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public MovieResponse getMovie(String id) {
        Movie movie = movieRepository.findById(id);

        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .ageLimit(movie.getAgeLimit())
                .avgGrade(movie.getAvgGrade())
                .reviews(toReviews(movie))
                .build();
    }

    private List<MovieResponse.Review> toReviews(Movie movie) {
        return movie.getReviews().stream()
                .map(MovieResponse.Review::of)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }
}
