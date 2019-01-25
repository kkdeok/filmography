package com.doubleknd26.filmography.indexer.crawl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by Kideok Kim on 02/12/2018.
 */
public abstract class Crawler <T> {
    private List<String> urls;
    // Jsoup provide a select api to get specific tags from parsed html using
    // a css selector. ref)https://www.w3schools.com/cssref/css_selectors.asp
    private List<String> selectors;

    Crawler(List<String> urls, List<String> selectors) {
        this.urls = urls;
        this.selectors = selectors;
    }

    Crawler(String url, int pageLimit, List<String> selectors) {
        List<String> urls = Lists.newArrayList();
        for (int pageNum = 1; pageNum <= pageLimit; pageNum++) {
            urls.add(url + pageNum);
        }
        this.urls = urls;
        this.selectors = selectors;
    }

    Set<T> crawl() throws IOException {
        Set<T> response = Sets.newHashSet();
        for (String url: urls) {
            Document doc = Jsoup.connect(url).get();
            String cssQuery = StringUtils.join(selectors, " ");
            Elements elements = doc.select(cssQuery);
            response.addAll(parse(elements));
        }
        return response;
    }

    abstract Set<T> parse(Elements elements);
}
