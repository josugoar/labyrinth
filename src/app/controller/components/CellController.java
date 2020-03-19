package app.controller.components;

import java.awt.Color;
import java.util.Objects;
import java.util.Set;

import app.model.components.Node;

/**
 * Cell interface wrapper with inneer <code>app.model.components.Node</code>
 * pointer.
 *
 * @param <T> CellController<T>
 * @see app.model.components.Node Node
 */
public interface CellController<T extends CellController<T>> {

    /**
     * Enum representing state and implementing
     * <code>app.controller.components.ColoredState</code>.
     *
     * @see app.controller.components.ColoredState ColoredState
     */
    public static enum CellState implements ColoredState {

        START(Color.RED), END(Color.GREEN), OBSTACLE(Color.BLACK), EMPTY(Color.WHITE);

        /**
         * State <code>java.awt.Color</code> pointer.
         */
        private Color color;

        /**
         * Create a new state.
         *
         * @param color Color
         */
        private CellState(final Color color) {
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

    /**
     * Notify state change.
     */
    public abstract void stateChange();

    /**
     * Return current
     * <code>app.controller.components.CellController.CellState</code> instance.
     */
    public CellState getState();

    /**
     * Set current <code>app.controller.components.CellController.CellState</code>
     * instance.
     *
     * @param state CellState
     */
    public void setState(final CellState state);

    /**
     * Return current <code>app.controller.components.CellController</code> neighbor
     * pointers.
     *
     * @return Set<T extends CellController<T>>
     */
    public abstract Set<T> getNeighbors();

    /**
     * Set current <code>app.controller.components.CellController</code> neighbor
     * pointers.
     *
     * @param neighbors Set<T extends CellController<T>>
     */
    public abstract void setNeighbors(final Set<T> neighbors);

    /**
     * Return <code>app.model.components.Node</code> inner pointer.
     *
     * @return Node<T extends CellController<T>>
     */
    public abstract Node<T> getInner();

    /**
     * Set <code>app.model.components.Node</code> inner pointer.
     *
     * @param inner Node<T extends CellController<T>>
     */
    public abstract void setInner(final Node<T> inner);

}
