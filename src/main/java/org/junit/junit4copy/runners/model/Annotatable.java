package org.junit.junit4copy.runners.model;

import java.lang.annotation.Annotation;

/**
 * Created by lenovo on 2016/6/14.
 */
public interface Annotatable {
    Annotation[] getAnnotations();

    <T extends Annotation> T getAnnotation(Class<T> annotationType);
}
