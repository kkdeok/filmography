package com.doubleknd26.filmgoer.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.namenode.NameNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by doubleknd26 on 2018-12-04.
 */
public class MiniDFSClusterWrapper extends MiniDFSCluster {
    private Configuration conf;
    private File baseDir;

    public MiniDFSClusterWrapper() throws IOException {
        this.baseDir = makeBaseDir();
        this.conf = new Configuration();
        this.conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, baseDir.getAbsolutePath());

        MiniDFSCluster.Builder builder = new MiniDFSCluster.Builder(this.conf);
        builder.nameNodeHttpPort(50070);
        builder.numDataNodes(3);
//        initMiniDFSCluster()
    }

    private File makeBaseDir() {
        final String path = Paths.get(".").toAbsolutePath().normalize().toString() + "/tmp";
        File baseDir = new File(path);
        if (!baseDir.exists()) {
            baseDir.mkdir();
        }
        return baseDir;
    }



    @Override
    public NameNode getNameNode() {
        return cluster.getNameNode();
    }
}
