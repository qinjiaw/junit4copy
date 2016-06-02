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
		private static final String DIFF_END = "]";
		private static final String DIFF_START = "[";
		
		private final int contextLength;
		private final String expected;
		private final String actual;
		
		public ComparisonCompactor(int contextLength, String expected, String actual) {
			this.contextLength = contextLength;
			this.expected = expected;
			this.actual = actual;
		}
		
		public String compact(String message) {
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
		
		private String sharedPrefix() {
			int end = Math.min(expected.length(), actual.length());
			for (int i = 0; i < end; i++) {
				if (expected.charAt(i) != actual.charAt(i)) {
					return expected.substring(0, i);
				}
			}
			return expected.substring(0, end);
		}
		
		private String sharedSuffix(String prefix) {
			int suffixLength = 0;
			int maxSuffixLength = Math.min(expected.length() - prefix.length(),
					actual.length() - prefix.length()) - 1;
			for (; suffixLength <= maxSuffixLength; suffixLength++) {
				if (expected.charAt(expected.length() - 1 - suffixLength)
					!= actual.charAt(actual.length() - 1 - suffixLength)) {
					break;
				}
			}
			return expected.substring(expected.length() - suffixLength);
		}
		
		private class DiffExtractor {
			private final String sharedPrefix;
			private final String sharedSuffix;
			
			private DiffExtractor() {
				sharedPrefix = sharedPrefix();
				sharedSuffix = sharedSuffix(sharedPrefix);
			}
			
			public String expectedDiff() {
				return extractDiff(expected);
			}
			
			public String actualDiff() {
				return extractDiff(actual);
			}
			
			public String compactPrefix() {
				if (sharedPrefix.length() <= contextLength) {
					return sharedPrefix;
				}
				return ELLIPSIS + sharedPrefix.substring(sharedPrefix.length() - contextLength);
			}
			
			public String compactSuffix() {
				if(sharedSuffix.length() <= contextLength) {
					return sharedSuffix;
				}
				return sharedSuffix.substring(0, contextLength) + ELLIPSIS;
			}
			
			private String extractDiff(String source) {
				return DIFF_START + source.substring(sharedPrefix.length(), source.length() - sharedSuffix.length()) + DIFF_END;
			}
		}
	}
}
