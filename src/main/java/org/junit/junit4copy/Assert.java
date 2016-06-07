package org.junit.junit4copy;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.junit4copy.internal.ArrayComparisonFailure;
import org.junit.junit4copy.internal.ExactComparisonCriteria;
import org.junit.junit4copy.internal.InexactComparisonCriteria;

public class Assert {

	protected Assert() {
		
	}
	
	public static void assertTrue(String message, boolean condition) {
		if (!condition) {
			fail(message);
		}
	}
	
	public static void assertTrue(boolean condition) {
		assertTrue(null, condition);
	}
	
	public static void assertFalse(String message, boolean condition) {
		assertTrue(message, !condition);
	}
	
	public static void assertFalse(boolean condition) {
		assertFalse(null, condition);
	}
	
	public static void fail(String message) {
		if (message == null) {
			throw new AssertionError();
		}
		throw new AssertionError(message);
	}
	
	public static void fail() {
		fail(null);
	}
	
	public static void assertEquals(String message, Object expected, Object actual) {
		if (equalsRegardingNull(expected, actual)) {
			return;
		}
		if (expected instanceof String && actual instanceof String) {
			String cleanMessage = message == null ? "" : message;
			throw new ComparisonFailure(cleanMessage, (String) expected, (String) actual);
		} else {
			failNotEquals(message, expected, actual);
		}
	}
	
	private static boolean equalsRegardingNull(Object expected, Object actual) {
		if (expected == null) {
			return actual == null;
		}
		
		return isEquals(expected, actual);
	}
	
	private static boolean isEquals(Object expected, Object actual) {
		return expected.equals(actual);
	}
	
	public static void assertEquals(Object expected, Object actual) {
		assertEquals(null, expected, actual);
	}
	
	public static void assertNoEquals(String message, Object unexpected, Object actual) {
		if (equalsRegardingNull(unexpected, actual)) {
			failEquals(message, actual);
		}
	}
	
	public static void failEquals(String message, Object actual) {
		String formartted = "Values should be defferent. ";
		if (message != null) {
			formartted = message + ". ";
		}
		
		formartted += "Actual: " + actual;
		fail(formartted);
	}
	
	public static void assertNotEquals(String message, long unexpected, long actual) {
		if (unexpected == actual) {
			failEquals(message, Long.valueOf(actual));
		}
	}
	
	public static void assertNoEquals(long unexpected, long actual) {
		assertNotEquals(null, unexpected, actual);
	}
	
	public static void assertNotEquals(String message, double unexpected, double actual, double delta) {
		if (!doubleIsDifferent(unexpected, actual, delta)) {
			failEquals(message, Double.valueOf(actual));
		}
	}
	
	public static void assertNotEquals(double unexpected, double actual, double delta) {
		assertNotEquals(null, unexpected, actual, delta);
	}
	
	public static void assertArrayEquals(String message, Object[] expecteds, Object[] actuals) throws ArrayComparisonFailure {
		internalArrayEquals(message, expecteds, actuals);
	}
	
