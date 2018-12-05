package com.doubleknd26.filmgoer.extract;

import com.doubleknd26.filmgoer.model.Review;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by doubleknd26 on 2018-12-05.
 */
public class NaverFilmExtractorTest {
    private NaverFilmExtractor extractor;

    @Before
    public void setUp() throws Exception {
        extractor = new NaverFilmExtractor(Lists.newArrayList("도어락"));
    }

    @Test
    public void testCrawl() throws Exception {
        for (Object obj: extractor.crawl()) {
            Review review = (Review) obj;
            System.out.println(review.toString());
        }
    }
}