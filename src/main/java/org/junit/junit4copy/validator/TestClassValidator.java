package org.junit.junit4copy.validator;

import java.util.List;

/**
 * Created by qinjw on 2016/6/14.
 */
public interface TestClassValidator {
    List<Exception> alidateTestClass(TestClass testClass);
}
