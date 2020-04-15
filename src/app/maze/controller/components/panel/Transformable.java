package app.maze.controller.components.panel;

import java.io.Serializable;

public abstract interface Transformable extends Serializable {

    abstract int[] transform(final int i);

}
