package app.model;

import java.awt.Point;
import java.security.spec.AlgorithmParameterSpec;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;
import app.controller.components.AbstractCell.CellState;
import app.view.components.RangedSlider.BoundedRange;

public abstract class Generator implements AbstractAlgorithm {

    private static final long serialVersionUID = 1L;

    /**
     * Flag for algorithm running state.
     */
    protected boolean isRunning = false;

    /**
     * Delay <code>app.view.components.RangedSlider.BoundedRange</code> between draw
     * cycles.
     *
     * @see app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    private final BoundedRange delay = new BoundedRange(0, 250, 100);

    /**
     * Maze <code>app.model.components.CellPanel.CellState.OBSTACLE</code>
     * <code>app.model.Generator</code> density.
     *
     * @see app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    private final BoundedRange density = new BoundedRange(1, 99, 50);

    /**
     * Generate obstacle structure in grid.
     *
     * @param <T>  AbstractCell<T>
     * @param grid T[][]
     */
    protected abstract <T extends AbstractCell<T>> void generate(final T[][] grid);

    @Override
    public final <T extends AbstractCell<T>> void awake(final T[][] grid, final Point start, final Point end) {
        new Thread(() -> this.generate(grid));
    }

    /**
     * Return current density <code>BoundedRange</code>.
     *
     * @return BoundedRange
     */
    public final BoundedRange getDensity() {
        return this.density;
    }

    /**
     * Set current density <code>BoundedRange</code> value.
     *
     * @param delay int
     */
    public final void setDensity(final int density) {
        this.density.setValue(density);
    }

    @Override
    public final boolean getIsRunning() {
        return this.isRunning;
    }

    @Override
    public final void setIsRunning(final boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public final BoundedRange getDelay() {
        return this.delay;
    }

    @Override
    public final void setDelay(final int delay) {
        this.delay.setValue(delay);
    }

    @Override
    public final String getAlgorithm() {
        return this.getClass().getSimpleName();
    }

    @Override
    public final String toString() {
        return this.getAlgorithm();
    }

    public static final class BackTracker extends Generator {

        private static final long serialVersionUID = 1L;

        @Override
        public final <T extends AbstractCell<T>> void generate(final T[][] grid) {

        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

    public static final class Random extends Generator {

        private static final long serialVersionUID = 1L;

        @Override
        public final <T extends AbstractCell<T>> void generate(final T[][] grid) {
            for (final T[] ts : grid)
                for (final T t : ts)
                    if (Math.random() < (float) super.density.getValue() / 100)
                        t.setState(CellState.OBSTACLE);
        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

}
