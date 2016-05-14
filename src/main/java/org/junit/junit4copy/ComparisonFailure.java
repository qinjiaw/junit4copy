package org.junit.junit4copy;

import junit.framework.Assert;

public class ComparisonFailure extends AssertionError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAX_CONTEXT_LENGTH = 20;
	
	private String fExpected;
	private String fActual;
	
	public ComparisonFailure(String message, String expected, String actual) {
		super(message);
		this.fExpected = expected;
		this.fActual = actual;
	}
	
	public String getMessage() {
		return new ComparisonCompactor(MAX_CONTEXT_LENGTH, fExpected, fActual).compact(super.getMessage());
	}
	
	public String getActual() {
		return this.fActual;
	}
	
	public String getExpected() {
		return this.fExpected;
	}
	
	private static class ComparisonCompactor {
		private static final String ELLIPSIS = "...";
		private static final String DIEF_END = "]";
		private static final String DIEF_START = "[";
		
		private final int contextLength;
		private final String expected;
		private final String actual;
		
		public ComparsionCompactor(int contextLength, String expected, String actual) {
			this.contextLength = contextLength;
			this.expected = expected;
			this.actual = actual;
		}
		
		public String comoact(String message) {
			if(expected == null || actual == null || expected.equals(actual)) {
				return Assert.format(message, expected, actual);
			} else {
				DiffExtractor extractor = new DiffExtractor();
				String compactedPrefix = extractor.compactPrefix();
				String compactedSuffix = extractor.compactSuffix();
				return Assert.format(message,
						compactedPrefix + extractor.expectedDiff() + compactedSuffix,
						compactedPrefix + extractor.actualDiff() + compactedSuffix);
			}
		}
		
		private String sharePrefix() {
			int end = Math.min(expected.length(), actual.length());
			for (int i = 0; i < end; i++) {
				if (expected.charAt(i) != actual.charAt(i)) {
					return expected.substring(0, i);
				}
			}
			return expected.substring(0, end);
		}
		
		private String shareSuffix(String prefix) {
			int suffixLength = 0;
			int maxSuffixLength = Math.min(expected.length() - prefix.length(),
					actual.length() - prefix.length()) - 1;
			for (; suffixLength <= maxSuffixLength; suffixLength++) {
				
			}
		}
	}
}
