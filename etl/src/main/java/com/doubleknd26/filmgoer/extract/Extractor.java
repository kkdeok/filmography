package com.doubleknd26.filmgoer.extract;

import java.io.IOException;
import java.util.Set;

/**
 *
 * Created by doubleknd26 on 02/12/2018.
 */
public interface Extractor {
    Set crawl() throws IOException;
}
