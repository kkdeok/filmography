package com.doubleknd26.filmography.indexer;

import com.doubleknd26.filmography.indexer.common.KryoRegistratorWrapper;
import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Kideok Kim on 2019-02-03.
 */
public class FilmographyIndexerTest {

    @Test
    public void testStart() throws Exception {
        FilmographyIndexer indexer = new FilmographyIndexer();
        indexer.isLocal = true;
        indexer.start();
    }

    @Test
    public void testIndex() {
        SparkSession ss = SparkSession.builder()
                .master("local[*]")
                .appName(this.getClass().getName())
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.kryo.registrator", KryoRegistratorWrapper.class.getCanonicalName())
                .getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(ss.sparkContext());

        FilmographyIndexer indexer = new FilmographyIndexer();
        indexer.isLocal = true;

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("title", "test value");

        LongAccumulator indexedDocCount = ss.sparkContext()
                .longAccumulator("indexed-doc-count");

        List<String> skippedDocs = indexer.index(jsc.parallelize(Arrays.asList(doc)), 1, indexedDocCount);
        assertEquals(0, skippedDocs.size());
    }
}