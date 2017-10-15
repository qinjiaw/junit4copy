package org.junit.junit4copy.runners;

import com.sun.scenario.effect.Filterable;
import org.junit.junit4copy.AfterClass;
import org.junit.junit4copy.AssumptionViolatedException;
import org.junit.junit4copy.BeforeClass;
import org.junit.junit4copy.ClassRule;
import org.junit.junit4copy.runner.Description;
import org.junit.junit4copy.runner.Runner;
import org.junit.junit4copy.runner.manipulation.Sortable;
import org.junit.junit4copy.runner.manipulation.Sorter;
import org.junit.junit4copy.runners.model.FrameworkField;
import org.junit.junit4copy.runners.model.FrameworkMethod;
import org.junit.junit4copy.runners.model.TestClass;
import org.junit.junit4copy.validator.AnnotationsValidator;
import org.junit.junit4copy.validator.PublicClassValidator;
import org.junit.junit4copy.validator.TestClassValidator;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by qinjw on 2016/7/4.
 *
 * @author qinjw
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
            // do nothing
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
        validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
        validateClassRules(errors);
        applyValidators(errors);
    }

    private void applyValidators(List<Throwable> errors) {
        if (getTestClass().getJavaClass() != null) {
            for (TestClassValidator each : VALIDATORS) {
                errors.addAll(each.validateTestClass(getTestClass()));
            }
        }
    }

    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotatoin,
                                                  boolean isStatic, List<Throwable> errors) {
        List<FrameworkField> methods = getTestClass().getAnnotatedMethods(annotatoin);

        for (FrameworkField eachTestMethod : methods) {
            eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
        }
    }

    private void validateClassRules(List<Throwable> errors) {
        CLASS_RULE_VALIDATOR.validate(getTestClass(), errors);
        CLASS_RULE_METHOD_VALIDATOR.validate(getTestClass(), errors);
    }

    protected Statement classBlock(final RunNotifier notifier) {
        Statement statement = childrenInvoker(notifier);
        if (!areAllChildrenIgnored()) {
            statement = withBeforeClasses(statement);
            statement = withAfterClasses(statement);
            statement = withClassRules(statement);
        }
        return statement;
    }

    private boolean areAllChildrenIgnored() {
        for (T child : getFilteredChildren()) {
            if (!isIgnored(child)) {
                return false;
            }
        }

        return true;
    }

    protected Statement withBeforeClasses(Statement statement) {
        List<FrameworkMethod> befores = testClass.getAnnotatedMethods(BeforeClass.class);

        return befores.isEmpty() ? statement :
                new RunBefores(statement, befores, null);
    }

    protected Statement withAfterClasses(Statement statement) {
        List<FrameworkMethod> afters = testClass.getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? statement :
                new RunAfters(statement, afters, null);
    }

    private Statement withClassRules(Statement statement) {
        List<TestRule> classRules = classRules();

        return classRules.isEmpty() ? statement :
                new RunRules(statement, classRules, getDescription());
    }

    protected List<TestRule> classRules() {
        List<TestRule> result = testClass.getAnnotatedMethods(null, ClassRule.class, TestRule.class);
        result.addAll(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
        return result;
    }

    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
            public void evaluate() {
                runChildren(notifier);
            }
        };
    }

    protected boolean isIgnored(T child) {
        return false;
    }

    private void runChildren(final RunNotiFier notiFier) {
        final RunnerScheduler currentScheduler = notiFier;
        try {
            for (final T each : getFilteredChildren()) {
                currentScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        ParentRunner.this.runChild(each, notiFier);
                    }
                });
            }
        } finally {
            currentScheduler.finished();
        }
    }

    protected String getName() {
        return testClass.getName();
    }

    public final TestClass getTestClass() {
        return testClass;
    }

    protected final void runLeaf(Statement statement, Description description, RunNotifier notifier) {
        EachTestNotifier eachTestNotifier = new EachTestNotifier(notifier, description);
        eachTestNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            eachTestNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            eachTestNotifier.addFailure(e);
        } finally {
            eachTestNotifier.fireTestFinished();
        }
    }

    protected Annotation[] getRunnerAnnotations() {
        return testClass.getAnnotations();
    }

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(getName(),
                getRunnerAnnotations());
        for (T child getFilteredChildren()) {
            description.addChild(describeChild(child));
        }

        return description;
    }

    @Override
    public void run(final RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        try {
            Statement statement = classBlock(notifier);
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            testNotifier.addFailedAssumption(e);
        } catch (StopedByUserException e) {
            throw e;
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        }
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        synchronized (childrenLock) {
            List<T> children = new ArrayList<T>(getFilteredChildren());
            for (Iterable<T> iter = children.iterator(); iter.hasNext(); ) {
                T each = iter.next();
                if (shouldRun(filter, each)) {
                    try {
                        filter.apply(each);
                    } catch (NoTestsRemainException e) {
                        iter.remove();
                    }
                } else {
                    iter.remove();
                }
            }
            filteredChildren = Collections.unmodifiableCollection(children);
            if (filteredChildren.isEmpty()) {
                throw new NoTestsRemainException();
            }
        }
    }

    public void sort(Sorter sorter) {
        synchronized (childrenLock) {
            for (T each : getFilteredChildren()) {
                sorter.apply(each);
            }
            List<T> sortedChildren = new ArrayList<T>(getFilteredChildren());
            Collections.sort(sortedChildren, comparator(sorter));
            filteredChildren = Collections.unmodifiableCollection(sortedChildren);
        }
    }

    private void validate() throws InitalizationError {
        List<Throwable> errors = new ArrayList<Throwable>();
        collectInitializationErrors(errors);
        if (!errors.isEmpty()) {
            throw new InitializatioinError(errors);
        }
    }

    private Collection<T> getFilteredChildren() {
        if (filteredChildren == null) {
            synchronized (childrenLock) {
                if (filteredChildren == null) {
                    filteredChildren = Collections.unmodifiableCollection(getFilteredChildren());
                }
            }
        }
        return filteredChildren;
    }

    private boolean shouldRun(Filter filter, T each) {
        return filter.shouldRun(describeChild(each));
    }

    private Comparator<? extends T> comparator(final Sorter sorter) {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return sorter.compare(describeChild(o1), describeChild(o2));
            }
        };
    }

    public void setScheduler(RunnerScheduler scheduler) {
        this.scheduler = scheduler;
    }
}
