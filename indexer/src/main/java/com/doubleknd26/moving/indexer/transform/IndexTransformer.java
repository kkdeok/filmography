package com.doubleknd26.moving.indexer.transform;

import com.doubleknd26.moving.proto.MovieInfo;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;

import java.util.Iterator;

/**
 * Created by doubleknd26 on 2018-12-11.
 */
public class IndexTransformer {
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
                    public Iterator<MovieInfo> call(Integer index, Iterator<MovieInfo> it) throws Exception {
                        return indexShard(index, it);
                    }
                };
        source.repartition(shards)
                .mapPartitionsWithIndex(func, true);
    }

    private Iterator<MovieInfo> indexShard(int index, Iterator<MovieInfo> it) {
        return null;
    }
}
