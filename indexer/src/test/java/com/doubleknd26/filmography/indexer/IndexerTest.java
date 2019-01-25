package com.doubleknd26.filmography.indexer;

import org.junit.Test;

/**
 * Created by Kideok Kim on 2018-12-11.
 */
public class IndexerTest {

    @Test
    public void testRun() throws Exception {
        Indexer.Params params = new Indexer.Params();
        Indexer indexer = new Indexer(params);
        indexer.run();
    }
}