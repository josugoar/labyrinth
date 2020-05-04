package utils;

import java.awt.Component;
import java.awt.Container;
import java.io.Serializable;
import java.util.Arrays;

/**
 * <code>java.lang.FunctionalInterface</code> wrapper for
 * <code>java.awt.Component</code> references, extending
 * <code>Serializable</code>.
 *
 * @see java.io.Serializable Serializable
 */
@FunctionalInterface
public interface JWrapper extends Serializable{

    /**
     * Self-reference pointer.
     *
     * @param component Component
     * @return Component
     */
    public abstract Component wrap(final Component component);

    /**
     * Add <code>java.awt.Component</code> to <code>java.awt.Container</code>.
     *
     * @param <T>      Container
     * @param <U>      Component
     * @param parent   T
     * @param children U...
     */
    @SafeVarargs
    public static <T extends Container, U extends Component> void add(final T parent, final U... children) {
        Arrays.asList(children).forEach(parent::add);
    }

    /**
     * Dispatch generic <code>java.lang.Throwable</code> by printing in error
     * PrintStream.
     *
     * @param e Throwable
     */
    public static void dispatchException(final Throwable e) {
        System.err.println(e);
    }

}
