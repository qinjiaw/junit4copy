package org.junit.junit4copy.validator;

import org.junit.junit4copy.runners.model.TestClass;

import java.util.List;

/**
 * Created by qinjw on 2016/6/14.
 */
public interface TestClassValidator {
    List<Exception> validateTestClass(TestClass testClass);
}
