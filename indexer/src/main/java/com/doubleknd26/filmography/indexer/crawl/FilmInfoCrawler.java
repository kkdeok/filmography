package com.doubleknd26.filmography.indexer.crawl;

import com.doubleknd26.filmography.indexer.common.Genre;
import com.doubleknd26.filmography.indexer.model.FilmInfo;
import com.doubleknd26.filmography.indexer.model.Review;
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

    // TODO: in progress
    @Override
    Set parse(Elements elements) {
        Set<FilmInfo> filmInfos = Sets.newHashSet();
        for (Element elem: elements) {
            String title = elem.select(".lst_dsc .tit a").text();
//            String director =
            String ageLimit = elem.select(".lst_dsc .tit span").text();
            String imgSrc = elem.select(".thumb a img").first().absUrl("src");
            String aa = elem.select(".lst_dsc .info_txt1").html();
//            long releaseTime;
//            int runningTime;
//            List<Review> reviews;


//            Elements info = elem.select(".lst_dsc .info_txt1 dd");

//            String director = elem.select(".lst_dsc .info_txt1 dd").get(2).text();

            System.out.println(aa);
            System.out.println(elem.html());
//            filmInfos.add(elem.text());
//            filmInfos.add(new FilmInfo())
        }
        return filmInfos;
    }
}
