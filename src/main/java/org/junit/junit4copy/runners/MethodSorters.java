package org.junit.junit4copy.runners;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.junit4copy.internal.MethodSorter;

public enum MethodSorters {

	NAME_ASCENDING(MethodSorter.NAME_ASCENDING),

	JVM(null),

	DEFAULT(MethodSorter.DEFAULT);

	private final Comparator<Method> comparator;

	private MethodSorters(Comparator<Method> comparator) {
		this.comparator = comparator;
	}

	public Comparator<Method> getComparator() {
		return comparator;
	}

}
