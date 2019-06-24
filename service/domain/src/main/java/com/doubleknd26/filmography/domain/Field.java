package com.doubleknd26.filmography.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Field {

    ID(() -> "id"),
    TITLE(() -> "title"),
    AGE_LIMIT(() -> "ageLimit"),
    AVG_GRADE(() -> "avgGrade"),
    IMAGE_PATHS(() -> "imagePath"),
    REVIEWS(() -> "reviews")
    ;

    private org.springframework.data.solr.core.query.Field field;
}
