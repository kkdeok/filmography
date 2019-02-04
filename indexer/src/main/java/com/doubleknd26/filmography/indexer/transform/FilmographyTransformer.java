package com.doubleknd26.filmography.indexer.transform;

import com.doubleknd26.filmography.proto.FilmInfo;
import com.doubleknd26.filmography.proto.Review;
import org.apache.solr.common.SolrDocument;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Collections;

/**
 * Created by Kideok Kim on 2018-12-08.
 */
public class FilmographyTransformer implements Serializable {

    public JavaRDD<SolrDocument> transform(JavaRDD<FilmInfo> filmInfoRdd, JavaRDD<Review> reviewRdd) {
        JavaPairRDD<String, Iterable<Review>> transformedReview = transformReview(reviewRdd);
        JavaPairRDD<String, FilmInfo> transformedFilmInfo = transformFilmInfo(filmInfoRdd);
        return transformedFilmInfo
                .rightOuterJoin(transformedReview)
                .flatMapValues(new Function<Tuple2<Optional<FilmInfo>, Iterable<Review>>, Iterable<FilmInfo>>() {
                    @Override
                    public Iterable<FilmInfo> call(Tuple2<Optional<FilmInfo>, Iterable<Review>> tuple) throws Exception {
                        Optional<FilmInfo> filmInfo = tuple._1();
                        if (filmInfo.isPresent()) {
                            FilmInfo.Builder builder = filmInfo.get().toBuilder();
                            int reviewCount = 0;
                            float gradeSum = 0f;
                            for (Review review : tuple._2()) {
                                builder.addReviews(review);
                                reviewCount++;
                                gradeSum += review.getGrade();
                            }
                            builder.setAvgGrade(gradeSum / reviewCount);
                            return Collections.singletonList(builder.build());
                        } else {
                            return Collections.emptyList();
                        }
                    }
                }).mapValues(new Function<FilmInfo, SolrDocument>() {
                    @Override
                    public SolrDocument call(FilmInfo v1) throws Exception {
                        return null;
                    }
                }).values();
    }

    private JavaPairRDD<String, Iterable<Review>> transformReview(JavaRDD<Review> reviewRdd) {
        return reviewRdd.groupBy(new Function<Review, String>() {
            @Override
            public String call(Review review) throws Exception {
                return review.getTitle();
            }
        });
    }

    private JavaPairRDD<String, FilmInfo> transformFilmInfo(JavaRDD<FilmInfo> filmInfoRdd) {
        return filmInfoRdd.mapToPair(new PairFunction<FilmInfo, String, FilmInfo>() {
            @Override
            public Tuple2<String, FilmInfo> call(FilmInfo filmInfo) throws Exception {
                return new Tuple2<>(filmInfo.getTitle(), filmInfo);
            }
        });
    }
}
