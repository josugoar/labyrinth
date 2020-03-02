package src.view;

import java.awt.Component;
import java.io.Serializable;

/**
 * <code>java.io.Serializable</code> component wrapper interface.
 *
 * @see java.io.Serializable Serializable
 */
@FunctionalInterface
public abstract interface JWrapper extends Serializable {

    static final long serialVersionUID = 1L;

    /**
     * Single <code>java.awt.Component</code> multiple reference pointer.
     *
     * @param component Component
     * @see java.awt.Component Component
     */
    abstract Component JWComponent(final Component component);

}
