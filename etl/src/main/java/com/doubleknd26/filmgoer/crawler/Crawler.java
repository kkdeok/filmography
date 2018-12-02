package com.doubleknd26.filmgoer.crawler;

import java.io.IOException;
import java.util.Set;

/**
 *
 * Created by doubleknd26 on 02/12/2018.
 */
public interface Crawler {
    Set crawl() throws IOException;
}
