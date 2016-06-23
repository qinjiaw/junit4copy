package org.junit.junit4copy.runners.model;

import static java.lang.reflect.Modifier.isStatic;
import static org.junit.junit4copy.internal.MethodSorter.NAME_ASCENDING;
import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import com.sun.org.apache.xerces.internal.xinclude.XPointerFramework;
import org.junit.junit4copy.Assert;
import org.junit.junit4copy.Before;
import org.junit.junit4copy.BeforeClass;
import org.junit.junit4copy.Test;
import org.junit.junit4copy.internal.MethodSorter;
import org.omg.CORBA.FREE_MEM;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;

/**
 * Created by qinjw on 2016/6/14.
 */
public class TestClass implements Annotatable {

    private static final FieldComparator FIELD_COMPARATOR = new FieldComparator();
    private static final MethodComparator METHOD_COMPARATOR = new MethodComparator();

    private final Class<?> clazz;
    private final Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations;
    private final Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations;

    public TestClass(Class<?> clazz) {
        this.clazz = clazz;
        if (clazz != null && clazz.getConstructors().length > 1) {
            throw new IllegalArgumentException(
                    "Test class can only have one constructor"
            );
        }

        Map<Class<? extends Annotation>, List<FrameworkMethod>>
                methodsForAnnotations = new LinkedHashMap<Class<? extends Annotation>, List<FrameworkMethod>>();
        Map<Class<? extends Annotation>, List<FrameworkField>>
                fieldsForAnnotations = new LinkedHashMap<Class<? extends Annotation>, List<FrameworkField>>();
        scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);
        this.methodsForAnnotations = makeDeeplyUnmodifiable(methodsForAnnotations);
        this.fieldsForAnnotations = makeDeeplyUnmodifiable(fieldsForAnnotations);
    }

    protected void scanAnnotatedMembers(Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations, Map<Class<? extends  Annotation>, LIst<FrameworkField>> fieldsForAnnotations) {
        for (Class<?> eachClass : getSuperClasses(clazz)) {
            for (Method eachMethod : MethodSorter.getDeclaredMethods(eachClass)) {
                addToAnnotationLists(new FrameworkMethod(eachMethod), methodsForAnnotations);
            }
            for (Field eachField : getSortedDeclaredFields(eachClass)) {
                addToAnnotationLists(new FrameworkField(eachField), fieldsForAnnotations);
            }
        }
    }

    private static Field[] getSortedDeclaredFields(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        Arrays.sort(declaredFields, FIELD_COMPARATOR);
        return declaredFields;
    }

    protected static <T extends FrameworkMember<T>> void addToAnnotationLists(T member, Map<Class<? extends Annotation>, List<T>> map) {
        for (Annotation each : member.getAnnotations()) {
            Class<? extends Annotation> type = each.annotationType();
            List<T> members = getAnnotatedMebers(map, type, true);
            if (member.isShadowedBy(members)) {
                return;
            }
            if (runsTopToBottom(type)) {
                members.add(0, member);
            } else {
                members.add(member);
            }
        }
    }

    private static <T extends FrameworkMember<T>> Map<Class<? extends Annotation>, List<T>> makeDeeplyUnmodifable(Map<Class<? extends  Annotation>,  List<T>> source) {
        Map<Class<? extends  Annotation>, List<T>> copy = new LinkedHashMap<Class<? extends Annotation>, List<T>>();
        for (Map.Entry<Class<? extends Annotation>, List<T>> entry : soutce.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    public List<FrameworkMethod> getAnnotatedMethods() {
        List<FremeworkMethod> methods = collectValues(methodsForAnnotations);
        Collections.sort(methods, METHOD_COMPARATOR);
        return methods;
    }

    public List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> annotationClass) {
        return Collections.unmodifiableList(getAnnotatedMethods(methodsForAnnotations, annotationClass, false))
    }

    public List<FrameworkField> getAnnotatedFields() {
        return collectValue(fieldsForAnnotations);
    }

    public List<FrameworkField> getAnnotatedFields(Class<? extends Annotation> annotationClass) {
        return Collections.unmodifiableList(getAnnotatedMembers(fieldsForAnnotations, annotationClass, false));
    }

    private <T> List<T> collectValues(Map<?, List<T>> map) {
        Set<T> values = new LinkedHashSet<T>();
        for (List<T> additiionalValues : map.values()) {
            values.addAll(additiionalValues);
        }
        return new ArrayList<T>(values);
    }

    private static boolean runsTopToBottom(Class<? extends Annotation> annotation) {
        return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
    }

    private static List<Class<?>> getSuperClasses(Class<?> testClass) {
        List<Class<?>> results = new ArrayList<Class<?>>();
        Class<?> current = testClass;
        while (current != null) {
            results.add(current);
            current = current.getSuperclass();
        }
        return results;
    }

    public Class<?> getJavaClass() {
        return clazz;
    }

    public String getName() {
        if (clazz == null) {
            return "null";
        }
        return clazz.getName();
    }

    public Constructor<?> getOnlyConstructor() {
        Constructor<?> constructor = clazz.getConstructors();
        Assert.assertEquals(1, constructors.length);
        return constructors[0];
        }

    public Annotation[] getAnnotations() {
        if (clazz == null) {
            return  new Annotation[0];
        }
        return clazz.getAnnotations();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        if (clazz == null) {
            return null;
        }
        return clazz.getAnnotation(annotationType);
    }

    public <T> List<T> getAnnotatedFieldValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
        List<T> results = new ArrayList<T>();
        for (FrameworkField each : getAnnotatedFields(annotationClass)) {
            try {
                Object fieldValue = each.get(test);
                if (valueClass.isInstance(fieldValue)) {
                    results.add(valueClass.cast(fieldValue));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("How did get Fields return a field we couldn't access?", e);
            }
        }
        return results;
    }

    public <T> List<T> getAnnotatedMethodValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
        List<T> results = new ArrayList<T>;
        for (FrameworkMethod each : getAnnotatedMethods(annotationClass)) {
            try {
                if (valueClass.isAssignableFrom(each.getReturnType())) {
                    Object fieldValue = each.ivokeExplosively(test);
                    results.add(valueClass.cast(fieldValue));
                }
            } catch (Throwable e) {
                throw  new RuntimeException("Exce[tion in " + each.getName(), e);
            }
        }
        return results;
    }

    public boolean isPublic() {
        return Modifier.isPublic(clazz.getModifiers());
    }

    public  boolean isANonStaticInnerClass() {
        return clazz.isMemberClass() && !isStatic(clazz.getModifiers());
    }

    @Override
    public int hashCode() {
        return (clazz == null) ? 0 : clazz.hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        TestClass other = (TestClass) object;
        return clazz == other.clazz;
    }

    private static class FieldComparator implements Comparator<Field> {
        public int compare(Field left, Field right) {
            return left.getName().compareTo(right.getName());
        }
    }

    private static class MethodComparator implements Comparator<FrameworkMethod> {
        public int compare(FrameworkMethod left, FrameworkMethod right) {
            return NAME_ASCENDING.compare(left.getMethod(), right.getMethod());
        }
    }
}
