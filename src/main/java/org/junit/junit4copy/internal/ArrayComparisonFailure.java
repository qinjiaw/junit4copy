package org.junit.junit4copy.internal;

import java.util.ArrayList;
import java.util.List;

public class ArrayComparisonFailure extends AssertionError {

	private static final long serialVersionUID = 1L;
	
	private final List<Integer> fIndices = new ArrayList<Integer>();
	private final String fMessage;
	
	public ArrayComparisonFailure(String message, AssertionError cause, int index) {
		this.fMessage = message;
		initCause(cause);
		addDimension(index);
	}
	
	public void addDimension(int index) {
		fIndices.add(0, index);
	}
	
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		if (fMessage != null) {
			sb.append(fMessage);
		}
		sb.append("arrays first differed at element ");
		for (int each : fIndices) {
			sb.append("[");
			sb.append(each);
			sb.append("]");
		}
		sb.append("; ");
		sb.append(getCause().getMessage());
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}
