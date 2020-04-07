package app.maze.components.algorithm.pathfinder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import app.maze.components.cell.observer.CellObserver;
import utils.AlgorithmManager;
import utils.JWrapper;

public abstract class PathFinder extends AlgorithmManager {

    private static final long serialVersionUID = 1L;

    protected CellObserver start = null;

    protected CellObserver target = null;

    protected abstract CellObserver advance(final Set<CellObserver> currGen) throws StackOverflowError, InterruptedException;

    public final void awake(final CellObserver start, final CellObserver target) {
        try {
            if (start == null)
                throw new NullPointerException("No starting node found...");
            // Set grid and enpoints
            this.setStart(start);
            this.setTarget(target);
            // Run Thread
            new Thread(this).start();
        } catch (final NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    @Override
    protected final void awake() {
        try {
            if (this.start == null)
                throw new NullPointerException("PathFinder is not initialized...");
            this.advance(new HashSet<CellObserver>() {
                private static final long serialVersionUID = 1L;
                {
                    // Construct first generation
                    this.add(PathFinder.this.start);
                    // Set running
                    PathFinder.this.setRunning(true);
                }
                // Traverse entire CellObserver structure
            }).getPath(); // TODO: Return TreePath
        } catch (final NullPointerException | StackOverflowError | InterruptedException e) {
            JWrapper.dispatchException(e);
        } finally {
            this.setRunning(false);
        }
    }

    @Override
    public final void run() {
        this.awake();
    }

    public final CellObserver getStart() {
        return this.start;
    }

    public synchronized final void setStart(final CellObserver start) {
        try {
            this.assertRunning();
            this.start = Objects.requireNonNull(start, "Start must not be null...");
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final CellObserver getTarget() {
        return this.target;
    }

    public synchronized final void setTarget(final CellObserver target) {
        this.target = target;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.running ? 1231 : 1237);
        result = prime * result + ((this.target == null) ? 0 : this.target.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final PathFinder other = (PathFinder) obj;
        if (this.running != other.running)
            return false;
        if (this.target == null)
            if (other.target != null)
                return false;
            else if (!this.target.equals(other.target))
                return false;
        return true;
    }

}
