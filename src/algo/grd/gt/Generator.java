package algo.grd.gt;

import algo.grd.GridAlgorithm;
import algo.grd.dsa.AbstractCell;
import utils.JWrapper;

public abstract class Generator<T extends AbstractCell<T>> extends GridAlgorithm<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Maze <code>app.model.components.CellPanel.CellState.OBSTACLE</code>
     * <code>app.model.Generator</code> density.
     */
    protected int density = 50;

    /**
     * Generate obstacle structure in grid.
     *
     * @param grid T[][]
     * @throws InterruptedException
     */
    protected abstract void generate() throws InterruptedException;

    public final void awake(final T[][] grid) {
        try {
            // Set grid
            this.setGrid(grid);
            // Run Thread
            new Thread(this).start();
        } catch (final NullPointerException | InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    @Override
    protected final void awake() {
        try {
            this.setRunning(true);
            this.generate();
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        } finally {
            this.setRunning(false);
        }
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.density;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Generator<?> other = (Generator<?>) obj;
        if (this.density != other.density)
            return false;
        return true;
    }

}
