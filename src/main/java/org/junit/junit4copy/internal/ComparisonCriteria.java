package org.junit.junit4copy.internal;

import java.lang.reflect.Array;
import java.util.Arrays;

import junit.framework.Assert;

public abstract class ComparisonCriteria {
	
	public void arrayEquals(String message, Object expecteds, Object actuals) throws ArrayComparisonFailure {
		arrayEquals(message, expecteds, actuals, true);
	}
	
	private void arrayEquals(String message, Object expecteds, Object actuals, boolean outer) throws ArrayComparisonFailure {
		if (expecteds == actuals || Arrays.deepEquals(new Object[] {expecteds}, new Object[] {actuals})) {
			return;
		}
		String header = message == null ? "" : message + ": ";
		
		String exceptionMessage = outer ? header : "";
		int expectedsLength = assertArraysAreSameLength(expecteds, actuals, exceptionMessage);
		
		for (int i = 0; i < expectedsLength; i++) {
			Object expected = Array.get(expecteds, i);
			Object actual = Array.get(actuals, i);
			
			if (isArray(expected) && isArray(actual)) {
				try {
					
				} catch (ArrayComparisonFailure e) {
					e.addDimension(i);
					throw e;
				} catch (AssertionError e) {
					throw new ArrayComparisonFailure(header, e, i);
				}	
			} else {
				try {
					assertElementsEqual(expected, actual);
				} catch (AssertionError e) {
					throw new ArrayComparisonFailure(header, e, i);
				}
			} 
		}
	}
	
	private boolean isArray(Object expected) {
		return expected != null && expected.getClass().isArray();
	}
	
	private int assertArraysAreSameLength(Object expecteds, Object actuals, String header) {
		if (expecteds == null) {
			Assert.fail(header + "expected array was null");
		}
		if (actuals == null) {
			Assert.fail(header + "actual array was null");
		}
		int actualsLength = Array.getLength(actuals);
		int expectedsLength = Array.getLength(expecteds);
		if (actualsLength != expectedsLength) {
			Assert.fail(header + "array lengths differed, expected.length=" + expectedsLength + " actual.length=" + actualsLength);
		}
		return expectedsLength;
	}
	
	protected abstract void assertElementsEqual(Object expected, Object actual);
	
	
	
	
	
	
	
	
	
	
	
}
