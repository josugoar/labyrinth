package app.model.components;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;

import app.controller.components.CellController;
import app.controller.components.ColoredState;

/**
 * <code>app.controller.components.CellController</code> helper for
 * <code>app.controller.components.AlgorithmController</code> tasks, which
 * stores multiple parent and outer pointers of different iteration generations.
 *
 * @param <T> CellController<T>
 * @see app.controller.components.CellController CellController
 * @see app.controller.components.AlgorithmController AlgorithmController
 */
public final class Node<T extends CellController<T>> implements Serializable {

    /**
     * Enum representing state and implementing
     * <code>app.controller.components.ColoredState</code>.
     *
     * @see app.controller.components.ColoredState ColoredState
     */
    public static enum NodeState implements ColoredState {

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
        public final Color getColor() {
            return this.color;
        }

        @Override
        public void setColor(Color color) {
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
     * Enclosing <code>app.controller.components.CellController</code> instance
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
     * @param parent Node<T extends CellController<T>>
     * @param outer  T extends CellController<T>
     */
    public Node(final Node<T> parent, final T outer) {
        this.parent = parent;
        this.outer = outer;
    }

    /**
     * No-parent ancestor <code>app.model.components.Node</code>.
     *
     * @param outer T extends CellController<T>
     */
    public Node(final T outer) {
        this(null, outer);
    }

    /**
     * Return previous generation <code>app.model.components.Node</code> parent
     * pointer.
     *
     * @return Node<T extends CellController<T>>
     */
    public Node<T> getParent() {
        return this.parent;
    }

    /**
     * Return enclosing <code>app.controller.components.CellController</code> instance
     * outer pointer.
     *
     * @return T extends CellController<T>
     */
    public T getOuter() {
        return this.outer;
    }

    /**
     * Return current <code>app.model.components.Node.NodeState</code> instance.
     *
     * @return NodeState
     */
    public NodeState getState() {
        return this.state;
    }

    /**
     * Set current <code>app.model.components.Node.NodeState</code> instance.
     *
     * @param state NodeState
     */
    public void setState(final NodeState state) {
        this.state = Objects.requireNonNull(state, "'state' must not be null");
        this.outer.stateChange();
    }

}
