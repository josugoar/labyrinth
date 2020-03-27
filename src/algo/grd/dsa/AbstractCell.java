package algo.grd.dsa;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import algo.grd.State;

/**
 * Cell interface wrapper with inner
 * <code>app.model.components.Node</code> pointer, implementing
 * <code>java.io.Serializable</code>.
 *
 * @param <T> AbstractCell<T>
 * @see java.io.Serializable Serializable
 * @see app.model.components.Node Node
 */
public abstract interface AbstractCell<T extends AbstractCell<T>> extends Serializable {

    /**
     * Enum representing state and implementing
     * <code>app.controller.State</code>.
     *
     * @see app.controller.State State
     */
    public static enum CellState implements State<Color> {

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
        public final Color getReference() {
            return this.color;
        }

        @Override
        public void setReference(final Color color) {
            this.color = Objects.requireNonNull(color, "'color' must not be null");
        }

    }

    /**
     * Notify state change.
     */
    public abstract void notifyChange();

    /**
     * Return current <code>app.controller.components.AbstractCell.CellState</code>
     * instance.
     */
    public CellState getState();

    /**
     * Set current <code>app.controller.components.AbstractCell.CellState</code>
     * instance.
     *
     * @param state CellState
     */
    public void setState(final CellState state);

    // TODO: Move neighbors to Node

    /**
     * Return current <code>app.controller.components.AbstractCell</code> neighbor
     * pointers.
     *
     * @return Set<T extends AbstractCell<T>>
     */
    public abstract Set<T> getNeighbors();

    /**
     * Set current <code>app.controller.components.AbstractCell</code> neighbor
     * pointers.
     *
     * @param neighbors Set<T extends AbstractCell<T>>
     */
    public abstract void setNeighbors(final Set<T> neighbors);

    /**
     * Return <code>app.model.components.Node</code> inner pointer.
     *
     * @return Node<T extends AbstractCell<T>>
     */
    public abstract Node<T> getInner();

    /**
     * Set <code>app.model.components.Node</code> inner pointer.
     *
     * @param inner Node<T extends AbstractCell<T>>
     */
    public abstract void setInner(final Node<T> inner);

}
