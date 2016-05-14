package org.junit.junit4copy.internal;

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodSorter {

	public static final Comparator<Method> DEFAULT = new Comparator<Method>() {
		@Override
		public int compare(Method o1, Method o2) {
			int i1 = o1.getName().hashCode();
			int i2 = o2.getName().hashCode();
			if (i1 != i2) {
				return i1 < i2 ? -1 : 1;
			}
			return NAME_ASCENDING.compare(o1, o2);
		}
	};

	public static final Comparator<Method> NAME_ASCENDING = new Comparator<Method>() {
		
		@Override
		public int compare(Method o1, Method o2) {
			final int comparison = o1.getName().compareTo(o2.getName());
			if (comparison != 0) {
				return comparison;
			}
			return o1.toString().compareTo(o2.getName().toString());
		}

	};
}
