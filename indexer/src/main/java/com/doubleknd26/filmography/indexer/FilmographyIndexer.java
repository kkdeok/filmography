package com.doubleknd26.filmography.indexer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.doubleknd26.filmography.indexer.common.KryoRegistratorWrapper;
import com.doubleknd26.filmography.indexer.crawl.FilmInfoCrawler;
import com.doubleknd26.filmography.indexer.crawl.NaverFilmReviewCrawler;
import com.doubleknd26.filmography.indexer.transform.FilmographyTransformer;
import com.doubleknd26.filmography.proto.FilmInfo;
import com.doubleknd26.filmography.proto.Review;
import org.apache.commons.collections.IteratorUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.List;

import static com.doubleknd26.filmography.utils.ProtoUtils.protoToJson;

/**
 * Created by Kideok Kim on 2018-12-08.
 */
public class FilmographyIndexer {

    @Parameter(names = { "--local" }, required = false, arity = 1,
            description = "if the spark job run on local")
    protected boolean isLocal = false;


    public static void main(String[] args) throws Exception {
        FilmographyIndexer indexer = new FilmographyIndexer();
        JCommander commander = new JCommander(indexer);
        commander.parse(args);
        indexer.start();
    }

    public void start() throws IOException {
        SparkSession ss = prepareSpark();
        JavaSparkContext jsc = new JavaSparkContext(ss.sparkContext());

        JavaRDD<FilmInfo> filmInfoRdd = getFilmInfo(jsc);
        JavaRDD<Review> reviewRdd = getReview(jsc);

        FilmographyTransformer transformer = new FilmographyTransformer();
        JavaRDD<SolrDocument> docs = transformer.transform(filmInfoRdd, reviewRdd);


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

    private SparkSession prepareSpark() {
        SparkSession.Builder builder = SparkSession.builder()
                .appName(this.getClass().getName());
        if (isLocal) {
            builder.master("local[*]");
        }
        return builder
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.kryo.registrator", KryoRegistratorWrapper.class.getCanonicalName())
                .getOrCreate();
    }
}
