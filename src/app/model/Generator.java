package app.model;

import java.awt.Point;
import java.io.Serializable;
import java.security.spec.AlgorithmParameterSpec;

import app.controller.components.AbstractCell;
import app.controller.components.AbstractCell.CellState;
import app.controller.components.AbstractEuclideanAlgorithm;
import utils.JWrapper;

public abstract class Generator extends AbstractEuclideanAlgorithm {

    // TODO: Spec
    private class GeneratorSpec implements AlgorithmParameterSpec, Serializable {

        private static final long serialVersionUID = 1L;

    }

    private static final long serialVersionUID = 1L;

    /**
     * Maze <code>app.model.components.CellPanel.CellState.OBSTACLE</code>
     * <code>app.model.Generator</code> density.
     */
    private int density = 50;

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
     * Return current maze
     * <code>app.model.components.CellPanel.CellState.OBSTACLE</code>
     * <code>app.model.Generator</code> density.
     *
     * @return int
     */
    public final int getDensity() {
        return this.density;
    }

    /**
     * Set current maze
     * <code>app.model.components.CellPanel.CellState.OBSTACLE</code>
     * <code>app.model.Generator</code> density.
     *
     * @param density int
     */
    public final void setDensity(final int density) {
        this.density = density;
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
                    if (Math.random() < (float) super.density / 100)
                        cell.setState(CellState.OBSTACLE);
                    // Delay iteration
                    Thread.sleep(super.delay);
                }
        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

}
