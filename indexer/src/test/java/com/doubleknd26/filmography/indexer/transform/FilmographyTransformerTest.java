package com.doubleknd26.filmography.indexer.transform;

import com.doubleknd26.filmography.indexer.common.KryoRegistratorWrapper;
import com.doubleknd26.filmography.proto.FilmInfo;
import com.doubleknd26.filmography.proto.Review;
import com.doubleknd26.filmography.proto.Source;

import org.apache.solr.common.SolrDocument;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Kideok Kim on 2018-12-09.
 */
public class FilmographyTransformerTest {
    private SparkSession ss;
    private JavaSparkContext jsc;

    @Before
    public void setUp() throws Exception {
        this.ss = SparkSession.builder()
                .master("local[*]")
                .appName("Filmography")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.kryo.registrator", KryoRegistratorWrapper.class.getCanonicalName())
                .getOrCreate();
        this.jsc = new JavaSparkContext(ss.sparkContext());
    }

    @Test
    public void testTransform() {
        JavaRDD<Review> reviewRdd = jsc.parallelize(Arrays.asList(
                createReview("test1", 1),
                createReview("test1", 2),
                createReview("test2", 3),
                createReview("test3", 4)));
        JavaRDD<FilmInfo> filmInfoRdd =  jsc.parallelize(Arrays.asList(
                createFilmInfo("test1"),
                createFilmInfo("test2")));

        FilmographyTransformer transformer = new FilmographyTransformer();
        JavaRDD<SolrDocument> response = transformer.transform(filmInfoRdd, reviewRdd);

        assertEquals(2, response.count());
    }

    private FilmInfo createFilmInfo(String title) {
        return FilmInfo.newBuilder()
                .setTitle(title)
                .build();
    }

    private Review createReview(String title, int grade) {
        return Review.newBuilder()
                .setTitle(title)
                .setGrade(grade)
                .setSource(Source.TEST)
                .build();
    }
}