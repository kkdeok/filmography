package com.doubleknd26.filmography.domain;

import com.doubleknd26.filmography.proto.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.List;

@Getter
@SolrDocument(collection = "filmography")
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Movie {

    @Id
    @Indexed(value = "id", type = "string")
    private String id;

    @Field(value = "title")
    private String title;

    @Field(value = "ageLimit")
    private String ageLimit;

    @Field(value = "avgGrade")
    private Double avgGrade;

    @Field(value = "imagePath")
    private List<String> imagePaths = new ArrayList<>();

    @Field(value = "reviews")
    private List<Review> reviews = new ArrayList<>();

}
