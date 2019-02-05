package com.doubleknd26.filmography.solrmanager;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Kideok Kim on 2019-02-04.
 */
public class SolrManagerTest {
    private EmbeddedSolrServer server;

    @After
    public void tearDown() throws Exception {
        if (server != null) {
            server.close();
        }
    }

    @Test
    public void test() throws Exception {
        final String testField = "title";
        final String testValue = "testvalue";
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(testField, testValue);

        SolrManager solrManager = SolrManager.getInstance();
        EmbeddedSolrServer server = solrManager.setupSolrServer(0);

        server.add(doc);
        server.commit();

        SolrParams params = new SolrQuery("testvalue");
        QueryResponse response = server.query(params);

        assertEquals(1, response.getResults().getNumFound());
        assertEquals(testValue, response.getResults().get(0).get(testField));
    }
}