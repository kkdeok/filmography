package com.doubleknd26.filmography.repository;

import com.doubleknd26.filmography.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieRepositoryCustom {
    Page<Movie> findMovies(Pageable pageable);
}
