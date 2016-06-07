package org.junit.junit4copy.internal;

import junit.framework.Assert;

public class InexactComparisonCriteria extends ComparisonCriteria {
	public Object fDelta;
	
	public InexactComparisonCriteria(double delta) {
		fDelta = delta;
	}
	
	public InexactComparisonCriteria(float delta) {
		fDelta = delta;
	}
	
	@Override
	protected void assertElementsEqual(Object expected, Object actual) {
		if (expected instanceof Double) {
			Assert.assertEquals((Double) expected, (Double) actual, (Double) fDelta);
		} else {
			Assert.assertEquals((Float) expected, (Float) actual, (Double) fDelta);
		}
		
	}

}
