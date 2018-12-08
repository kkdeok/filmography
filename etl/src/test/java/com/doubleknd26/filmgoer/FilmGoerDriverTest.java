package com.doubleknd26.filmgoer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by doubleknd26 on 2018-12-08.
 */
public class FilmGoerDriverTest {

    @Test
    public void testRun() throws Exception {
        FilmGoerDriver driver = new FilmGoerDriver();
        driver.run();
    }

}