package com.doubleknd26.filmography.repository;

import com.doubleknd26.filmography.domain.Movie;
import org.springframework.data.solr.repository.SolrRepository;

public interface MovieRepository extends SolrRepository<Movie, String>, MovieRepositoryCustom {
    Movie findById(String id);
    Movie findByTitle(String title);
}
