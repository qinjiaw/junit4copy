package org.junit.junit4copy.runners.model;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by qinjw on 2016/6/23.
 */
public abstract class FrameworkMember<T extends FrameworkMember<T>> implements Annotatable {
    abstract  boolean isShadowedBy(T otherMember);

    boolean isShadowedBy(List<T> members) {
        for (T each : members) {
            if (isShadowedBy(each)) {
                return true;
            }
        }
        return false;
    }

    protected abstract int getModifiers();

    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }
    public abstract  String getName();
    public abstract Class<?> getType();
    public abstract Class<?> getDeclaringClass();
}
