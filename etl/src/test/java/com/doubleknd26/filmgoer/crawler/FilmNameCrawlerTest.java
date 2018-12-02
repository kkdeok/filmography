package com.doubleknd26.filmgoer.crawler;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by doubleknd26 on 02/12/2018.
 */
public class FilmNameCrawlerTest {
    private FilmNameCrawler crawler;

    @Before
    public void setUp() throws Exception {
        crawler = new FilmNameCrawler();
    }

    @Test
    public void crawl() throws Exception {
        Set response = crawler.crawl();
        assertThat(response.isEmpty(), is(false));
    }

    @Test
    public void typeCasting() throws Exception {
        Set response = crawler.crawl();
        for (Object obj: response) {
            String result = obj instanceof String ? ((String) obj) : null;
            assertThat(result == null || result.isEmpty(), is(false));
        }
    }

    @Test
    public void parse() throws Exception {
        Set response = crawler.crawl();
        for (Object obj: response) {
            String result = (String) obj;
            assertThat(result.startsWith(" "), is(false));
            assertThat(result.endsWith(" "), is(false));
        }
    }
}