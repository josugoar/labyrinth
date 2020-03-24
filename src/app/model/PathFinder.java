package app.model;

import java.awt.Component;
import java.awt.Point;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashSet;
import java.util.Set;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;
import app.model.components.Node;
import app.model.components.Node.NodeState;

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
     * Flag for algorithm running state.
     */
    protected boolean isRunning = false;

    /**
     * Starting endpoint pointer.
     */
    protected Point start;

    /**
     * Ending endpoint pointer.
     */
    protected Point end;

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
    protected static final <T extends AbstractCell<T>> void visitGeneration(final Set<Node<T>> gen) {
        for (final Node<T> node : gen)
            node.setState(Node.NodeState.VISITED);
    }

    /**
     * Store endpoints for performing later comparisons.
     *
     * @param start Point
     * @param end   Point
     * @throws NullPointerException if (start == null || end == null)
     */
    private final void setEndpoints(final Point start, final Point end) throws NullPointerException {
        if (start == null || end == null)
            throw new NullPointerException("No endpoint found...");
        this.start = start;
        this.end = end;
    }

    @Override
    public final <T extends AbstractCell<T>> void awake(final T[][] grid, final Point start, final Point end) {
        // Invoke new Thread
        new Thread(() -> {
            try {
                // Store endpoints
                this.setEndpoints(start, end);
                // Find child and traverse tree
                PathFinder.traverse(this.advance(grid, new HashSet<Node<T>>() {
                    private static final long serialVersionUID = 1L;
                    {
                        // Construct first generation
                        this.add(new Node<T>(grid[start.x][start.y]));
                        // Start running
                        PathFinder.this.setIsRunning(true);
                    }
                }));
            } catch (NullPointerException | StackOverflowError | InterruptedException e) {
                System.err.println(e.toString());
            } finally {
                this.setIsRunning(false);
            }
        }).start();
    }

    @Override
    public final String getAlgorithm() {
        return this.getClass().getSimpleName();
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
            // Visit nodes
            super.visitGeneration(currGen);
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
                            super.visitGeneration(newGen);
                            return cell.getInner();
                        default:
                    }
                }
            if (newGen.size() == 0)
                throw new StackOverflowError("No solution...");
            if (!this.isRunning)
                throw new InterruptedException("Invokation interrupted...");
            // Delay iteration
            Thread.sleep((((MazeModel) ((Component) newGen.iterator().next().getOuter()).getParent()).getController()
                    .getDelay().getValue()));
            // Call method recursively until convergence
            return this.advance(grid, newGen);
        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
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

    }

}
