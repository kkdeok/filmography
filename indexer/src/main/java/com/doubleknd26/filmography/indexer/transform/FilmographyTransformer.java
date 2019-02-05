package com.doubleknd26.filmography.indexer.transform;

import com.doubleknd26.filmography.proto.FilmInfo;
import com.doubleknd26.filmography.proto.Review;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.doubleknd26.filmography.utils.ProtoUtils.protoToJson;

/**
 * Created by Kideok Kim on 2018-12-08.
 */
public class FilmographyTransformer implements Serializable {

    public JavaRDD<SolrInputDocument> transform(JavaRDD<FilmInfo> filmInfoRdd, JavaRDD<Review> reviewRdd) {
        JavaPairRDD<String, Iterable<Review>> transformedReview = transformReview(reviewRdd);
        JavaPairRDD<String, FilmInfo> transformedFilmInfo = transformFilmInfo(filmInfoRdd);
        return transformedFilmInfo
                .rightOuterJoin(transformedReview)
                .flatMapValues(new Function<Tuple2<Optional<FilmInfo>, Iterable<Review>>, Iterable<FilmInfo>>() {
                    @Override
                    public Iterable<FilmInfo> call(Tuple2<Optional<FilmInfo>, Iterable<Review>> tuple) throws Exception {
                        Optional<FilmInfo> filmInfo = tuple._1();
                        if (filmInfo.isPresent()) {
                            FilmInfo.Builder builder = filmInfo.get().toBuilder();
                            int reviewCount = 0;
                            float gradeSum = 0f;
                            for (Review review : tuple._2()) {
                                builder.addReviews(review);
                                reviewCount++;
                                gradeSum += review.getGrade();
                            }
                            builder.setAvgGrade(gradeSum / reviewCount);
                            return Collections.singletonList(builder.build());
                        } else {
                            return Collections.emptyList();
                        }
                    }
                }).mapValues(new Function<FilmInfo, SolrInputDocument>() {
                    @Override
                    public SolrInputDocument call(FilmInfo v1) throws Exception {
                        ObjectMapper mapper = new ObjectMapper();
                        String jsonString = protoToJson(v1);
                        JsonNode root = mapper.readTree(jsonString);
                        return generateSolrDoc(root);
                    }
                }).values();
    }

    private SolrInputDocument generateSolrDoc(JsonNode root) {
        SolrInputDocument doc = new SolrInputDocument();
        Iterator<Map.Entry<String, JsonNode>> it = root.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> entry = it.next();
            String fieldName = entry.getKey();
            if (!fieldName.equals("reviews")) {
                Object fieldValue = convertFieldValue(entry.getValue());
                doc.setField(fieldName, fieldValue);
                if (fieldName.equals("title")) {
                    doc.setField("_root_", fieldValue);
                }
            } else {
                List<SolrInputDocument> childDocs = Lists.newArrayList();
                Iterator<JsonNode> childIter = entry.getValue().elements();
                while (childIter.hasNext()) {
                    childDocs.add(generateSolrDoc(childIter.next()));
                }
                doc.addChildDocuments(childDocs);
            }
        }
        return doc;
    }

    private Object convertFieldValue(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isLong()) {
            return node.longValue();
        } else if (node.isInt()) {
            return node.intValue();
        } else if (node.isFloat()) {
            return node.floatValue();
        } else if (node.isDouble()) {
            return node.doubleValue();
        } else if (node.isBoolean()) {
            return node.booleanValue();
        } else if (node.isArray()) {
            Iterator<JsonNode> it = node.elements();
            List<Object> list = Lists.newArrayList();
            while (it.hasNext()) {
                list.add(convertFieldValue(it.next()));
            }
            return list;
        } else {
            throw new RuntimeException("Unhandled field type: " + node.toString());
        }
    }

    private JavaPairRDD<String, Iterable<Review>> transformReview(JavaRDD<Review> reviewRdd) {
        return reviewRdd.groupBy(new Function<Review, String>() {
            @Override
            public String call(Review review) throws Exception {
                return review.getTitle();
            }
        });
    }

    private JavaPairRDD<String, FilmInfo> transformFilmInfo(JavaRDD<FilmInfo> filmInfoRdd) {
        return filmInfoRdd.mapToPair(new PairFunction<FilmInfo, String, FilmInfo>() {
            @Override
            public Tuple2<String, FilmInfo> call(FilmInfo filmInfo) throws Exception {
                return new Tuple2<>(filmInfo.getTitle(), filmInfo);
            }
        });
    }
}
