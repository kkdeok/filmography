//package com.doubleknd26.filmography.indexer.crawl;
//
//import com.doubleknd26.filmography.proto.Review;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.Set;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.*;
//
///**
// * Created by Kideok Kim on 2018-12-05.
// */
//public class NaverMovieCrawlerTest {
//    private NaverMovieCrawler extractor;
//
//    @Before
//    public void setUp() throws Exception {
//        this.extractor = new NaverMovieCrawler(1);
//    }
//
//    @Test
//    public void testCrawl() throws Exception {
//        Set<Review> response = extractor.crawl();
//        assertThat(response.isEmpty(), is(false));
//    }
//}