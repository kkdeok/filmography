package com.doubleknd26.filmography.searcher;

import com.doubleknd26.filmography.solrmanager.SolrManager;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;

import java.util.List;

/**
 * Created by Kideok Kim on 2019-02-05.
 */
public class FilmographySearcher {
    private List<EmbeddedSolrServer> servers;

    public FilmographySearcher() {
        servers = SolrManager.getInstance().getServers();
    }

    public static void main(String[] args) {

    }
}
