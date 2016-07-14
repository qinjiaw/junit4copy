package org.junit.junit4copy.runner.notification;

/**
 * Created by lenovo on 2016/7/14.
 */
public class SynchronizedRunListener extends RunListener {
    private final RunListener listener;
    private final Object monitor;

    SynchronizedRunListener(RunListener listener, Object monitor) {
        this.listener = listener;
        this.monitor = monitor;
    }
    public void testRunStarted(Description description) throws Exception {
        synchronized (monitor) {
            listener.testRunStarted(description);
        }
    }

    public void testRunFinished(Result result) throws Exception {
        synchronized (monitor) {
            listener.testRunFinished(description);
        }
    }

    public void testStarted(Description description) throws Exception {
        synchronized (monitor) {
            listener.testStarted(description);
        }
    }

    public void testFinished(Description description) throws Exception {
        synchronized (monitor) {
            listener.testFinished(description);
        }
    }

    public void testFailure(Failure failure) throws Exception {
        synchronized (monitor) {
            listener.testFailure(failure);
        }
    }

    public void testAssumtionfailure(Failure failure) {
        synchronized (monitor) {
            listener.testAssumptionFailure(failure);
        }
    }

    public void testIgnored(Description description) throws Exception {
        synchronized (monitor) {
            listener.testIgnored(description);
        }
    }

    public int hashCode() {
        return listener.hashCode();
    }

    public boolean equals(Object other) {
        if (this == null) {
            return false;
        }
        if (!(other instanceof SynchronizedRunListener)) {
            return false;
        }
        SynchronizedRunListener that = (SynchronizedRunListener) other;
        return listener.equals(that.listener);
    }

    public String toString() {
        return listener.toString() + " (with sychronization wrapper)";
    }
}
