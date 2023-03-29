package org.JE.JE2ObjectParser.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The persistent name for the field. Parser will look for this name before the fieldName when trying to resolve fields.
 * This annotation also allows you to change the name of the variable while maintaining compatability.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistentName {
    String name() default "field";
}
