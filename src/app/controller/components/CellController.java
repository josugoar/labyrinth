package app.controller.components;

import java.awt.Color;
import java.util.Objects;
import java.util.Set;

import app.model.components.Node;

/**
 * Cell interface wrapper with inneer <code>app.model.components.Node</code>
 * pointer.
 *
 * @see app.model.components.Node Node
 */
public interface CellController<T extends CellController<T>> {

    /**
     * Colored state interface.
     */
    public interface ColoredState {

        /**
         * Return current associated <code>java.awt.Color</code>.
         *
         * @return Color
         * @see java.awt.Color Color
         */
        public abstract Color getColor();

        /**
         * Set current associated <code>java.awt.Color</code>.
         *
         * @param color Color
         * @see java.awt.Color Color
         */
        public abstract void setColor(final Color color);

    }

    public static enum CellState implements ColoredState {

        START(Color.RED), END(Color.GREEN), OBSTACLE(Color.BLACK), EMPTY(Color.WHITE);

        private Color color;

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

    public CellState getState();

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
