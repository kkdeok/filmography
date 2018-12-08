package com.doubleknd26.filmgoer;


import com.doubleknd26.filmgoer.extract.Extractor;
import com.doubleknd26.filmgoer.extract.FilmNameExtractor;
import com.doubleknd26.filmgoer.extract.NaverFilmExtractor;
import com.doubleknd26.filmgoer.model.FilmInfo;
import com.doubleknd26.filmgoer.model.Review;
import com.doubleknd26.filmgoer.transform.FilmGoerTransformer;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.SetUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.reflect.ClassTag;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by doubleknd26 on 2018-12-08.
 */
public class FilmGoerDriver {
    private SparkSession ss;
    private JavaSparkContext jsc;
    private static final ImmutableList<Extractor> list = ImmutableList.<Extractor>builder()
            .add(new NaverFilmExtractor())
            .build();

    public FilmGoerDriver() {
        this.ss = SparkSession.builder()
                .master("local[*]")
                .appName("Word Count")
                .getOrCreate();
        this.jsc = new JavaSparkContext(this.ss.sparkContext());
    }

    public static void main(String[] args) throws Exception {
        FilmGoerDriver driver = new FilmGoerDriver();
        driver.run();
    }

    public void run() throws Exception {
        Broadcast<Set<String>> filmNames = getFilmNames();
        JavaRDD<Review> reviewRdd = getReviewRdd();

        FilmGoerTransformer transformer = new FilmGoerTransformer(filmNames, reviewRdd);
        JavaRDD<FilmInfo> transformed = transformer.transform();
        transformed.saveAsTextFile("/pang/wow.txt");
    }

    private Broadcast<Set<String>> getFilmNames() throws Exception {
        FilmNameExtractor filmNameExtractor = new FilmNameExtractor();
        ClassTag<Set<String>> classTag = scala
                .reflect
                .ClassTag$
                .MODULE$
                .apply(Set.class);
        return ss.sparkContext().broadcast(filmNameExtractor.extract(), classTag);
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
