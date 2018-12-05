package com.doubleknd26.filmgoer.extract;

import com.google.common.collect.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class crawls current running film name from NAVER movie web page.
 * Here is url, https://movie.naver.com/movie/running/current.nhn
 *
 * Created by doubleknd26 on 02/12/2018.
 */
public class FilmNameExtractor implements Extractor {
    private static final String BASE_URL = "https://movie.naver.com/movie/running/current.nhn";
    private static final String PREFIX = "관람";
    private static final String POSTFIX = " 네티즌";

    @Override
    public Set crawl() throws Exception {
        Set<String> runnings = Sets.newHashSet();
        Document doc = Jsoup.connect(BASE_URL).get();
        runnings.addAll(parse(doc));
        return runnings;
    }

    private Set<String> parse(Document doc) {
        Elements elements = doc.body()
                .getElementsByClass("lst_detail_t1")
                .select("li");
        return elements.stream().map(ele -> ele.text()
                .substring(ele.text().indexOf(PREFIX) + 4, ele.text().indexOf(POSTFIX))
                .replaceFirst(" ", ""))
                .collect(Collectors.toSet());
    }
}
