package com.doubleknd26.moving.indexer;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.doubleknd26.moving.indexer.crawl.Crawler;
import com.doubleknd26.moving.indexer.crawl.MovieNameCrawler;
import com.doubleknd26.moving.indexer.crawl.NaverMovieCrawler;
import com.doubleknd26.moving.indexer.transform.ReviewTransformer;
import com.doubleknd26.moving.indexer.utils.ScalaUtils;
import com.doubleknd26.moving.proto.MovieInfo;
import com.doubleknd26.moving.proto.Review;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import scala.reflect.ClassTag;

import java.util.List;
import java.util.Set;

/**
 * Created by Kideok Kim on 2018-12-08.
 */
public class Indexer {
    @VisibleForTesting
    protected static class Params {
        @Parameter(names = {"--shards"}, description = "index shard count")
        public int shards = 2;

        @Parameter(names = {"--master"}, description = "spark master")
        public String master = "local[*]";
    }

    private static final ImmutableList<Crawler> crawlingList = ImmutableList.<Crawler>builder()
            .add(new NaverMovieCrawler())
            .build();
    private SparkSession ss;
    private JavaSparkContext jsc;

    public Indexer(Params params) {
        this.ss = SparkSession.builder()
                .appName("Moving Indexer")
                .master(params.master)
                .getOrCreate();
        this.jsc = new JavaSparkContext(this.ss.sparkContext());
    }

    public static void main(String[] args) throws Exception {
        Params params = new Params();
        JCommander jCommander = new JCommander(params);
        jCommander.parse(args);
        Indexer indexer = new Indexer(params);
        indexer.run();
    }

    public void run() throws Exception {
        MovieNameCrawler movieNameCrawler = new MovieNameCrawler();
        ClassTag<Set<String>> classTag = ScalaUtils.getClassTag(Set.class);
        Broadcast<Set<String>> titles =  ss.sparkContext().broadcast(movieNameCrawler.crawl(), classTag);

        JavaRDD<Review> reviewRdd = jsc.emptyRDD();
        for (Crawler crawler : crawlingList) {
            List<Review> lists = Lists.newArrayList(crawler.crawl());
            reviewRdd = reviewRdd.union(jsc.parallelize(lists));
        }

        ReviewTransformer transformer = new ReviewTransformer(titles, reviewRdd);
        JavaRDD<MovieInfo> transformed = transformer.run();
    }
}
