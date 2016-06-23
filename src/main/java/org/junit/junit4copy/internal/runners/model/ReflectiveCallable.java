package org.junit.junit4copy.internal.runners.model;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by  qinjw on 2016/6/23.
 */
public abstract class ReflectiveCallable {
    public Object run() throws Throwable {
        try {
            return runReflectiveCall();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    protected abstract Object runReflectiveCall() throws Throwable;
}
