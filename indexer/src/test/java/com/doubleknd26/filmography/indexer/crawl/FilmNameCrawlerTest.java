package com.doubleknd26.filmography.indexer.crawl;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Kideok Kim on 02/12/2018.
 */
public class FilmNameCrawlerTest {

    @Test
    public void testCrawl() throws Exception {
        FilmNameCrawler crawler = new FilmNameCrawler();
        Set response = crawler.crawl(FilmNameCrawler.URL, FilmNameCrawler.SELECTORS);
        assertFalse(response.isEmpty());
    }
}