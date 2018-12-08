package com.doubleknd26.moving.extract;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by doubleknd26 on 02/12/2018.
 */
public class FilmNameExtractorTest {
    private FilmNameExtractor crawler;

    @Before
    public void setUp() throws Exception {
        crawler = new FilmNameExtractor();
    }

    @Test
    public void testExtract() throws Exception {
        Set response = crawler.extract();
        assertThat(response.isEmpty(), is(false));
    }

    @Test
    public void testTypeCasting() throws Exception {
        Set response = crawler.extract();
        for (Object obj: response) {
            String result = obj instanceof String ? ((String) obj) : null;
            assertThat(result == null || result.isEmpty(), is(false));
        }
    }

    @Test
    public void parse() throws Exception {
        Set response = crawler.extract();
        for (Object obj: response) {
            String result = (String) obj;
            assertThat(result.startsWith(" "), is(false));
            assertThat(result.endsWith(" "), is(false));
        }
    }
}