	public static void assertArrayEquals(Object[] expecteds, Object[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}
	
	public static void assertArrayEquals(String message, boolean[] expecteds, boolean[] actuals) throws ArrayComparisonFailure {
		internalArrayEquals(message, expecteds, actuals);
	}
	
	public static void assertArrayEquals(boolean[] expecteds, boolean[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}
	
	public static void assertArrayEquals(String message, byte[] expecteds, byte[] actuals) throws ArrayComparisonFailure {
		internalArrayEquals(message, expecteds, actuals);
	}
	
	public static void assertArrayEquals(byte[] expecteds, byte[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}
	
	public static void assertArrayEquals(char[] expecteds, char[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}
	
	public static void assertArrayEquals(String message, short[] expecteds, short[] actuals) throws ArrayComparisonFailure {
		internalArrayEquals(message, expecteds, actuals);
	}
	
	public static void assertArrayEquals(short[] expecteds, short[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}
	
	public static void assertArrayEquals(String message, int[] expecteds, int[] actuals) throws ArrayComparisonFailure {
		internalArrayEquals(message, expecteds, actuals);
	}
	
	public static void assertArrayEquals(int[] expecteds, int[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}
	
	public static void assertArrayEquals(String message, long[] expecteds, long[] actuals) throws ArrayComparisonFailure {
		internalArrayEquals(message, expecteds, actuals);
	}
	
	public static void assertArrayEquals(long[] expecteds, long[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}
	
	public static void assertArrayEquals(String message, double[] expecteds, double[] actuals, double delta) throws ArrayComparisonFailure {
		new InexactComparisonCriteria(delta).arrayEquals(message, expecteds, actuals);
	}
	
	public static void assertArrayEquals(double[] expecteds, double[] actuals, double delta) {
		assertArrayEquals(null, expecteds, actuals);
	}
	
	public static void assertArrayEquals(String message, float[] expecteds, float[] actuals, float delta) throws ArrayComparisonFailure {
		new InexactComparisonCriteria(delta).arrayEquals(message, expecteds, actuals);
	}
	
	public static void assertArrayEquals(float[] expecteds, float[] actuals, float delta) {
		assertArrayEquals(null, expecteds, actuals, delta);
	}
	
	public static void assertArrayEquals(String message, Object expecteds, Object actuals) throws ArrayComparisonFailure {
		new ExactComparisonCriteria().arrayEquals(message, expecteds, actuals);
	}
	
	private static void internalArrayEquals(String message, Object expecteds, Object actuals) throws ArrayComparisonFailure {
		new ExactComparisonCriteria().arrayEquals(message, expecteds, actuals);
	}
	
	public static void assertEquals(String message, double expected, double actual, double detla) {
		if (doubleIsDifferent(expected, actual, detla)) {
			failNotEquals(message, Double.valueOf(expected), Double.valueOf(actual));
		}
	}
	
	public static void assertEquals(String message, float expecteds, float actuals, float detla) {
		if (floatIsDifferent(expecteds, actuals, detla)) {
			failNotEquals(message, Float.valueOf(expecteds), Float.valueOf(actuals));
		}
	}
	
	public static void assertNotEquals(String message, float unexpected, float actual, float detla) {
		if (!floatIsDifferent(unexpected, actual, detla)) {
			failEquals(message, actual);
		}
	}
	
	public static boolean doubleIsDifferent(double d1, double d2, double detla) {
		if (Double.compare(d1, d2) == 0) {
			return false;
		}
		if (Math.abs(d1 - d2) <= detla) {
			return false;
		}
		
		return true;
	}
	
	public static boolean floatIsDifferent(float f1, float f2, float detal) {
		if (Float.compare(f1, f2) == 0) {
			return false;
		}
		if (Math.abs(f1 - f2) <= detal) {
			return false;
		}
		
		return true;
	}
	
	public static void assertEquals(long expected, long actual) {
		assertEquals(null, expected, actual);
	}
	
	
	public static void assertEquals(String message, long expected, long actual) {
		if (expected != actual) {
			failNotEquals(message, Long.valueOf(expected), Long.valueOf(actual));
		}
	}
	
	@Deprecated
	public static void asserEquals(double expected, double actual) {
		assertEquals(null, expected, actual);
	}
	
	@Deprecated
	public static void assertEquals(String message, double expected, double actual) {
		fail("Use asserEquals(expected, actual, detla) to compare floating-point numbers");
	}
	
	public static void assertEuals(double expected, double actual, double detla) {
		assertEquals(null, expected, actual, detla);
	}
	
	public static void assertEquals(float expected, float actual, float detla) {
		assertEquals(null, expected, actual, detla);
	}
	
	public static void assertNotNull(String message, Object object) {
		assertTrue(message, object != null);
	}
	
	public static void assertNotNull(Object object) {
		assertNotNull(null, object);
	}
	
	public static void assertNull(String message, Object object) {
		if (object == null) {
			return;
		}
		failNotNull(message, object);
	}
	
	public static void failNotNull(String message, Object actual) {
		String formatted = "";
		if (message != null) {
			formatted = message + " ";
		}
		fail(formatted + "expected null, but was:<" + actual + ">");
	}
	
	public static void assertSame(String message, Object expected, Object actual) {
		if (expected == actual) {
			return;
		}
		failNotSame(message, expected, actual);
	}
	
	public static void assertSame(Object expected, Object actual) {
		assertSame(null, expected, actual);
	}
	
	public static void assertNotSame(String message, Object unexpected, Object actual) {
		if (unexpected == actual) {
			failSame(message);
		}
	}
	
	public static void assertNotSame(Object unexpected, Object actual) {
		assertNotSame(null, unexpected, actual);
	}
	
	private static void failSame(String message) {
		String formatted = "";
		if (message != null) {
			formatted = message + " ";
		}
		fail(formatted + "expected not same");
	}
	
	private static void failNotSame(String message, Object expected, Object actual) {
		String formatted = "";
		if (message != null) {
			formatted = message + " ";
		}
		fail(formatted + "expected same:<" + expected + "> was not:<" + actual + ">");
	}
	
	private static void failNotEquals(String message, Object expected, Object actual) {
		fail(format(message, expected, actual));
	}
	
	static String format(String message, Object expected, Object actual) {
		String formatted = "";
		if (message != null && !"".equals(message)) {
			formatted = message + " ";
		}
		String expectedString = String.valueOf(expected);
		String actualString = String.valueOf(actual);
		if (expectedString.equals(actualString)) {
			return formatted + "expected:" + formatClassAndValue(expected, expectedString)
					+ " but was: " + formatClassAndValue(actual, actualString);
		} else {
			return formatted + "expected:<" + expectedString + "> but was:<" + actualString + ">";
		}
	}
	
	private static String formatClassAndValue(Object value, String valueString) {
		String className = value == null ? "null" : value.getClass().getName();
		return className + "<" + valueString + ">";
	}
	
	@Deprecated
	private static void assertEquals(String message, Object[] expecteds, Object[] actuals) {
		assertArrayEquals(message, expecteds, actuals);
	}
 	
	@Deprecated
	private static void assertEquals(Object[] expecteds, Object[] actuals) {
		assertArrayEquals(expecteds, actuals);
	}
	
	@Deprecated
	private static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
		MatcherAssert.assertThat(reason, actual, matcher);
	}
	
	private interface ThrowingRunnable {
		void run() throws Throwable;
	}
	
	private static void assertThrows(Class<? extends Throwable> expectedThrowable, ThrowingRunnable runnable) {
		expectThrows(expectedThrowable, runnable);
	}
	
	private static <T extends Throwable> T expectThrows(Class<T> expectedThrowable, ThrowingRunnable runnable) {
		try {
			runnable.run();
		} catch (Throwable actualThrown) {
			if (expectedThrowable.isInstance(actualThrown)) {
				@SuppressWarnings("unchecked") T retVal = (T) actualThrown;
				return retVal;
			} else {
				String mismatchMessage = format("unexpected exception type thrown;", expectedThrowable.getSimpleName(), actualThrown.getClass().getSimpleName());
				// The AssertionError(String, Throwable) ctor is only available on JDK7.
				AssertionError assertionError = new AssertionError(mismatchMessage);
				assertionError.initCause(actualThrown);
				throw assertionError;
			}
		}
		String message = String.format("expected %s to be thrown, but nothing was thrown", expectedThrowable.getSimpleName());
		throw new AssertionError(message);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
