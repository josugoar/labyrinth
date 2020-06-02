package utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Delegate pointer.
 *
 * @deprecated
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Delegate {

    /**
     * Point to <code>java.lang.Class<?></code> target.
     *
     * @return Class<?>
     */
    public Class<?> target() default Object.class;

}
