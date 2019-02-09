package com.doubleknd26.filmography.indexer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.doubleknd26.filmography.indexer.common.KryoRegistratorWrapper;
import com.doubleknd26.filmography.indexer.crawl.FilmInfoCrawler;
import com.doubleknd26.filmography.indexer.crawl.NaverFilmReviewCrawler;
import com.doubleknd26.filmography.indexer.load.IndexLoader;
import com.doubleknd26.filmography.indexer.transform.IndexTransformer;
import com.doubleknd26.filmography.proto.FilmInfo;
import com.doubleknd26.filmography.proto.Review;
import org.apache.commons.collections.IteratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Kideok Kim on 2018-12-08.
 */
public class FilmographyIndexer implements Serializable {

    @Parameter(names = {"--local"}, required = false, arity = 1,
            description = "if the spark job run on local")
    protected boolean isLocal = false;

    private static final Logger logger = LogManager.getLogger();
    private LongAccumulator indexedDocCount;
    private LongAccumulator unindexedDocCount;

    public void start() throws Exception {
        SparkSession ss = prepareSpark();
        JavaSparkContext jsc = new JavaSparkContext(ss.sparkContext());

        logger.info("*** Started Filmography indexer.");

        JavaRDD<FilmInfo> filmInfoRdd = getFilmInfo(jsc);
        JavaRDD<Review> reviewRdd = getReview(jsc);

        IndexTransformer transformer = new IndexTransformer();
        JavaRDD<SolrInputDocument> docs = transformer.transform(filmInfoRdd, reviewRdd);

        IndexLoader loader = new IndexLoader();
        loader.load(docs, indexedDocCount, unindexedDocCount);

        logger.info("*** Indexed doc count: " + indexedDocCount.value());
        logger.info("*** Unindexed doc count: " + unindexedDocCount.value());
        logger.info("*** Filmography is ready to be search. Let's search on solr.");
        logger.info("*** please visit: http://localhost:8983/solr/#/filmography/query");
    }

    private SparkSession prepareSpark() {
        SparkSession.Builder builder = SparkSession.builder();
        if (isLocal) {
            builder.master("local[*]");
        }
        SparkSession ss = builder
                .appName(this.getClass().getName())
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.kryo.registrator", KryoRegistratorWrapper.class.getCanonicalName())
                .getOrCreate();
        ss.sparkContext().setLogLevel("ERROR");
        indexedDocCount = ss.sparkContext().longAccumulator("indexed-doc-count");
        unindexedDocCount = ss.sparkContext().longAccumulator("unindexed-doc-count");
        return ss;
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

    public static void main(String[] args) throws Exception {
        FilmographyIndexer indexer = new FilmographyIndexer();
        JCommander commander = new JCommander(indexer);
        commander.parse(args);
        indexer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stdout here since the logger may have been reset by its JVM shutdown hook.
            System.out.println("*** Filmography shut down");
        }));
    }
}
