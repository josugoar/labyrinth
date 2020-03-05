package src.model;

import java.awt.Point;
import java.io.Serializable;

/**
 * <code>java.io.Serializable</code> element containing given reference pointing
 * to parent.
 *
 * @param <T> Reference
 * @see java.io.Serializable Serializable
 */
public final class Node<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Pointer to parent <code>src.model.Node<T></code>.
     */
    private Node<T> parent;

    /**
     * Inner reference.
     */
    private final T inner;

    /**
     * Euclidean coordinates.
     */
    private final Point seed;

    /**
     * Enclose <code>src.model.Node<T></code> <code>src.model.Node<T></code>, Point
     * and T.
     *
     * @param parent Node<T>
     * @param seed   Point
     * @param inner  T
     */
    public Node(final Node<T> parent, final Point seed, final T inner) {
        this.parent = parent;
        this.seed = seed;
        this.inner = inner;
    }

    /**
     * Enclose <code>src.model.Node<T></code> <code>src.model.Node<T></code> and
     * Point.
     *
     * @param seed  Point
     * @param inner T
     */
    public Node(final Point seed, final T inner) {
        this(null, seed, inner);
    }

    @Override
    public String toString() {
        return String.format("Node(seed: %s)", this.getSeed());
    }

    protected T getInner() {
        return this.inner;
    }

    protected Point getSeed() {
        return this.seed;
    }

    protected Node<T> getParent() {
        return this.parent;
    }

}
