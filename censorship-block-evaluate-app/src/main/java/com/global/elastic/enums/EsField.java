package com.global.elastic.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EsField {

	/** The Constant DEFAULT. */
	public static final String DEFAULT = "";

	/**
	 * Type.
	 *
	 * @return the field type
	 */
	FieldType type() default FieldType.ALPHA;

	/**
	 * Method to return name of Annotation field
	 * 
	 * @return name of field
	 */
	String name() default DEFAULT;

	/**
	 * Fragment size for highlight
	 * 
	 * @return integer of fragment size
	 */
	int length() default 1;

	/**
	 * Min range.
	 *
	 * @return the int
	 */
	int minRange() default 0;
	
	String minDate() default "1990-01-01";
}
