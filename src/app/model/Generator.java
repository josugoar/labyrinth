package app.model;

import java.awt.Point;
import java.security.spec.AlgorithmParameterSpec;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;
import app.view.components.RangedSlider.BoundedRange;

public abstract class Generator implements AbstractAlgorithm {

    private static final long serialVersionUID = 1L;

    protected boolean isRunning = false;

    /**
     * Maze <code>app.model.components.CellPanel.CellState.OBSTACLE</code>
     * <code>app.model.Generator</code> density.
     *
     * @see app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    private final BoundedRange density = new BoundedRange(1, 100, 10);

    @Override
    public final <T extends AbstractCell<T>> void awake(final T[][] grid, final Point start, final Point end) {
    }

    /**
     * Return current density
     * <code>app.view.components.RangedSlider.BoundedRange</code>.
     *
     * @return BoundedRange
     */
    public final BoundedRange getDensity() {
        return this.density;
    }

    /**
     * Set current density
     * <code>app.view.components.RangedSlider.BoundedRange</code> value.
     *
     * @param val int
     */
    public final void setDensity(final int val) {
        this.density.setValue(val);
    }

    @Override
    public final String getAlgorithm() {
        return this.getClass().getSimpleName();
    }

    public static final class BackTracker extends Generator {

        private static final long serialVersionUID = 1L;

        @Override
        public final boolean getIsRunning() {
            return this.isRunning;
        }

        @Override
        public final void setIsRunning(final boolean isRunning) {
            // TODO: Glass pane
            this.isRunning = isRunning;
        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

}
