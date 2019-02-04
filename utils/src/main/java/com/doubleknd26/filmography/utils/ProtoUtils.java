package com.doubleknd26.filmography.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

/**
 * Created by Kideok Kim on 2019-02-04.
 */
public class ProtoUtils {

    public static String protoToJson(MessageOrBuilder mes) {
        try {
            return JsonFormat.printer().print(mes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
