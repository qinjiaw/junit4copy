package org.junit.junit4copy.runner;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

/**
 * Created by qinjw on 2016/7/14.
 */
public class Description implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Pattern METHOD_AND_CLASS_NAME_PATTERN = Pattern.compile("([\\s\\S]*)\\((.*)\\)");

    public static Description createSuiteDescription(String name, Annotation... annotations) {
        return new Description(null, name, annotations);
    }

    public static Description createSuiteDescription(String name, Serializable uniqueId, Annotation... annotations) {
        return new Description(null, name, uniqueId, annotations);
    }

    public static Description createTestDescription(String className, String name, Annotation... annotations) {
        return new Description(null, formatDisplayName(name, className), annotations);
    }

    public static Description createTestDescription(Class<?> clazz, String name, Annotation... annotations) {
        return new Description(clazz, formatDisplayName(name, clazz.getName()), annotations);
    }

    public static Description createTestDescription(Class<?> clazz, String name) {
        return new Description(clazz, formatDisplayName(name, clazz.getName()));
    }

    public static createTestDescription(String className, String name, Serializable uniqueId) {
        return new Description(null, formatDisplayName(name, className), uniqueId);
    }

    private static String formatDisplayName(String name, String className) {
        return String.format("%s(%s)", name, className);
    }

    public static Description createSuiteDescription(Class<?> testClass) {
        return new Description(testClass, testClass.getName(), testClass.getAnnotations());
    }

    public static final Description EMPTY = new Description(null, "No Tests");

    public static final Description TEST_MECHANISM = new Description(null, "Test mechanism");
    private final Collection<Description> fChildren = new ConcurrentLinkedQueue<Description>();
    private final String fDisplayName;
    private final Serializable fUniqueId;
    private final Annotation[] fAnnotations;
    private volatile Class<?> fTestClass;
}
