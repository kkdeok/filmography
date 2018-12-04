package com.doubleknd26.filmgoer.common;

import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.junit.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by doubleknd26 on 2018-12-04.
 */
public class MiniDFSClusterWrapperTest {
    private static MiniDFSClusterWrapper clusterWrapper;

    @BeforeClass
    public static void setUp() throws Exception {
        clusterWrapper = new MiniDFSClusterWrapper();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        clusterWrapper.shutdown(true);
    }

    @Test
    public void testNameNode() throws Exception {
        NameNode nn = clusterWrapper.getNameNode();
        System.out.println(nn.getHostAndPort());
        assertNotNull("No namenode", nn);
        assertThat(nn.isActiveState(), is(true));
    }

}