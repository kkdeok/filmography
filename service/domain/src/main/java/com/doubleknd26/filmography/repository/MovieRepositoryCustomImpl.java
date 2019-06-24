package com.doubleknd26.filmography.repository;

import com.doubleknd26.filmography.domain.Field;
import com.doubleknd26.filmography.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;

@RequiredArgsConstructor
public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    private static final String COLLECTION = "filmography";

    private final SolrOperations solrOperations;

    @Override
    public Page<Movie> findMovies(Pageable pageable) {

        FacetQuery query = new SimpleFacetQuery(new Criteria(Criteria.WILDCARD).expression(Criteria.WILDCARD))
                .addProjectionOnField(Field.ID.getField())
                .addProjectionOnField(Field.TITLE.getField())
                .addProjectionOnField(Field.AGE_LIMIT.getField())
                .addProjectionOnField(Field.AVG_GRADE.getField())
                .addProjectionOnField(Field.IMAGE_PATHS.getField())
                .setPageRequest(pageable);

        return solrOperations.queryForPage(COLLECTION, query, Movie.class);
    }
}
