package algo.grd.gt.pfdr;

import java.awt.Point;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashSet;
import java.util.Set;

import algo.grd.GridAlgorithm;
import algo.grd.dsa.AbstractCell;
import algo.grd.dsa.Node;
import utils.JWrapper;

/**
 * PathFinding algorithm abstract wrapper, implementing
 * <code>app.controller.components.GridAlgorithm</code>.
 *
 * @param <T>
 *
 * @param <T>
 *
 * @see app.controller.components.GridAlgorithm GridAlgorithm
 * @see app.controller.components.AbstractCell AbstractCell
 * @see app.model.components.Node Node
 */
public abstract class PathFinder<T extends AbstractCell<T>> extends GridAlgorithm<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Start endpoint pointer.
     *
     * @see java.awt.Point Point
     */
    protected Point start = null;

    /**
     * Target endpoint pointer.
     *
     * @see java.awt.Point Point
     */
    protected Point target = null;;

    /**
     * Recursively iterate over generations using
     * <code>app.model.components.Node</code> priority queue.
     *
     * @param grid    T[][]
     * @param currGen Set<Node<T>>Set<Node<T>>
     * @throws StackOverflowError   if (newGen.size() == 0)
     * @throws InterruptedException if (!isRunning)
     */
    protected abstract Node<T> advance(final Set<Node<T>> currGen) throws StackOverflowError, InterruptedException;

    /**
     * <code>app.model.PathFinder.awake()</code> grid, start and target wrapper.
     *
     * @param grid   T[][]
     * @param start  Point
     * @param target Point
     */
    public final void awake(final T[][] grid, final Point start, final Point target) {
        try {
            if (start == null)
                throw new NullPointerException("No starting node found...");
            // Set grid and enpoints
            this.setGrid(grid);
            this.setStart(start);
            this.setTarget(target);
            // Run Thread
            new Thread(this).start();
        } catch (final NullPointerException | IndexOutOfBoundsException | InterruptedException  e) {
            JWrapper.dispatchException(e);
        }
    }

    @Override
    protected final void awake() {
        try {
            if (this.grid == null || this.start == null)
                throw new NullPointerException("PathFinder is not initialized...");
            this.advance(new HashSet<Node<T>>() {
                private static final long serialVersionUID = 1L;
                {
                    // Construct first generation
                    this.add(new Node<T>(PathFinder.this.grid[PathFinder.this.start.x][PathFinder.this.start.y]));
                    // Set running
                    PathFinder.this.setRunning(true);
                }
                // Traverse entire Node tree structure
            }).traverse();
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

    /**
     * Return current taget endpoint pointer.
     *
     * @return Point
     */
    public final Point getStart() {
        return this.start;
    }

    /**
     * Set current start endpoint pointer.
     *
     * @param start Point
     */
    @SuppressWarnings("unused")
    public synchronized final void setStart(final Point start) {
        try {
            final T startCell = this.grid[start.x][start.y];
            this.start = start;
        } catch (final NullPointerException e1) {
            throw new NullPointerException("Grid might not have been initialized...");
        } catch (final IndexOutOfBoundsException e2) {
            throw new IndexOutOfBoundsException("Start does not belong to array...");
        }
    }

    /**
     * Return current taget endpoint pointer.
     *
     * @return Point
     */
    public final Point getTarget() {
        return this.target;
    }

    /**
     * Set current target endpoint pointer.
     *
     * @param target Point
     */
    @SuppressWarnings("unused")
    public synchronized final void setTarget(final Point target) {
        if (target == null)
            return;
        try {
            final T targetCell = this.grid[target.x][target.y];
            this.target = start;
        } catch (final NullPointerException e1) {
            throw new NullPointerException("Grid might not have been initialized...");
        } catch (final IndexOutOfBoundsException e2) {
            throw new IndexOutOfBoundsException("Target does not belong to array...");
        }
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
        final PathFinder<?> other = (PathFinder<?>) obj;
        if (this.running != other.running)
            return false;
        if (this.target == null)
            if (other.target != null)
                return false;
            else if (!this.target.equals(other.target))
                return false;
        return true;
    }

    /**
     * Dijkstra pathfinding algorithm implementation, extending
     * <code>app.model.PathFinder</code>.
     *
     * @see app.model.PathFinder PathFinder
     */
    public static final class Dijkstra<T extends AbstractCell<T>> extends PathFinder<T> {

        private static final long serialVersionUID = 1L;

        /**
         * Visit all <code>app.model.components.Node</code> in generation.
         *
         * @param <T> AbstractCell<T>
         * @param gen Set<Node<T>>
         */
        protected static final <T extends AbstractCell<T>> void visit(final Set<Node<T>> gen) {
            for (final Node<T> node : gen)
                node.setState(Node.NodeState.VISITED);
        }

        @Override
        protected final Node<T> advance(final Set<Node<T>> currGen) throws StackOverflowError, InterruptedException {
            // Check for waiting state
            if (this.waiting)
                synchronized (this.lock) {
                    try {
                        this.lock.wait();
                    } catch (final InterruptedException e) {
                        JWrapper.dispatchException(e);
                    }
                }
            // Check for interruption
            if (!this.running)
                throw new InterruptedException("Invokation interrupted...");
            // Visit nodes
            Dijkstra.visit(currGen);
            // Initialize new empty generation
            final Set<Node<T>> newGen = new HashSet<Node<T>>();
            // Range through current generaton nodes cell neighbors
            for (final Node<T> node : currGen)
                for (final T cell : node.getOuter().getNeighbors()) {
                    // Set new node
                    if (cell.getInner() == null)
                        cell.setInner(new Node<T>(node, cell));
                    // Check state
                    switch (cell.getState()) {
                        case EMPTY:
                            // Visit node
                            if (cell.getInner().getState() != Node.NodeState.VISITED)
                                newGen.add(cell.getInner());
                            break;
                        case END:
                            // End reached
                            Dijkstra.visit(newGen);
                            return cell.getInner();
                        default:
                    }
                }
            if (newGen.size() == 0)
                throw new StackOverflowError("No solution...");
            // Delay iteration
            Thread.sleep(super.delay);
            // Call method recursively until convergence
            return this.advance(newGen);
        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

    // TODO: A Star PriorityQueue

}
