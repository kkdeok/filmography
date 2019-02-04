package com.doubleknd26.filmography.indexer.crawl;

import com.doubleknd26.filmography.proto.FilmInfo;
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
public class FilmInfoCrawler extends WebCrawler {
    private static final List<String> URLS = Arrays.asList("https://movie.naver.com/movie/running/current.nhn");
    private static final List<String> TARGETS = Arrays.asList(".lst_detail_t1 li");

    public FilmInfoCrawler() {
        super(URLS, TARGETS);
    }

    @Override
    Set parse(Elements elements) {
        Set<FilmInfo> filmInfos = Sets.newHashSet();
        for (Element elem: elements) {
            String title = elem.select(".lst_dsc .tit a").text();
            String ageLimit = elem.select(".lst_dsc .tit span").text();
            String imagePath = elem.select(".thumb a img").first().absUrl("src");
            FilmInfo filmInfo = FilmInfo.newBuilder()
                    .setTitle(title)
                    .setAgeLimit(ageLimit)
                    .setImagePath(imagePath)
                    .build();
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }
}
