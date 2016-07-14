package org.junit.junit4copy.runner.notification;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by qinjw on 2016/7/14.
 */
public class RunListener {
    public void testRunStarted(Description description) throws Exception {}

    public void testRunFinished(Result result) throws Exception {}

    public void testStarted(Description description) throws Exception {}

    public void testFinished(Description description) throws Exception {}

    public void testFailure(Failure failure) throws Exception {}

    public void testIgnored(Description description) throws Exception {}

    public void testAssumptionFailure(Failure failure) {}

    @Documented
    @Target(ElementType.Type)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ThreadSafe {

    }
}
