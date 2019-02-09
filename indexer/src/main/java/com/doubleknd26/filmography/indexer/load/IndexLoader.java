package com.doubleknd26.filmography.indexer.load;

import com.doubleknd26.filmography.indexer.solr.SolrClientWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.util.LongAccumulator;

import java.io.Serializable;
import java.util.Iterator;


/**
 * Created by Kideok Kim on 2019-02-09.
 */
public class IndexLoader implements Serializable {
    private static final Logger logger = LogManager.getLogger();

    public void load(JavaRDD<SolrInputDocument> docs,
                     LongAccumulator docCount, LongAccumulator skipDocCount) {
        docs.foreachPartition(new VoidFunction<Iterator<SolrInputDocument>>() {
            @Override
            public void call(Iterator<SolrInputDocument> docs) throws Exception {
                SolrClientWrapper solrClient = SolrClientWrapper.getInstance();
                while (docs.hasNext()) {
                    SolrInputDocument doc = docs.next();
                    try {
                        solrClient.add(doc);
                        docCount.add(1L);
                    } catch (Exception e) {
                        logger.error("failed to add doc: {}", e.getMessage());
                        skipDocCount.add(1L);
                    }
                }
                solrClient.commit();
            }
        });
    }
}
