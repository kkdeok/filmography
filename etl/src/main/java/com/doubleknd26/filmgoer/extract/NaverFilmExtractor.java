package com.doubleknd26.filmgoer.extract;

import com.doubleknd26.filmgoer.common.Source;
import com.doubleknd26.filmgoer.model.Review;
import com.google.common.collect.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 *
 * Created by doubleknd26 on 2018-12-05.
 */
public class NaverFilmExtractor implements Extractor {
    private static final String BASE_URL = "https://movie.naver.com/movie/point/af/list.nhn?target=after&page=";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd");
    // In Naver, there is 1,000 pages for showing user reviews. We can see 10 reviews per page.
    private static final int MAX_NUM = 1000;
    private int pageLimit = 0;

    public NaverFilmExtractor() {
        this(MAX_NUM);
    }

    public NaverFilmExtractor(int pageLimit) {
        if (pageLimit > MAX_NUM) {
            this.pageLimit = MAX_NUM;
        } else {
            this.pageLimit = pageLimit;
        }
    }

    @Override
    public Set<Review> extract() throws Exception {
        Set<Review> reviews = Sets.newHashSet();
        for (int i = 1; i <= pageLimit; i++) {
            String url = BASE_URL + i;
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.body()
                    .getElementsByClass("list_netizen")
                    .select("tbody")
                    .select("tr");
            for (Element element: elements) {
                Review review = parse(element);
                review.setUrl(url);
                reviews.add(review);
            }
        }
        return reviews;
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
     */
    private Review parse (Element element) throws ParseException {
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
        return new Review(Source.NAVER, title, grade, comment, timestamp, id);
    }
}
