package algo.grd.gt.gen;

import java.io.Serializable;
import java.security.spec.AlgorithmParameterSpec;

import algo.grd.GridAlgorithm;
import algo.grd.dsa.AbstractCell;
import algo.grd.dsa.AbstractCell.CellState;
import utils.JWrapper;

public abstract class Generator<T extends AbstractCell<T>> extends GridAlgorithm<T> {

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
    protected abstract void generate(final T[][] grid) throws InterruptedException;

    public final void awake() {
        new Thread(() -> {
            try {
                this.generate(grid);
            } catch (final InterruptedException e) {
                JWrapper.dispatchException(e);
            }
        }).start();
    }

    @Override
    public final void run() {
        this.awake();
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

    public static final class BackTracker<T extends AbstractCell<T>> extends Generator<T> {

        private static final long serialVersionUID = 1L;

        @Override
        public final void generate(final T[][] grid) {

        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

    public static final class Random<T extends AbstractCell<T>> extends Generator<T> {

        private static final long serialVersionUID = 1L;

        @Override
        public final  void generate(final T[][] grid) throws InterruptedException {
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
