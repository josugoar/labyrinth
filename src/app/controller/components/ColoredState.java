package app.controller.components;

import java.awt.Color;

/**
 * Colored state interface.
 *
 * @see java.awt.Color Color
 */
public abstract interface ColoredState {

    /**
     * Return current associated <code>java.awt.Color</code>.
     *
     * @return Color
     */
    public abstract Color getColor();

    /**
     * Set current associated <code>java.awt.Color</code>.
     *
     * @param color Color
     */
    public abstract void setColor(final Color color);

}
