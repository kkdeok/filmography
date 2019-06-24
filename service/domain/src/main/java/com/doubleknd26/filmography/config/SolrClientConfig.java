package com.doubleknd26.filmography.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.solr.SolrProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackages = "com.doubleknd26.filmography")
public class SolrClientConfig {

    @Bean
    public SolrClient solrClient(SolrProperties solrProperties) {
        return new HttpSolrClient.Builder(solrProperties.getHost())
                .build();
    }

}
