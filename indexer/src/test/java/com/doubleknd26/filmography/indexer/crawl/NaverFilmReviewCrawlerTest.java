package com.doubleknd26.filmography.indexer.crawl;

import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Kideok Kim on 2019-01-26.
 */
public class NaverFilmReviewCrawlerTest {

    @Test
    public void testCrawl() throws IOException {
        NaverFilmReviewCrawler crawler = new NaverFilmReviewCrawler();
        Set response = crawler.crawl();
        assertFalse(response.isEmpty());
    }
}