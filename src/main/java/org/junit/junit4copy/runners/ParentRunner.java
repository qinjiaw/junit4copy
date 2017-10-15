package org.junit.junit4copy.runners;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.org.glassfish.gmbal.Description;
import com.sun.scenario.effect.Filterable;
//import org.junit.junit4copy.Test;
import org.junit.junit4copy.BeforeClass;
import org.junit.junit4copy.runners.model.FrameworkField;
import org.junit.junit4copy.runners.model.TestClass;
import org.junit.junit4copy.validator.AnnotationsValidator;
import org.junit.junit4copy.validator.PublicClassValidator;
import org.junit.junit4copy.validator.TestClassValidator;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by qinjw on 2016/7/4.
 */
public abstract class ParentRunner<T> extends Runner implements Filterable, Sortable {
    private static final List<TestClassValidator> VALIDATORS =
    Arrays.asList(new AnnotationsValidator(), new PublicClassValidator());

    private final Object childrenLock = new Object();
    private final TestClass testClass;

    private volatile Collection<T> filteredChildren = null;

    private volatile RunnerScheduler scheduler = new RunnerScheduler() {
        public void schedule(Runnable childStatement) {
            childStatement.run();
        }
        public void finished() {

        }
    };

    protected ParentRunner(Class<?> testClass) throws InitializationError {
        this.testClass = creteTestClass(testClass);
        validate();
    }

    protected TestClass creteTestClass(Class<?> testClass) {
        return new TestClass(testClass);
    }

    protected abstract List<T> getChildren();
    protected abstract Description describeChild(T child);
    protected abstract void runChild(T child, RunNotifier notifier);
    protected void collectInitializationErrors(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
    }
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotatoin,
                                                  boolean isStatic, List<Throwable> errors) {
        List<FrameworkField> methods = getTestClass().getAnnotatedMethods(annotatoin);

        for (FrameworkField eachTestMethod : methods) {
            eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
        }
    }
}
