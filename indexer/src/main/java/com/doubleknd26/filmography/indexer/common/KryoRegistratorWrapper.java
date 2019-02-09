package com.doubleknd26.filmography.indexer.common;

import com.doubleknd26.filmography.proto.FilmInfo;
import com.doubleknd26.filmography.proto.Review;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Parser;
import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.serializer.KryoRegistrator;

import java.lang.reflect.Method;

/**
 * Created by Kideok Kim on 2019-02-04.
 */
public class KryoRegistratorWrapper implements KryoRegistrator {
    @Override
    public void registerClasses(Kryo kryo) {
        kryo.register(FilmInfo.class, new ProtobufSerializer());
        kryo.register(Review.class, new ProtobufSerializer());
        kryo.register(SolrInputDocument.class, new JavaSerializer());
    }

    public class ProtobufSerializer<T extends AbstractMessage> extends Serializer<T> {
        @Override
        public void write(Kryo kryo, Output output, T object) {
            output.writeInt(object.toByteArray().length);
            output.write(object.toByteArray());
        }

        @Override
        public T read(Kryo kryo, Input input, Class<T> type) {
            try {
                Method parserMethod = type.getDeclaredMethod("parser");
                Parser<T> parser = (Parser<T>) parserMethod.invoke(null);
                int length = input.readInt();
                byte[] bytes = input.readBytes(length);
                return parser.parseFrom(bytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
