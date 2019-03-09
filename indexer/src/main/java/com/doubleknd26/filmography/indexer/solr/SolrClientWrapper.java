package com.doubleknd26.filmography.indexer.solr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.*;
import org.apache.solr.handler.RequestHandlerUtils;
import org.apache.solr.handler.UpdateRequestHandler;
import org.apache.solr.handler.UpdateRequestHandlerApi;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.update.CommitUpdateCommand;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kideok Kim on 2019-02-09.
 */
public class SolrClientWrapper {
    private static final Logger logger = LogManager.getLogger();
    private static final List<String> baseUrls = Arrays.asList("http://localhost:8983/solr/");
    private static final String COLLECTION_NAME = "filmography";
    private static final int NUM_SHARDS = 1;
    private static final int NUM_REPLICAS = 1;
    private static SolrClientWrapper instance = null;
    private final SolrClient client;
    private String collection;

    public static SolrClientWrapper getInstance() {
        if (instance == null) {
            synchronized (SolrClientWrapper.class) {
                if (instance == null) {
                    instance = new SolrClientWrapper(COLLECTION_NAME);
                }
            }
        }
        return instance;
    }

    private SolrClientWrapper(String collection) {
        this(baseUrls, collection);
    }

    private SolrClientWrapper(List<String> solrBaseUrls, String collection) {
        this.client = new CloudSolrClient.Builder(solrBaseUrls).build();
        this.collection = collection;
        try {
            if (isCollectionExist()) {
                initializeCollection();
            } else {
                createCollection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCollection() throws Exception {
        CollectionAdminRequest.Delete request = CollectionAdminRequest
                .deleteCollection(collection);
        CollectionAdminResponse response = request.process(client);
        if (!response.isSuccess()) {
            throw new RuntimeException(response.getErrorMessages().toString());
        }
        createCollection();
    }

    private void createCollection() throws Exception {
        CollectionAdminRequest.Create request = CollectionAdminRequest
                .createCollection(collection, NUM_SHARDS, NUM_REPLICAS)
                .setMaxShardsPerNode(1);
        CollectionAdminResponse response = request.process(client);
        if (!response.isSuccess()) {
            throw new RuntimeException(response.getErrorMessages().toString());
        }
        logger.info("collection {} is created. ", collection);
    }

    private boolean isCollectionExist() throws Exception {
        List<String> collections = CollectionAdminRequest.listCollections(client);
        return collections.contains(collection);
    }

    public void add(SolrInputDocument doc) throws Exception {
        client.add(collection, doc);
    }

    public void commit() throws Exception {
        client.commit(collection);
    }
}
