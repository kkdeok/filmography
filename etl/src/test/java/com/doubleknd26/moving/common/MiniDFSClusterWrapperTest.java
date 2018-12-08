package com.doubleknd26.moving.common;

import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.junit.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by doubleknd26 on 2018-12-04.
 */
public class MiniDFSClusterWrapperTest {
    private static MiniDFSClusterWrapper cluster;

    @BeforeClass
    public static void setUp() throws Exception {
        cluster = new MiniDFSClusterWrapper();
    }

    @AfterClass
    public static void tearDown() {
        cluster.shutdown();
    }

    @Test
    public void testNameNode() {
        NameNode nn = cluster.getNameNode();
        System.out.println(nn.getHostAndPort());
        assertNotNull("No namenode", nn);
        assertThat(nn.isActiveState(), is(true));
    }

}