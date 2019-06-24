package com.doubleknd26.filmography.web.endpoint;

import com.doubleknd26.filmography.web.service.MovieService;
import com.doubleknd26.filmography.web.service.dto.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public List<MovieResponse> getMovies(@PageableDefault Pageable pageable) {
        return movieService.getMovies(pageable);
    }

    @GetMapping("/{id}")
    public MovieResponse getMovie(@PathVariable String id) {
        return movieService.getMovie(id);
    }
}
