package com.doubleknd26.filmography.indexer.crawl;

import com.doubleknd26.filmography.indexer.common.Source;
import com.doubleknd26.filmography.indexer.model.Review;
import com.google.common.collect.Sets;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Kideok Kim on 2018-12-05.
 */
public class NaverFilmReviewCrawler extends WebCrawler {
    private static final String URL = "https://movie.naver.com/movie/point/af/list.nhn?target=after&page=";
    private static final List<String> TARGET = Arrays.asList("tbody", "tr");
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd");
    private static final int DEFAULT_PAGE_LIMIT = 1;

    public NaverFilmReviewCrawler() {
        this(DEFAULT_PAGE_LIMIT);
    }

    public NaverFilmReviewCrawler(int pageLimit) {
        super(URL, pageLimit, TARGET);
    }

    @Override
    Set parse(Elements elements) {
        Set<Review> reviews = Sets.newHashSet();
        for (Element elem: elements) {
            String title = elem.select(".title .movie").text();
            String content = elem.select(".title").first().ownText();
            int grade = Integer.parseInt(elem.select(".point").text());
            String date = String.valueOf(elem.select(".num").last().ownText());
            long timestamp = convert(date);
            reviews.add(new Review(title, content, grade, timestamp, Source.NAVER));
        }
        return reviews;
    }

    private long convert(String date) {
        try {
            return formatter.parse(date).toInstant().toEpochMilli();
        } catch (ParseException e) {
            return 0L;
        }
    }
}
