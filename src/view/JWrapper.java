package src.view;

import java.awt.Component;
import java.io.Serializable;

/**
 * Component wrapper interface
 *
 * @author JoshGoA
 */
@FunctionalInterface
public abstract interface JWrapper extends Serializable {

    static final long serialVersionUID = 1L;

    /**
     * Single Component multiple reference pointer
     *
     * @param component Component
     */
    abstract Component JWComponent(final Component component);

}
