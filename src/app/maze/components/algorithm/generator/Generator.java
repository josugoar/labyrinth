package app.maze.components.algorithm.generator;

import app.maze.components.algorithm.AlgorithmManager;
import utils.JWrapper;

public abstract class Generator extends AlgorithmManager {

    private static final long serialVersionUID = 1L;

    protected int density = 50;

    protected abstract void generate() throws InterruptedException;

    public final void awake(final Object[][] grid) {
        // try {
        //     // Set grid
        //     this.setGrid(grid);
        //     // Run Thread
        //     new Thread(this).start();
        // } catch (final NullPointerException | InterruptedException e) {
        //     JWrapper.dispatchException(e);
        // }
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

    public final int getDensity() {
        return this.density;
    }

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
        Generator other = (Generator) obj;
        if (this.density != other.density)
            return false;
        return true;
    }

}
