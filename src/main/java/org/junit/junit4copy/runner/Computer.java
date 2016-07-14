package org.junit.junit4copy.runner;

import org.junit.junit4copy.runners.model.InitializationError;

/**
 * Created by qinjw on 2016/7/14.
 */
public class Computer {
    public static Computer serial() {
        return new Computer();
    }

    public Runner getSuite(final RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        return new Suite(new RunnerBuilder() {
            public Runner runnerForClass(Class<?> testClass) throws Throwable {
                return getRunner(builder, testClass);
            }
        }, classes);
    }

    protected Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {
        return builder.runnerForClass(testClass);
    }
}
