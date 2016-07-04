package org.junit.junit4copy.validator;

import org.junit.junit4copy.runners.model.FrameworkField;
import org.junit.junit4copy.runners.model.FrameworkMethod;
import org.junit.junit4copy.runners.model.TestClass;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

/**
 * Created by qinjw on 2016/7/1.
 */
public class AnnotationValidator {
    private static final List<Exception> NO_VALIDATION_ERRORS = emptyList();

    public List<Exception> validateAnnotatedClass(TestClass testClass) {
        return NO_VALIDATION_ERRORS;
    }

    public List<Exception> validateAnnotatedField(FrameworkField field) {
        return NO_VALIDATION_ERRORS;
    }

    public List<Exception> validateAnnotatedMethod(FrameworkMethod method) {
        return NO_VALIDATION_ERRORS;
    }
}
