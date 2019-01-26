package com.doubleknd26.filmography.indexer;

import com.doubleknd26.filmography.indexer.crawl.FilmInfoCrawler;
import com.doubleknd26.filmography.indexer.model.FilmInfo;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Kideok Kim on 2018-12-08.
 */
public class FilmographyIndexer {
    private SparkSession ss;

    public FilmographyIndexer() {
        this.ss = SparkSession.builder()
                .appName("Filmography Indexer")
                .getOrCreate();
    }

    public static void main(String[] args) {
        FilmographyIndexer indexer = new FilmographyIndexer();
        try {
            indexer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        FilmInfoCrawler filmTitleCrawler = new FilmInfoCrawler();
        Set<FilmInfo> filmInfos = filmTitleCrawler.crawl();

    }


//    private static final ImmutableList<Crawler> crawlingList = ImmutableList.<Crawler>builder()
//            .add(new NaverMovieCrawler())
//            .build();
//    private SparkSession ss;
//    private JavaSparkContext jsc;

//

//    public void run() throws Exception {
//        MovieNameCrawler movieNameCrawler = new MovieNameCrawler();
//        ClassTag<Set<String>> classTag = ScalaUtils.getClassTag(Set.class);
//        Broadcast<Set<String>> titles =  ss.sparkContext().broadcast(movieNameCrawler.crawl(), classTag);
//
//        JavaRDD<Review> reviewRdd = jsc.emptyRDD();
//        for (Crawler crawler : crawlingList) {
//            List<Review> lists = Lists.newArrayList(crawler.crawl());
//            reviewRdd = reviewRdd.union(jsc.parallelize(lists));
//        }
//
//        ReviewTransformer transformer = new ReviewTransformer(titles, reviewRdd);
//        JavaRDD<MovieInfo> transformed = transformer.run();
//    }
}
