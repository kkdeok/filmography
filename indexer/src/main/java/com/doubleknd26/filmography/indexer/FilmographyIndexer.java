package com.doubleknd26.filmography.indexer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.doubleknd26.filmography.indexer.common.KryoRegistratorWrapper;
import com.doubleknd26.filmography.indexer.crawl.FilmInfoCrawler;
import com.doubleknd26.filmography.indexer.crawl.NaverFilmReviewCrawler;
import com.doubleknd26.filmography.indexer.transform.FilmographyTransformer;
import com.doubleknd26.filmography.proto.FilmInfo;
import com.doubleknd26.filmography.proto.Review;
import com.doubleknd26.filmography.solrmanager.SolrManager;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.apache.commons.collections.IteratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kideok Kim on 2018-12-08.
 */
public class FilmographyIndexer implements Serializable {

    @Parameter(names = {"--local"}, required = false, arity = 1,
            description = "if the spark job run on local")
    protected boolean isLocal = false;

    @Parameter(names = {"--shard"}, required = false, arity = 1,
            description = "solr shard count")
    protected int shard = 1;

    private static final Logger logger = LogManager.getLogger();


    public static void main(String[] args) throws Exception {
        FilmographyIndexer indexer = new FilmographyIndexer();
        JCommander commander = new JCommander(indexer);
        commander.parse(args);
        indexer.start();
    }

    public void start() throws Exception {
        SparkSession ss = prepareSpark();
        JavaSparkContext jsc = new JavaSparkContext(ss.sparkContext());

        JavaRDD<FilmInfo> filmInfoRdd = getFilmInfo(jsc);
        JavaRDD<Review> reviewRdd = getReview(jsc);

        FilmographyTransformer transformer = new FilmographyTransformer();
        JavaRDD<SolrInputDocument> docs = transformer.transform(filmInfoRdd, reviewRdd);

        System.out.println("count:: " + docs.count());

        LongAccumulator indexedDocCount = ss.sparkContext()
                .longAccumulator("indexed-doc-count");
        List<String> skippedDocs = index(docs, shard, indexedDocCount);
        monitor(skippedDocs, indexedDocCount);
    }

    private SparkSession prepareSpark() {
        SparkSession.Builder builder = SparkSession.builder();
        if (isLocal) {
            builder.master("local[*]");
        }
        return builder
                .appName(this.getClass().getName())
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.kryo.registrator", KryoRegistratorWrapper.class.getCanonicalName())
                .getOrCreate();
    }

    @VisibleForTesting
    protected List<String> index(JavaRDD<SolrInputDocument> docs, int partition, LongAccumulator indexedDocCount) {
        Function2<Integer, Iterator<SolrInputDocument>, Iterator<String>> func =
                new Function2<Integer, Iterator<SolrInputDocument>, Iterator<String>>() {
                    @Override
                    public Iterator<String> call(
                            Integer partition, Iterator<SolrInputDocument> docs) throws Exception {
                        SolrManager solrManager = SolrManager.getInstance();
                        EmbeddedSolrServer server = solrManager.setupSolrServer(partition);

                        List<String> skippedDocs = Lists.newArrayList();
                        while (docs.hasNext()) {
                            SolrInputDocument doc = docs.next();
                            try {
                                server.add(docs);
                                indexedDocCount.add(1);
                            } catch (Exception e) {
                                skippedDocs.add(String.valueOf(doc.toString()));
                            }
                        }
                        server.commit(true, true);
                        server.close();
                        return skippedDocs.iterator();
                    }
                };
        return docs.repartition(partition)
                .mapPartitionsWithIndex(func, true)
                .collect();
    }

    private void monitor(List<String> skippedDoc, LongAccumulator indexedDocCount) {
        logger.info("** indexed doc count: " + indexedDocCount.value().longValue());
        logger.info("** skipped doc count: " + skippedDoc.size());
        for (String doc: skippedDoc) {
            logger.info("*** " + doc);
        }
        logger.info("** Filmography index is generated successfully");
    }

    private JavaRDD<Review> getReview(JavaSparkContext jsc) throws IOException {
        NaverFilmReviewCrawler reviewCrawler = new NaverFilmReviewCrawler();
        List<Review> reviews = IteratorUtils.toList(reviewCrawler.crawl().iterator());
        return jsc.parallelize(reviews);
    }

    private JavaRDD<FilmInfo> getFilmInfo(JavaSparkContext jsc) throws IOException {
        FilmInfoCrawler crawler = new FilmInfoCrawler();
        List<FilmInfo> filmInfos = IteratorUtils.toList(crawler.crawl().iterator());
        return jsc.parallelize(filmInfos);
    }
}
