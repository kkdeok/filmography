package com.doubleknd26.filmgoer.extract;

import com.doubleknd26.filmgoer.model.Review;
import org.apache.commons.collections.IteratorUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by doubleknd26 on 2018-12-05.
 */
public class NaverFilmExtractorTest {
    private NaverFilmExtractor extractor;

    @Before
    public void setUp() throws Exception {
        this.extractor = new NaverFilmExtractor(1);
    }

    @Test
    public void testExtract() throws Exception {
        Set<Review> response = extractor.extract();
        assertThat(response.isEmpty(), is(false));
    }
}