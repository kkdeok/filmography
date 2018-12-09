package com.doubleknd26.moving.indexer;


import com.doubleknd26.moving.indexer.extract.Extractor;
import com.doubleknd26.moving.indexer.extract.MovieNameExtractor;
import com.doubleknd26.moving.indexer.extract.NaverMovieExtractor;
import com.doubleknd26.moving.indexer.transform.Transformer;
import com.doubleknd26.moving.proto.MovieInfo;
import com.doubleknd26.moving.proto.Review;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections.IteratorUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import scala.reflect.ClassTag;

import java.util.Set;

/**
 * Created by doubleknd26 on 2018-12-08.
 */
public class Indexer {
    private SparkSession ss;
    private JavaSparkContext jsc;
    private static final ImmutableList<Extractor> list = ImmutableList.<Extractor>builder()
            .add(new NaverMovieExtractor())
            .build();

    public Indexer() {
        this.ss = SparkSession.builder()
                .master("local[*]")
                .appName("Word Count")
                .getOrCreate();
        this.jsc = new JavaSparkContext(this.ss.sparkContext());
    }

    public static void main(String[] args) throws Exception {
        Indexer driver = new Indexer();
        driver.run();
    }

    public void run() throws Exception {
        Broadcast<Set<String>> filmNames = getFilmNames();
        JavaRDD<Review> reviewRdd = getReviewRdd();

        Transformer transformer = new Transformer(filmNames, reviewRdd);
//        JavaRDD<MovieInfo> transformed = transformer.transform();
//        transformed.saveAsTextFile("/pang/wow.txt");
    }

    private Broadcast<Set<String>> getFilmNames() throws Exception {
        MovieNameExtractor movieNameExtractor = new MovieNameExtractor();
        ClassTag<Set<String>> classTag = scala
                .reflect
                .ClassTag$
                .MODULE$
                .apply(Set.class);
        return ss.sparkContext().broadcast(movieNameExtractor.extract(), classTag);
    }

    private JavaRDD<Review> getReviewRdd() throws Exception {
        JavaRDD<Review> reviewRdd = null;
        for (Extractor extractor: list) {
            Set reviews = extractor.extract();
            if (reviewRdd == null) {
                reviewRdd = jsc.parallelize(IteratorUtils.toList(reviews.iterator()));
            } else {
                reviewRdd.union(jsc.parallelize(IteratorUtils.toList(reviews.iterator())));
            }
        }
        return reviewRdd;
    }
}
