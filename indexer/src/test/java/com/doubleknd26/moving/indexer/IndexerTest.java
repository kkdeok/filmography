package com.doubleknd26.moving.indexer;

import org.junit.Test;

import static org.junit.Assert.*;

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