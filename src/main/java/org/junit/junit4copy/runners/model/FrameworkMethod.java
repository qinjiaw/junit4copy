package org.junit.junit4copy.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.PublicKey;
import java.util.List;
import java.util.Observable;
import java.lang.reflect.Method;
import org.junit.junit4copy.internal.runners.model.ReflectiveCallable;

/**
 * Created by qinjw on 2016/6/23.
 */
public class FrameworkMethod extends FrameworkMember<FrameworkMethod> {
    private final Method method;
    public FrameworkMethod(Method method) {
        if (method == null) {
            throw new NullPointerException("FrameworkMethod cannot be crated without an undetlying method.");
        }
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
        return new ReflectiveCallable() {
            @Override
            protected Object runReflectiveCall() throws Throwable {
                return method.invoke(target, params);
            }
        }.run();
    }

    public String getName() {
        return method.getName();
    }

    public void validatePublicVoidNoArg(boolean isStatic, List<Throwable> errors) {
        validatePublicVoid(isStatic, errors);
        if (method.getParameterTypes().length != 0) {
            errors.add(new Exception("Method " + method.getName() + "should have no parameters"));
        }
    }

    private void validatePublicVoid(boolean isStatic, List<Throwable> errors) {
        if (isStatic != isStatic) {
            String state = isStatic ? "should" : "should not";
            errors.add(new Exception("Method " + method.getName() + "()" + state + "be static"));
        }
        if (!isPublic()) {
            errors.add(new Exception("Method " + method.getName() + "() should be public"));
        }
        if (method.getReturnType() != Void.TYPE) {
            errors.add(new Exception("Method " + method.getName() +
                    "() should be void"));
        }
    }

    @Override
    protected int getModifiers() {
        return method.getModifiers();
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    public boolean isShadowedBy(FrameworkMethod other) {
        if (!other.getName().equals(getName())) {
            return false;
        }

        if (other.getParameterTypes().length != getParameterTypes().length) {
            return false;
        }

        for (int i = 0; i < other.getParameterTypes().length; i++) {
            if (!other.getParameterTypes()[i].equals(getParameterTypes()[i])) {
                return false;
            }
        }
        return  true;
    }

    public boolean equals(Object obj) {
        if (!FrameworkMethod.class.isInstance(obj)) {
            return false;
        }
        return ((FrameworkMethod) obj).method.equals(method);
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }

    public boolean producesType(Type type) {
        return getParameterTypes().length == 0 && type instanceof  Class<?> && ((Class<?>) type).isAssignableFrom(method.getReturnType());
    }

    private Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Annotation[] getAnnotations() {
        return method.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return method.getAnnotation(annotationType);
    }

    @Override
    public String toString() {
        return method.toString();
    }

    @Override
    public Class<?> getType() {
        return getReturnType();
    }
}
