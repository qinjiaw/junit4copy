package org.junit.junit4copy.validator;

import static java.util.Collections.singletonList;
import org.junit.junit4copy.runners.model.Annotatable;
import org.junit.junit4copy.runners.model.FrameworkField;
import org.junit.junit4copy.runners.model.FrameworkMethod;
import org.junit.junit4copy.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.util.*;

public final class AnnotationsValidator implements TestClassValidator {

	private static final List<AnnotatableValidator<?>> VALIDATORS = Arrays.<AnnotatableValidator<?>>asList(
			new ClassValidator(), new MethodValidator(), new FieldValidator());
			
	public List<Exception> validateTestClass(TestClass testClass) {
		List<Exception> validationErrors = new ArrayList<Exception>();
		for (AnnotatableValidator<?> validator : VALIDATORS) {
			List<Exception> additionalErrors = validator.validateTestClass(testClass);
			validationErrors.addAll(additionalErrors);
		}
		return validationErrors;
	}

	private static abstract class AnnotatableValidator<T extends Annotatable> {
		private static final AnnotationValidatorFactory ANNOTATION_VALIDATOR_FACTORY = new AnnotationValidatorFactory();

		abstract Iterator<T> getAnnotatablesForTestClass(TestClass testClass);

		abstract List<Exception> validateAnnotatable(AnnotationValidator validator, T annotatable);

		public List<Exception> validateTestClass(TestClass testClass) {
			List<Exception> validationErrors = new ArrayList<Exception>();

			for (Iterator<T> iter = getAnnotatablesForTestClass(testClass); iter.hasNext();) {
				T annotatable = iter.next();
				List<Exception> additionalErrors = validateAnnotatable(annotatable);
				validationErrors.addAll(additionalErrors);
			}
			return validationErrors;
		}

		private List<Exception> validateAnnotatable(T annotatable) {
			List<Exception> validationErrors = new ArrayList<Exception>();
			for (Annotation annotation : annotatable.getAnnotations()) {
				Class<? extends Annotation> annotationType = annotation.annotationType();
				ValidateWith validateWith = annotationType.getAnnotation(ValidateWith.class);
				if (validateWith != null) {
					AnnotationValidator annotationValidator = ANNOTATION_VALIDATOR_FACTORY.createAnnotationValidator(validateWith);
					List<Exception> errors = validateAnnotatable(annotationValidator, annotatable);
					validationErrors.addAll(errors);
				}
			}
			return validationErrors;
		}
	}

	private static class ClassValidator extends AnnotatableValidator<TestClass> {

		@Override
		Iterator<TestClass> getAnnotatablesForTestClass(TestClass testClass) {
			return (Iterator<TestClass>) singletonList(testClass);
		}

		@Override
		List<Exception> validateAnnotatable(AnnotationValidator validator, TestClass testClass) {
			return validator.validateAnnotatedClass(testClass);
		}
	}

	private static class MethodValidator extends AnnotatableValidator<FrameworkMethod> {

		@Override
		Iterator<FrameworkMethod> getAnnotatablesForTestClass(TestClass testClass) {
			return (Iterator<FrameworkMethod>) testClass.getAnnotatedMethods();
		}

		@Override
		List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkMethod method) {
			return validator.validateAnnotatedMethod(method);
		}
	}

	private static class FieldValidator extends AnnotatableValidator<FrameworkField> {

		@Override
		Iterator<FrameworkField> getAnnotatablesForTestClass(TestClass testClass) {
			return (Iterator<FrameworkField>) testClass.getAnnotatedFields();
		}

		@Override
		List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkField field) {
			return validator.validateAnnotatedField(field);
		}
	}
}
