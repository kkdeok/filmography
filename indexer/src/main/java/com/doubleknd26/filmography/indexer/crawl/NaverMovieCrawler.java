package com.doubleknd26.filmography.indexer.crawl;

import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * This class crawls film review from NAVER web page.
 *
 * Created by Kideok Kim on 2018-12-05.
 */
public class NaverMovieCrawler extends Crawler {
    private static final String URL = "https://movie.naver.com/movie/point/af/list.nhn?target=after&page=";
    private static final List<String> SELECTORS = Arrays.asList("list_netizen", "tbody", "tr");
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd");
    private static final int DEFAULT_PAGE_LIMIT = 10;

    public NaverMovieCrawler() {
        this(DEFAULT_PAGE_LIMIT);
    }

    public NaverMovieCrawler(int pageLimit) {
        super(URL, pageLimit, SELECTORS);
    }

    /**
     * This is a example what format element has.
     * <tr>
     *   <td class="ac num">14951220</td>
     *   <td>
     *     <div class="fr point_type_n">
     *     <div class="mask" style="width:10.0%"></div>
     *     </div></td>
     *   <td class="point">1</td>
     *   <td class="title">
     *     <a href="?st=mcode&amp;sword=76972&amp;target=after" class="movie">인 타임</a>
     *     <br>{REVIEW}
     *     <a href="...>신고</a>
     *   </td>
     *   <td class="num">
     *     <a href="...">spin****</a>
     *     <br>18.12.08
     *   </td>
     * </tr>
     * @param element
     * @return
     * @throws ParseException
     *
     * TODO: start from here.
     */
    private Review.Builder parse (Element element) throws ParseException {
        // grade
        int grade = Integer.parseInt(element.getElementsByClass("point").text());
        // title
        String title = element.getElementsByClass("title").select("a").text();
        title = title.substring(0, title.length() - 3);
        // comment
        String comment = element.getElementsByClass("title").html();
        comment = comment.substring(comment.indexOf("<br>") + 4);
        comment = comment.substring(0, comment.indexOf("<a") - 1);
        // id
        String id = element.getElementsByClass("num").select("a").text();
        // timestamp
        String date = element.getElementsByClass("num").html();
        date = date.substring(date.indexOf("<br>") + 4);
        long timestamp = formatter.parse(date).toInstant().getEpochSecond();
        return Review.newBuilder()
                .setSourceType(SourceType.NAVER)
                .setTitle(title)
                .setGrade(grade)
                .setComment(comment)
                .setTimestamp(timestamp);
    }
}
