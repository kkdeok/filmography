package com.doubleknd26.moving.indexer.transform;

import com.doubleknd26.moving.proto.MovieInfo;
import com.doubleknd26.moving.proto.Review;
import com.google.common.collect.Lists;
import com.google.protobuf.Descriptors;
import org.apache.commons.collections.ListUtils;
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
public class ReviewTransformer implements Serializable {
    private Broadcast<Set<String>> titles;
    private JavaRDD<Review> reviewRdd;

    public ReviewTransformer(Broadcast<Set<String>> titles, JavaRDD<Review> reviewRdd) {
        this.titles = titles;
        this.reviewRdd = reviewRdd;
    }

    public JavaRDD<MovieInfo> run() {
        return reviewRdd
                .filter(review -> titles.getValue().contains(review.getTitle()))
                .mapToPair(review -> new Tuple2<>(review.getTitle(), review))
                .combineByKey(
                        (Function<Review, MovieInfo>) review -> MovieInfo.newBuilder()
                                .setTitle(review.getTitle())
                                .setGrade(review.getGrade())
                                .setField(MovieInfo.getDescriptor().findFieldByName("reviews"), Lists.newArrayList(review))
                                .build(),
                        (Function2<MovieInfo, Review, MovieInfo>) (movieInfo, review) -> movieInfo.toBuilder()
                                .setGrade(movieInfo.getGrade() + review.getGrade())
                                .setField(MovieInfo.getDescriptor().findFieldByName("reviews"), ListUtils.union(movieInfo.getReviewsList(), Lists.newArrayList(review)))
                                .build(),
                        (Function2<MovieInfo, MovieInfo, MovieInfo>) (movieInfo1, movieInfo2) -> movieInfo1.toBuilder()
                                .setGrade(movieInfo1.getGrade() + movieInfo2.getGrade())
                                .setField(MovieInfo.getDescriptor().findFieldByName("reviews"), ListUtils.union(movieInfo1.getReviewsList(), movieInfo2.getReviewsList()))
                                .build())
                .values();
    }
}
