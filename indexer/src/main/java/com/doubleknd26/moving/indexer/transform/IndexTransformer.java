package com.doubleknd26.moving.indexer.transform;

import com.doubleknd26.moving.proto.MovieInfo;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;

import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by doubleknd26 on 2018-12-11.
 */
public class IndexTransformer {
    private static final Logger logger = LogManager.getLogger(IndexTransformer.class);
    private JavaRDD<MovieInfo> source;
    private final int shards;

    public IndexTransformer(JavaRDD<MovieInfo> source, int shards) {
        this.source = source;
        this.shards = shards;
    }

    public void run() {
        final Function2<Integer, Iterator<MovieInfo>, Iterator<MovieInfo>> func =
                new Function2<Integer, Iterator<MovieInfo>, Iterator<MovieInfo>>() {
                    @Override
                    public Iterator<MovieInfo> call(Integer shardIndex, Iterator<MovieInfo> iterator) throws Exception {
                        return indexShard(shardIndex, iterator);
                    }
                };
        source.repartition(shards)
                .mapPartitionsWithIndex(func, true);
    }

    private Iterator<MovieInfo> indexShard(int shardIndex, Iterator<MovieInfo> iterator) throws Exception {
        logger.info("start build index shard: " + shardIndex);
        final String solrHome = Files.createTempDirectory("solr_home").toAbsolutePath().toString();
        List<MovieInfo> indexDocs = Lists.newArrayList(iterator);

        return null;
    }
}
