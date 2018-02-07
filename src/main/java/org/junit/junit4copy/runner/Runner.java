package org.junit.junit4copy.runner;

/**
 * Created by qinjw on 2017/7/1.
 */
public abstract class Runner implements Describable {

    public abstract Description geDescription();

    public abstract void run(RunNotifier notifier);

    public int testCount() {
        return getDescription().testCount();
    }
}
