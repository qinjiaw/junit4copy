package org.junit.junit4copy.validator;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by qinjw on 2016/7/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ValidateWith {
    Class<? extends AnnotationValidator> value();
}
