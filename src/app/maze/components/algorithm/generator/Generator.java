package app.maze.components.algorithm.generator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import app.maze.components.algorithm.AlgorithmManager;
import app.maze.components.cell.Walkable;
import utils.JWrapper;

public abstract class Generator extends AlgorithmManager {

    private static final long serialVersionUID = 1L;

    protected Set<Walkable> visited;

    protected Walkable start;

    protected int density = 50;

    protected abstract void advance(final Walkable node) throws InterruptedException;

    public final void generate(final Walkable start) {
        try {
            if (start == null)
                throw new NullPointerException("No starting node found...");
            // Set start
            setStart(start);
            // Run Thread
            start();
        } catch (final NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    @Override
    protected final void awake() {
        try {
            if (start == null)
                throw new NullPointerException("Generator is not initialized...");
            visited = new HashSet<Walkable>(0);
            setRunning(true);
            advance(start);
        } catch (final NullPointerException | InterruptedException e) {
            JWrapper.dispatchException(e);
        } finally {
            setRunning(false);
        }
    }

    public final Walkable getStart() {
        return start;
    }

    public final void setStart(final Walkable start) {
        this.start = Objects.requireNonNull(start, "Walkable must not be null...");
    }

    public final int getDensity() {
        return density;
    }

    public final void setDensity(final int density) {
        this.density = density;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + density;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Generator other = (Generator) obj;
        if (density != other.density)
            return false;
        return true;
    }

}
