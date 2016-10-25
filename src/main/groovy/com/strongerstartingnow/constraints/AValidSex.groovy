package com.strongerstartingnow.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target([FIELD])
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = [com.strongerstartingnow.constraints.AValidSexImpl.class])
@interface AValidSex {

	String message() default "com.strongerstartingnow.constraints.avalidsex";
	
	Class<?>[] groups() default [];

	Class<? extends Payload>[] payload() default [];
	
}
