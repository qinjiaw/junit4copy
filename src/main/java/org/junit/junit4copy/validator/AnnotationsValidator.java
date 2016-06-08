package org.junit.junit4copy.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AnnotationsValidator implements TestClassValidator {

	private static final List<AnnotatableValidator<?>> VALIDATORS = Arrays.<AnnotatableValidator<?>>asList(new ClassValidator(), new MethodValidator(), new FieldValidator())
			
	public List<Exception> validateTestClass(TestClass testClass) {
		List<Exception> validationErrors = new ArrayList<Exception>();
		for (AnnotatableValidator<?> validator : VALIDATORS) {
			List<Exception> additionalErrors = validator.validateTestClass(testClass);
			validatorErrors.addAll(additionalErrors);
		}
		return validationErrors;
	}	
			
			
}
