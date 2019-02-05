package com.doubleknd26.filmography.searcher;

import com.doubleknd26.filmography.solrmanager.SolrManager;
import org.apache.lucene.search.PhraseQuery;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Kideok Kim on 2019-02-05.
 */
public class FilmographySearcherTest {

    @Test
    public void testGetSolrServer() throws Exception {
        // given
        final String testValue = "testvalue";
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("title", testValue);
        doc.addField("_root_", testValue);

        SolrManager solrManager = SolrManager.getInstance();
        EmbeddedSolrServer server = solrManager.setupSolrServer(0);

        server.add(doc);
        server.commit();
        server.close();

        List<EmbeddedSolrServer> servers = solrManager.getServers();

        SolrQuery query = new SolrQuery();
        query.setQuery(testValue);
        query.setStart(0);
        QueryResponse response = servers.get(0).query(query);

        assertEquals(1, response.getResults().getNumFound());
        assertEquals(testValue, response.getResults().get(0).get("title"));
    }

    @Test
    public void testRequest() {

    }
}