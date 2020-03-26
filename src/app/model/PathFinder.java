package app.model;

import java.awt.Point;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashSet;
import java.util.Set;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;
import app.model.components.Node;
import app.model.components.Node.NodeState;
import app.view.components.RangedSlider.BoundedRange;
import utils.JWrapper;

/**
 * PathFinding algorithm abstract wrapper, implementing
 * <code>app.controller.components.AbstractAlgorithm</code>.
 *
 * @see app.controller.components.AbstractAlgorithm AbstractAlgorithm
 * @see app.controller.components.AbstractCell AbstractCell
 * @see app.model.components.Node Node
 */
public abstract class PathFinder implements AbstractAlgorithm {

    private static final long serialVersionUID = 1L;

    /**
     * Ending endpoint pointer.
     */
    protected Point target;

    /**
     * Flag for algorithm running state.
     */
    protected volatile boolean isRunning = false;

    /**
     * Delay <code>app.view.components.RangedSlider.BoundedRange</code> between draw
     * cycles.
     *
     * @see app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    private final BoundedRange delay = new BoundedRange(0, 250, 100);

    /**
     * Recursively iterate over generations using
     * <code>app.model.components.Node</code> priority queue.
     *
     * @param <T>     AbstractCell<T>
     * @param grid    T[][]
     * @param currGen Set<Node<T>>Set<Node<T>>
     * @throws StackOverflowError   if (newGen.size() == 0)
     * @throws InterruptedException if (!isRunning)
     */
    protected abstract <T extends AbstractCell<T>> Node<T> advance(final T[][] grid, final Set<Node<T>> currGen)
            throws StackOverflowError, InterruptedException;

    /**
     * Iterate over all parents of
     * <code>app.controller.components.AbstractCell</code>
     * <code>app.model.components.Node</code>.
     *
     * @param <T>   AbstractCell<T>
     * @param child Node<T>
     */
    public static final <T extends AbstractCell<T>> void traverse(final Node<T> child) {
        if (child.getParent() != null) {
            child.setState(NodeState.PATH);
            PathFinder.traverse(child.getParent());
        }
    }

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
    public final <T extends AbstractCell<T>> void awake(final T[][] grid, final Point start, final Point end) {
        // Invoke new Thread
        new Thread(() -> {
            try {
                if (start == null)
                    throw new NullPointerException("No starting node found...");
                try {
                    final T startCell = grid[start.x][start.y];
                    // Store target which could not belong to array
                    this.setTarget(end);
                    // Find child and traverse tree
                    PathFinder.traverse(this.advance(grid, new HashSet<Node<T>>() {
                        private static final long serialVersionUID = 1L;
                        {
                            // Construct first generation
                            this.add(new Node<T>(startCell));
                            // Start running
                            PathFinder.this.setIsRunning(true);
                        }
                    }));
                } catch (final IndexOutOfBoundsException e) {
                    throw new IndexOutOfBoundsException("Start does not belong to array...");
                }
            } catch (NullPointerException | StackOverflowError | InterruptedException e) {
                JWrapper.dispatchException(e);
            } finally {
                this.setIsRunning(false);
            }
        }).start();
    }

    /**
     * Store target for later comparisons.
     *
     * @param target Point
     */
    private final void setTarget(final Point target) {
        this.target = target;
    }

    @Override
    public final boolean getIsRunning() {
        return this.isRunning;
    }

    @Override
    public final void setIsRunning(final boolean isRunning) {
        // TODO: Glass pane
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.isRunning ? 1231 : 1237);
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
        if (this.isRunning != other.isRunning)
            return false;
        if (this.target == null)
            if (other.target != null)
                return false;
        else if (!this.target.equals(other.target))
            return false;
        return true;
    }

    @Override
    public final String toString() {
        return this.getAlgorithm();
    }

    /**
     * Dijkstra pathfinding algorithm implementation, extending
     * <code>app.model.PathFinder</code>.
     *
     * @see app.model.PathFinder PathFinder
     */
    public static final class Dijkstra extends PathFinder {

        private static final long serialVersionUID = 1L;

        @Override
        protected final <T extends AbstractCell<T>> Node<T> advance(final T[][] grid, final Set<Node<T>> currGen)
                throws StackOverflowError, InterruptedException {
            if (!this.isRunning)
                throw new InterruptedException("Invokation interrupted...");
            // Visit nodes
            super.visit(currGen);
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
                            super.visit(newGen);
                            return cell.getInner();
                        default:
                    }
                }
            if (newGen.size() == 0)
                throw new StackOverflowError("No solution...");
            // Delay iteration
            Thread.sleep(super.delay.getValue());
            // Call method recursively until convergence
            return this.advance(grid, newGen);
        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

    // TODO: A Star PriorityQueue

}
