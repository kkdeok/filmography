package com.doubleknd26.filmography.solrmanager;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by Kideok Kim on 2019-02-04.
 */
public class SolrManager implements Serializable {
    private static final Logger logger = LogManager.getLogger();
    private static final String configSet = "filmography";
    private static SolrManager solrManager;

    private CoreContainer container;
    private final String solrConfig;
    private final String solrHome;

    public static SolrManager getInstance() {
        synchronized (SolrManager.class) {
            if (solrManager == null) {
                solrManager = new SolrManager();
            }
            return solrManager;
        }
    }

    public SolrManager() {
        this.solrConfig = getSolrConfigPath();
        this.solrHome = getSolrHomePath();
        System.setProperty("solr.solr.home", solrHome);
        try {
            createSolrXml();
            copySolrConfig();
        } catch (Exception e) {
            logger.error("failed to prepare embedded solr server. {}", e.getMessage());
        }
        this.container = new CoreContainer(solrHome);
        container.load();
    }

    public EmbeddedSolrServer setupSolrServer(int shardNum) throws IOException {
        synchronized (SolrManager.class) {
            String shardName = getShardName(shardNum);
            if (container.isLoaded(shardName)) {
                container.unload(shardName);
            }
            FileUtils.deleteDirectory(
                    new File(solrHome + getShardName(shardNum)));

            Map<String, String> params = Maps.newHashMap();
            params.put("configSet", configSet);
            params.put("dataDir", "data");
            // Solr will create core.properties under the shard folder.
            SolrCore core = container.create(shardName, params);
            return new EmbeddedSolrServer(core);
        }
    }

    private static String getShardName(int num) {
        return String.format("shard%03d", num);
    }

    private void createSolrXml() throws IOException {
        FileUtils.writeStringToFile(new File(solrHome + "/solr.xml"),
                "<solr></solr>\n", Charset.defaultCharset());
    }

    private void copySolrConfig() throws IOException {
        final String copyPath = solrHome + "/configsets/" + configSet +"/conf/";
        FileUtils.copyDirectory(new File(solrConfig), new File(copyPath));
    }

    private String getSolrHomePath() {
        String path = SolrManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String solrHome = path.substring(0, path.indexOf("out")) + "solrhome";
        logger.info("solr home path: {}", solrHome);
        return solrHome;
    }

    private String getSolrConfigPath() {
        String path = SolrManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String solrConfig = path.substring(0, path.indexOf("out")) + "solrconfig";
        logger.info("solr config path: {}", solrConfig);
        return solrConfig;
    }
}
