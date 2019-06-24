package com.doubleknd26.filmography.web.endpoint;

import com.doubleknd26.filmography.web.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {

    private final MovieService movieService;

}
