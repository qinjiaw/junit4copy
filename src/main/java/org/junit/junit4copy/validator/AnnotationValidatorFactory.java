package org.junit.junit4copy.validator;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qinjw on 2016/7/1.
 */
public class AnnotationValidatorFactory {

    private static final ConcurrentHashMap<ValidateWith, AnnotationValidator> VALIDATORS_FOR_ANNOTATION_TYPES =
            new ConcurrentHashMap<ValidateWith, AnnotationValidator>();

    public AnnotationValidator createAnnotationValidator(ValidateWith validateWithAnnotation) {
        AnnotationValidator validator = VALIDATORS_FOR_ANNOTATION_TYPES.get(validateWithAnnotation);
        if (validator != null) {
            return validator;
        }
        Class<? extends AnnotationValidator> clazz = validateWithAnnotation.value();
        try {
            AnnotationValidator annotationValidator = clazz.newInstance();
            VALIDATORS_FOR_ANNOTATION_TYPES.putIfAbsent(validateWithAnnotation, annotationValidator);
            return VALIDATORS_FOR_ANNOTATION_TYPES.get(validateWithAnnotation);
        } catch (Exception e) {
            throw new RuntimeException("Exception received when creating AnnotationValidator class " + clazz.getName(), e);
        }
    }
}
