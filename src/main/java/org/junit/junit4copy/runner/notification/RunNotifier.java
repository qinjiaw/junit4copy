package org.junit.junit4copy.runner.notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by qinjw on 2016/7/14.
 */
public class RunNotifier {
    private final List<RunListener> listeners = new CopyOnWriteArrayList<RunListener>();
    private volatile boolean pleaseStop = false;
    public void addListener(RunListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot add a null listener");
        }
        listeners.add(wrapIfNotThreadSafe(listener));
    }

    public void removeListener(RunListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot remove a null listener");
        }
        listeners.remove(wrapIfNotThreadSafe(listener));
    }

    RunListener wrapIfNotThreadSafe(RunListener listener) {
        return listener.getClass().isAnnotationPresent(RunListener.ThreadSafe.class) ? listener : new SynchronizeRunListerner(listener, this);
    }

    private abstract class SafeNotifier {
        private final List<RunListener> currentListeners;
        SafeNotifier() {
            this(listeners);
        }

        SafeNotifier(List<RunListener> currentListeners) {
            this.currentListeners = currentListeners;
        }

        void run() {
            int capacity = currentListeners.size();
            List<RunListener> safeListeners = new ArrayList<RunListener>(capacity);
            List<Failure> failures = new ArrayList<Failure>(capacity);
            for (RunListener listener : currentListeners) {
                try {
                    notifyListener(listener);
                    safeListeners.add(listener);
                } catch (Exception e) {
                    failures.add(new Failure(Desciption.TEST_MECHANISM, e));
                }
            }
            fireTestFailures(safeListeners, failures);
        }

        abstract protected void notifyListener(RunListener each) throws Exception;
    }

    public void fireTestRunStarted(final Desciption desciption) {
        new SafeNotifier() {
            protected void notifyListener(RunListener each) throws Exception {
                each.testRunStarted(desciption);
            }
        }.run();
    }

    public void fireTestRunFinished(final Result result) {
        new SafeNotifier() {
            protected void notifyListener(RunListener each) throws Exception {
                each.testRunFinished(result);
            }
        }.run();
    }

    public void fireTestStarted(final Desciption desciption) throws StoppedByUserException {
        if (pleaseStop) {
            throw new StoppedByUserException();
        }
        new SafeNotifier() {
            protected void notityListener(RunListener each) throws Exception {
                each.testStarted(desciption);
            }
        }.run();
    }

    public void fireTestFailures(Failure failure) {
        fireTestFailures(listeners, asList(failure));
    }

    private void fireTestFailures(Failure failure) {
        fireTestFailures(listeners, asList(failure));
    }

    private void fireTestFailures(List<RunListener> listeners, final List<Failure> failures) {
        if (!failures.isEmpty()) {
            new SafeNotifier(listeners) {
                protected void notifyListener(RunListener listener) throws Exception {
                    for (Failure each : failures) {
                        listener.testFailure(each);
                    }
                }
            }.run();
        }
    }

    public void fireTestAssumptionFailed(final Failure failure) {
        new SafeNotifier() {
            protected void notityListener(RunListener each) throws Exception {
                each.testAssumptionFialure(failure);
            }
        }.run();
    }

    public void fireTestIgnored(final Description description) {
        new SafeNotifier() {
            protected void notityListener(RunListener each) throws Exception {
                each.testIgnored(description);
            }
        }.run();
    }

    public void fireTestFinished(final Description description) {
        new SafeNotifier() {
            protected void notityListener(Runlistener each) throws Exception {
                each.testFinished(description);
            }
        }.run();
    }

    public void pleaseStop() {
        pleaseStop = true;
    }

    public void addFirstListener(RunListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot add a null listener");
        }
        listeners.add(0, wrapIfNotThreadSafe(listener));
    }
}


