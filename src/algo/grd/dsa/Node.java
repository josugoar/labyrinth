package algo.grd.dsa;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;

import algo.State;

/**
 * <code>app.controller.components.AbstractCell</code> helper for
 * <code>app.controller.components.GridAlgorithm</code> tasks, which
 * stores multiple parent and outer pointers of different iteration generations.
 *
 * @param <T> AbstractCell<T>
 * @see app.controller.components.AbstractCell AbstractCell
 * @see app.controller.components.GridAlgorithm GridAlgorithm
 */
public final class Node<T extends AbstractCell<T>> implements Serializable {

    /**
     * Enum representing state and implementing
     * <code>app.controller.State</code>.
     *
     * @see app.controller.State State
     */
    public static enum NodeState implements State<Color> {

        VISITED(Color.CYAN), GERMINATED(Color.BLUE), PATH(Color.YELLOW);

        /**
         * State <code>java.awt.Color</code> pointer.
         */
        private Color color;

        /**
         * Create a new state.
         *
         * @param color Color
         */
        private NodeState(final Color color) {
            this.color = color;
        }

        @Override
        public final Color getReference() {
            return this.color;
        }

        @Override
        public void setReference(final Color color) {
            this.color = Objects.requireNonNull(color, "'color' must not be null");
        }

    }

    private static final long serialVersionUID = 1L;

    /**
     * Previous generation <code>app.model.components.Node</code> parent pointer.
     *
     * @see app.model.components.Node#parent Recursion is fun!
     */
    private final Node<T> parent;

    /**
     * Enclosing <code>app.controller.components.AbstractCell</code> instance
     * outer pointer.
     */
    private final T outer;

    /**
     * Current <code>app.model.components.Node.NodeState</code> instance.
     */
    private NodeState state = NodeState.GERMINATED;

    /**
     * Enclose <code>app.model.components.Node</code> parent and outer.
     *
     * @param parent Node<T extends AbstractCell<T>>
     * @param outer  T extends AbstractCell<T>
     */
    public Node(final Node<T> parent, final T outer) {
        this.parent = parent;
        this.outer = outer;
    }

    /**
     * No-parent ancestor <code>app.model.components.Node</code>.
     *
     * @param outer T extends AbstractCell<T>
     */
    public Node(final T outer) {
        this(null, outer);
    }

    /**
     * Iterate over all parents of
     * <code>app.controller.components.AbstractCell</code>
     * <code>app.model.components.Node</code>.
     */
    public final void traverse() {
        if (this.parent != null) {
            this.setState(NodeState.PATH);
            this.parent.traverse();
        }
    }

    /**
     * Return previous generation <code>app.model.components.Node</code> parent
     * pointer.
     *
     * @return Node<T extends AbstractCell<T>>
     */
    public final Node<T> getParent() {
        return this.parent;
    }

    /**
     * Return enclosing <code>app.controller.components.AbstractCell</code> instance
     * outer pointer.
     *
     * @return T extends AbstractCell<T>
     */
    public final T getOuter() {
        return this.outer;
    }

    /**
     * Return current <code>app.model.components.Node.NodeState</code> instance.
     *
     * @return NodeState
     */
    public final NodeState getState() {
        return this.state;
    }

    /**
     * Set current <code>app.model.components.Node.NodeState</code> instance.
     *
     * @param state NodeState
     */
    public final void setState(final NodeState state) {
        this.state = Objects.requireNonNull(state, "'state' must not be null");
        this.outer.notifyChange();
    }

    @Override
    public final String toString() {
        return String.format("Node [state: %s]", this.state);
    }

}
