package com.doubleknd26.filmography.indexer;

import org.junit.Test;

import java.io.Serializable;

/**
 * Created by Kideok Kim on 2019-02-03.
 */
public class FilmographyIndexerTest implements Serializable {

    @Test
    public void testStart() throws Exception {
        FilmographyIndexer indexer = new FilmographyIndexer();
        indexer.isLocal = true;
        indexer.start();
    }
}