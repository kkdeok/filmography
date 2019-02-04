package com.doubleknd26.filmography.indexer;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Kideok Kim on 2019-02-03.
 */
public class FilmographyIndexerTest {

    @Test
    public void start() throws IOException {
        FilmographyIndexer indexer = new FilmographyIndexer();
        indexer.isLocal = true;
        indexer.start();
    }
}