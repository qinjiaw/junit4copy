package org.junit.junit4copy.runners.model;

/**
 * Created by qinjw on 2016/7/14.
 */
public interface RunnerScheduler {
    void schedule(Runnable childStatement);
    void finished();
}
