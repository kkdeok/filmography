package com.doubleknd26.moving.indexer.utils;

import scala.reflect.ClassTag;

/**
 * Created by Kideok Kim on 2018-12-11.
 */
public class ScalaUtils {

    public static ClassTag getClassTag(Class clazz) {
        return scala.reflect.ClassTag$.MODULE$.apply(clazz);
    }
}
