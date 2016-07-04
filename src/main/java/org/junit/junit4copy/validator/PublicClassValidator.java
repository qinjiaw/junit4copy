package org.junit.junit4copy.validator;

import org.junit.junit4copy.runners.model.TestClass;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by qinjw on 2016/7/4.
 */
public class PublicClassValidator {
    private static final List<Exception> NO_VALIDATION_ERRORS = Collections.emptyList();

    public List<Exception> validateTestClass(TestClass testClass) {
        if (testClass.isPublic()) {
            return NO_VALIDATION_ERRORS;
        } else {
            return Collections.singletonList(new Exception("The class " + testClass.getName() + " is not public."));
        }
    }
}
