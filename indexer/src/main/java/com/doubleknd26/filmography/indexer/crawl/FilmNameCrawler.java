package com.doubleknd26.filmography.indexer.crawl;

import com.google.common.collect.Sets;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * This class crawls running film name from NAVER web page.
 *
 * Created by Kideok Kim on 02/12/2018.
 */
public class FilmNameCrawler extends Crawler {
    private static final List<String> URLS = Arrays.asList("https://movie.naver.com/movie/running/current.nhn");
    private static final List<String> SELECTORS = Arrays.asList(".tit", "a");

    public FilmNameCrawler() {
        super(URLS, SELECTORS);
    }

    @Override
    Set parse(Elements elements) {
        Set<String> filmNames = Sets.newHashSet();
        for (Element elem: elements) {
            System.out.println(elem.text());
            filmNames.add(elem.text());
        }
        return filmNames;
    }
}
