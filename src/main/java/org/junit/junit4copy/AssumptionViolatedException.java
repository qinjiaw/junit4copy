package org.junit.junit4copy;

import org.hamcrest.Matcher;

public class AssumptionViolatedException extends org.junit.junit4copy.internal.AssumptionViolatedException {

	private static final long serialVersionUID = 1L;
	
	@Deprecated
	public <T> AssumptionViolatedException(T actual, Matcher<T> matcher) {
		super(actual, matcher);
	}
	
	@Deprecated
	public <T> AssumptionViolatedException(String message, T expected, Matcher<T> matcher) {
		super(message, expected, matcher);
	}
	
	public AssumptionViolatedException(String message) {
		super(message);
	}
	
	public AssumptionViolatedException(String assumption, Throwable t) {
		super(assumption, t);
	}
}
