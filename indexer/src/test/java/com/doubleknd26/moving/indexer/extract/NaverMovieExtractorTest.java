package com.doubleknd26.moving.indexer.extract;

import com.doubleknd26.moving.proto.Review;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by doubleknd26 on 2018-12-05.
 */
public class NaverMovieExtractorTest {
    private NaverMovieExtractor extractor;

    @Before
    public void setUp() throws Exception {
        this.extractor = new NaverMovieExtractor(1);
    }

    @Test
    public void testExtract() throws Exception {
        Set<Review> response = extractor.extract();
        assertThat(response.isEmpty(), is(false));
    }
}