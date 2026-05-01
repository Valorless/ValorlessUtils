package valorless.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.FIELD;

/** * Indicates that the annotated element is intended for internal use within the
 * codebase and should not be accessed or used by external code.
 * This annotation serves as a warning to developers that the annotated element may change
 * or be removed without notice, and should not be considered part of the public API.
 */
@Documented
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
public @interface Internal {
	/**
	 * An optional description providing additional context about the internal element.
	 *
	 * @return a string description of the internal element
	 */
	String value() default "";
	
}
