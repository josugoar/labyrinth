package algo.grd;

import algo.AbstractAlgorithm;
import algo.grd.dsa.AbstractCell;

/**
 * Euclidean plane algorithm abstract class wrapper, extending
 * <code>algo.AbstractAlgorithm</code>.
 *
 * @see algo.AbstractAlgorithm AbstractAlgorithm
 */
public abstract class GridAlgorithm<T extends AbstractCell<T>> extends AbstractAlgorithm {

    private static final long serialVersionUID = 1L;

    /**
     * Euclidean plane grid.
     */
    protected T[][] grid = null;

    /**
     * Return current epean plane grid.
     *
     * @return T[][]
     */
    public final T[][] getGrid() {
        return this.grid;
    }

    /**
     * Set current epean plane grid.
     *
     * @param grid T[][]
     * @throws InterruptedException if (running)
     */
    public synchronized void setGrid(final T[][] grid) throws InterruptedException {
        if (this.running)
            throw new InterruptedException("Cannot modify grid while running...");
        this.grid = grid;
    }

}
