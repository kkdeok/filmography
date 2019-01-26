package com.doubleknd26.filmography.indexer.crawl;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by coupang on 2019-01-26.
 */
public class FilmInfoCrawlerTest {

    @Test
    public void testCrawl() throws Exception {
        FilmInfoCrawler crawler = new FilmInfoCrawler();
        Set response = crawler.crawl();
    }

}