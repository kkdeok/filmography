package com.doubleknd26.moving.indexer.crawl;

import com.google.common.collect.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Set;

/**
 * This class crawls current running movie name from NAVER movie web page.
 * Here is url, https://movie.naver.com/movie/running/current.nhn
 *
 * Created by Kideok Kim on 02/12/2018.
 */
public class MovieNameCrawler implements Crawler {
    private static final String BASE_URL = "https://movie.naver.com/movie/running/current.nhn";

    @Override
    public Set<String> crawl() throws Exception {
        Set<String> runningFilmNames = Sets.newHashSet();
        Document doc = Jsoup.connect(BASE_URL).get();
        Elements elements = doc.body()
                .getElementsByClass("lst_detail_t1")
                .select("li");
        for (Element element: elements) {
            String title = element.getElementsByClass("tit").select("a").text();
            runningFilmNames.add(title);
        }
        return runningFilmNames;
    }
}
