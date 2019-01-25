package com.doubleknd26.moving.indexer.transform;

import com.doubleknd26.moving.proto.MovieInfo;
import com.doubleknd26.moving.proto.Review;
import com.google.common.collect.Sets;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;
import scala.reflect.ClassTag;

import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Kideok Kim on 2018-12-09.
 */
public class ReviewTransformerTest {
    private SparkSession ss;
    private JavaSparkContext jsc;
    private ClassTag<Set<String>> classTag;

    @Before
    public void setUp() throws Exception {
        this.ss = SparkSession.builder()
                .master("local")
                .appName("test")
                .getOrCreate();
        this.jsc = new JavaSparkContext(ss.sparkContext());
        this.classTag = scala.reflect.ClassTag$.MODULE$.apply(Set.class);
    }

    @Test
    public void testRun() {
        Broadcast<Set<String>> titles = ss.sparkContext()
                .broadcast(Sets.newHashSet("test"), classTag);
        JavaRDD<Review> reviews = jsc.parallelize(Arrays.asList(
                createReview("test", 6),
                createReview("test", 5),
                createReview("test", 4)));

        ReviewTransformer transformer = new ReviewTransformer(titles, reviews);
        JavaRDD<MovieInfo> response = transformer.run();

        assertEquals(1, response.count());
    }

    @Test
    public void testRunForTitleFiltering() {
        Broadcast<Set<String>> titles = ss.sparkContext()
                .broadcast(Sets.newHashSet("test", "test#"), classTag);
        JavaRDD<Review> reviews = jsc.parallelize(Arrays.asList(
                createReview("test", 6),
                createReview("test", 5),
                createReview("test#", 4),
                createReview("test@", 3)));

        ReviewTransformer transformer = new ReviewTransformer(titles, reviews);
        JavaRDD<MovieInfo> response = transformer.run();

        assertEquals(2, response.count());
    }

    private Review createReview(String title, int grade) {
        return Review.newBuilder()
                .setTitle(title)
                .setGrade(grade)
                .build();
    }
}