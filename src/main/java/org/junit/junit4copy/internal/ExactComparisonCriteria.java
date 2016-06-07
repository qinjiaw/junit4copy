package org.junit.junit4copy.internal;

import org.junit.junit4copy.Assert;

public class ExactComparisonCriteria extends ComparisonCriteria {

	@Override
	protected void assertElementsEqual(Object expected, Object actual) {
		Assert.assertEquals(expected, actual);
	}
}
