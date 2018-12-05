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
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 1page 10개 댓글 * 1000 = 10000 리뷰
 *
 * Created by doubleknd26 on 2018-12-05.
 */
public class NaverFilmExtractor implements Extractor {
    private static final String BASE_URL = "https://movie.naver.com/movie/point/af/list.nhn?target=after&page=";
    private static final int MAX_NUM = 10;
    private SimpleDateFormat formatter;
    private List<String> targets;

    public NaverFilmExtractor(List<String> targets) {
        formatter = new SimpleDateFormat("yy.MM.dd");
        this.targets = targets;
    }

    @Override
    public Set crawl() throws Exception {
        Set<Review> sets = Sets.newHashSet();
        for (int i = 1; i <= MAX_NUM; i++) {
            String url = BASE_URL + i;
            Document doc = Jsoup.connect(url).get();
            sets.addAll(parse(doc, url));
        }
        return sets;
    }

    private Set parse (Document doc, String url) throws ParseException {
        Elements elements = doc.body()
                .getElementsByClass("list_netizen")
                .select("tbody")
                .select("tr");
        // 테이블 번호, 평점, 제목, 리뷰, 신고(Text) 아이디, 날짜 (yy.mm.dd)
        // 14940327 8 도어락 서랍장침대 사세요 ~~~~ 신고 juns**** 18.12.05

        Set<Review> result = Sets.newHashSet();
        for (Element element: elements) {
//            String text = element.text();
//            String[] splited = text.split(" 신고 ");
//            String firstSplit = splited[0].substring(splited[0].indexOf(" ") + 1);
//            String[] secondSplit = splited[1].split(" ");
//
//            Integer.parseInt(firstSplit.substring(0, 2));
//            for (String title: targets) {
//                if (firstSplit[0].startsWith(title)) {
//                    int grade = Integer.parseInt(firstSplit[0]);
//                    String comment = firstSplit[1];
//                    String id = secondSplit[0];
//                    Date date = Date.from(formatter.parse(secondSplit[1]).toInstant());
//                    result.add(new Review(Source.NAVER, url, id, date, grade, comment));
//                }
//            }
        }
        return result;
    }
}
