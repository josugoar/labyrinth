package app.model;

import java.awt.Point;
import java.io.Serializable;
import java.security.spec.AlgorithmParameterSpec;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;
import app.controller.components.AbstractCell.CellState;
import app.view.components.RangedSlider.BoundedRange;
import utils.JWrapper;

public abstract class Generator extends AbstractAlgorithm {

    // TODO: Spec
    private class GeneratorSpec implements AlgorithmParameterSpec, Serializable {

        private static final long serialVersionUID = 1L;

    }

    private static final long serialVersionUID = 1L;

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
     * @throws InterruptedException
     */
    protected abstract <T extends AbstractCell<T>> void generate(final T[][] grid) throws InterruptedException;

    @Override
    public final <T extends AbstractCell<T>> void awake(final T[][] grid, final Point start, final Point end) {
        new Thread(() -> {
            try {
                this.generate(grid);
            } catch (final InterruptedException e) {
                JWrapper.dispatchException(e);
            }
        }).start();
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
        public final <T extends AbstractCell<T>> void generate(final T[][] grid) throws InterruptedException {
            for (final T[] cells : grid)
                for (final T cell : cells) {
                    if (Math.random() < (float) super.density.getValue() / 100)
                        cell.setState(CellState.OBSTACLE);
                    // Delay iteration
                    Thread.sleep(super.delay.getValue());
                }
        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

}
