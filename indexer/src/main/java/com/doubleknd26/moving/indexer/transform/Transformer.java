package com.doubleknd26.moving.indexer.transform;

import com.doubleknd26.moving.indexer.model.FilmInfo;
import com.doubleknd26.moving.indexer.model.Review;
import com.google.common.collect.Lists;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by doubleknd26 on 2018-12-08.
 */
public class Transformer implements Serializable {
    private Broadcast<Set<String>> filmNames;
    private JavaRDD<Review> reviewRdd;

    public Transformer(Broadcast<Set<String>> filmNames, JavaRDD<Review> reviewRdd) {
        this.filmNames = filmNames;
        this.reviewRdd = reviewRdd;
    }

    public JavaRDD<FilmInfo> transform() {
        return reviewRdd
                .filter(review -> filmNames.getValue().contains(review.getTitle()))
                .mapToPair(review -> new Tuple2<>(review.getTitle(), review))
                .combineByKey(
                        (Function<Review, FilmInfo>) review -> {
                            FilmInfo filmInfo = new FilmInfo();
                            filmInfo.updateTitle(review.getTitle());
                            filmInfo.addGrade(review.getGrade());
                            filmInfo.addReviews(Lists.newArrayList(review));
                            return filmInfo;
                        },
                        (Function2<FilmInfo, Review, FilmInfo>) (filmInfo, review) -> {
                            filmInfo.updateTitle(review.getTitle());
                            filmInfo.addGrade(review.getGrade());
                            filmInfo.addReviews(Lists.newArrayList(review));
                            return filmInfo;
                        },
                        (Function2<FilmInfo, FilmInfo, FilmInfo>) (filmInfo1, filmInfo2) -> {
                            filmInfo1.updateTitle(filmInfo2.getTitle());
                            filmInfo1.addGrade(filmInfo2.getGrade());
                            filmInfo1.addReviews(filmInfo2.getReviews());
                            return filmInfo1;
                        })
                .values();
    }
}
