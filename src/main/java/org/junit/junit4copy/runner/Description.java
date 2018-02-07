package org.junit.junit4copy.runner;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qinjw on 2017/7/1.
 * @author qinjw
 */
public class Description implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Pattern METHOD_AND_CLASS_NAME_PATTERN =
            Pattern.compile("([\\s\\S]*)\\((.*)\\)");

    public static Description createSuiteDescription(String name, Annotation... annotations) {
        return new Description(null, name, annotations);
    }

    public static Description createSuiteDescription(String name, Serializable uniqueId, Annotation... annotations) {
        return new Description(null, name, uniqueId, annotations);
    }

    public static Description createSuiteDescription(String className, String name, Annotation... annotations) {
        return new Description(null, formatDisplayName(name, className), annotations);
    }

    public static Description createSuiteDescription(Class<?> clazz, String name, Annotation... annotations) {
        return new Description(null, formatDisplayName(name, clazz.getName()), annotations);
    }

    public static Description createSuiteDescription(Class<?> clazz, String name) {
        return new Description(null, formatDisplayName(name, clazz.getName()));
    }

    public static Description createSuiteDescription(String className, String name, Serializable uniqueId) {
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

    private final Collection<Description> fChildren = new ConcurrentLinkedDeque<Description>();
    private final String fDisplayName;
    private final Serializable fUniqueId;
    private final Annotation[] fAnnotations;
    private volatile Class<?> fTestClass;

    private Description(Class<?> clazz, String displayName, Annotation... annotations) {
        this(clazz, displayName, displayName, annotations);
    }

    private Description(Class<?> testClass, String displayName, Serializable uniqueId, Annotation... annotations) {
        if ((displayName == null) || (displayName.length() == 0)) {
            throw new IllegalArgumentException(
                    "The display name must not be empty."
            );
        }
        if ((uniqueId == null)) {
            throw new IllegalArgumentException(
                    "The unique Id must not be null."
            );
        }
        this.fTestClass = testClass;
        this.fDisplayName = displayName;
        this.fUniqueId = uniqueId;
        this.fAnnotations = annotations;
    }

    public String getfDisplayName() {
        return fDisplayName;
    }

    public void addChild(Description description) {
        fChildren.add(description);
    }

    public ArrayList<Description> getChildren() {
        return new ArrayList<>(fChildren);
    }

    public boolean isSuite() {
        return !isTest();
    }

    public boolean isTest() {
        return fChildren.isEmpty();
    }

    public int testCount() {
        if (isTest()) {
            return 1;
        }
        int result = 0;
        for (Description child : fChildren) {
            result += child.testCount();
        }
        return result;
    }

    @Override
    public int hashCode() {
        return fUniqueId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Description)) {
            return false;
        }
        Description d = (Description) obj;
        return fUniqueId.equals(d.fUniqueId);
    }

    @Override
    public String toString() {
        return getfDisplayName();
    }

    public boolean isEmpty() {
        return equals(EMPTY);
    }

    public Description childlessCopy() {
        return new Description(fTestClass, fDisplayName, fAnnotations);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        for (Annotation each : fAnnotations) {
            if (each.annotationType().equals(annotationType)) {
                return annotationType.cast(each);
            }
        }
        return null;
    }

    public Collection<Annotation> getAnnotations() {
        return Arrays.asList(fAnnotations);
    }

    public Class<?> getfTestClass() {
        if (fTestClass != null) {
            return fTestClass;
        }

        String name = getClassName();
        if (name == null) {
            return null;
        }
        try {
            fTestClass = Class.forName(name, false, getClass().getClassLoader());
            return fTestClass;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public String getClassName() {
        return fTestClass != null ? fTestClass.getName() : methodAndClassNamePatternGroupOrDefault(2, toString());
    }

    public String getMethodName() {
        return methodAndClassNamePatternGroupOrDefault(1, null);
    }

    private String methodAndClassNamePatternGroupOrDefault(int group, String defaultString) {
        Matcher matcher = METHOD_AND_CLASS_NAME_PATTERN.matcher(toString());
        return matcher.matches() ? matcher.group(group) : defaultString;



    }
}
