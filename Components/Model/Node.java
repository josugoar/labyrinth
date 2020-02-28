package Components.Model;

import java.awt.Point;
import java.io.Serializable;

public final class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Point seed;
    private Node parent = null;
    private boolean path = false;

    public Node(final Node parent, final Point seed) {
        this.parent = parent;
        this.seed = seed;
    }

    public Node(final Point seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return String.format("%s (seed: %s)", this.getClass(), this.getSeed());
    }

    public Node getParent() {
        return this.parent;
    }

    public Point getSeed() {
        return this.seed;
    }

    public boolean getPath() {
        return this.path;
    }

    public void setPath(final boolean path) {
        this.path = path;
    }

}
