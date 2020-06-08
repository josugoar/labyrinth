package app.maze.controller.components.panel;

import java.io.Serializable;

/**
 * Multidimensional transformable euclidean space, implementing
 * <code>java.io.Serializable</code>.
 *
 * @see java.io.Serializable Serializable
 */
public abstract interface Transformable extends Serializable {

    /**
     * Flatten coordinates.
     *
     * @param dim int[]
     * @return int
     */
    abstract int transform(final int[] dim);

    /**
     * Bloat coordinates.
     *
     * @param i int
     * @return int[]
     */
    abstract int[] transform(final int i);

}
