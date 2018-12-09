package com.doubleknd26.moving.indexer;


import com.doubleknd26.moving.indexer.crawl.Crawler;
import com.doubleknd26.moving.indexer.crawl.MovieNameCrawler;
import com.doubleknd26.moving.indexer.crawl.NaverMovieCrawler;
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
    private static final ImmutableList<Crawler> list = ImmutableList.<Crawler>builder()
            .add(new NaverMovieCrawler())
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
        Broadcast<Set<String>> titles = getRunningTitles();
        JavaRDD<Review> reviewRdd = getReviewRdd();

//        JavaRDD<MovieInfo> transformed = transformer.transform();
//        transformed.saveAsTextFile("/pang/wow.txt");
    }

    private Broadcast<Set<String>> getRunningTitles() throws Exception {
        MovieNameCrawler movieNameCrawler = new MovieNameCrawler();
        ClassTag<Set<String>> classTag = scala
                .reflect
                .ClassTag$
                .MODULE$
                .apply(Set.class);
        return ss.sparkContext().broadcast(movieNameCrawler.crawl(), classTag);
    }

    private JavaRDD<Review> getReviewRdd() throws Exception {
        JavaRDD<Review> reviewRdd = null;
        for (Crawler crawler : list) {
            Set reviews = crawler.crawl();
            if (reviewRdd == null) {
                reviewRdd = jsc.parallelize(IteratorUtils.toList(reviews.iterator()));
            } else {
                reviewRdd.union(jsc.parallelize(IteratorUtils.toList(reviews.iterator())));
            }
        }
        return reviewRdd;
    }
}
