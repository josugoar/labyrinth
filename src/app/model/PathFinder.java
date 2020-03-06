package app.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import app.MazeApp;
import app.controller.Cell;
import app.controller.Cell.State;
import app.controller.JWGrid;

/**
 * A <code>java.io.Serializable</code> abstract class containing common
 * pathfinding algorithm algorithm methods.
 *
 * @see java.io.Serializable Serializable
 */
public abstract class PathFinder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Enum of <code>app.model.PathFinder</code> algorithms.
     */
    public enum Algorithm {
        DIJKSTRA, A_STAR
    }

    /**
     * Running flag for draw loops.
     */
    protected boolean isRunning = false;

    /**
     * Run given algorithm to find last child <code>app.model.Node<Cell></code>.
     *
     * @param arrayList List<Node>
     * @throws StackOverflowError
     */
    protected abstract void find(final Map<Point, Cell> grid, final Set<Node<Cell>> currGen) throws StackOverflowError;

    /**
     * Traverse through child nodes until no parent
     * <code>app.model.Node<Cell></code> is reached.
     *
     * @param child Node
     */
    private static final void traverse(final Node<Cell> child) {
        if (child.getParent() != null) {
            child.getInner().setState(State.PATH);
            PathFinder.traverse(child.getParent());
        }
    }

    /**
     * Wrapper for finding and traversing shortest path.
     *
     * @param grid
     */
    public final void awake(final Map<Point, Cell> grid) {
        try {
            // Set running
            this.isRunning = true;
            this.find(grid, new HashSet<Node<Cell>>() {
                private static final long serialVersionUID = 1L;
                {
                    final boolean directAccess = true;
                    // Find starting Point
                    if (directAccess) {
                        // Get starting Cell
                        final Cell start = ((JWGrid) grid.get(new Point(0, 0)).getParent()).getStart();
                        // No starting Cell
                        if (start == null)
                            throw new NullPointerException("No starting node found...");
                        // Get starting Point
                        for (final Point seed : grid.keySet()) {
                            if (grid.get(seed) == start) {
                                this.add(new Node<Cell>(seed, start));
                                break;
                            }
                        }
                    } else {
                        // Get starting Cell
                        outer: for (final Cell cell : grid.values()) {
                            if (cell.getState() == State.START) {
                                // Get starting Point
                                for (final Point seed : grid.keySet()) {
                                    if (grid.get(seed) == cell) {
                                        this.add(new Node<Cell>(seed, cell));
                                        break outer;
                                    }
                                }
                            }
                        }
                        // No starting Cell
                        if (this.size() == 0)
                            throw new NullPointerException("No starting node found...");
                    }
                }
            });
        } catch (final Exception e) {
            // Redirect running
            this.isRunning = false;
            System.err.println(e.toString());
        }
    }

    public final boolean getIsRunning() {
        return this.isRunning;
    }

    /**
     * Self-made recursive <code>app.model.PathFinder</code> algorithm resembling
     * Dijkstra's from scratch and extending <code>app.model.PathFinder</code>.
     *
     * @see app.model.PathFinder PathFinder
     */
    public static final class Dijkstra extends PathFinder {

        private static final long serialVersionUID = 1L;

        @Override
        protected final void find(final Map<Point, Cell> grid, final Set<Node<Cell>> currGen)
                throws StackOverflowError {
            // Endpoint flag
            Node<Cell> endpoint = null;
            // Initialize empty new generation HashSet
            // for enhanced speed in non-duplicate Node
            final Set<Node<Cell>> newGen = new HashSet<Node<Cell>>();
            // Range through neighbors
            for (final Node<Cell> node : currGen) {
                for (int row = node.getSeed().x - 1; row < node.getSeed().x + 2; row++) {
                    for (int col = node.getSeed().y - 1; col < node.getSeed().y + 2; col++) {
                        // Check if neighbor exists
                        if ((row < Math.sqrt(grid.size())) && (row >= 0) && (col < Math.sqrt(grid.size()))
                                && (col >= 0)) {
                            // Get Point coordinates
                            final Point point = new Point(row, col);
                            // Get Cell to check its State
                            final Cell cell = grid.get(point);
                            // Create new Node pointing to parent and Cell
                            final Node<Cell> newNode = new Node<Cell>(node, point, cell);
                            switch (cell.getState()) {
                                case EMPTY:
                                    // Store node in generation
                                    cell.setState(State.GERMINATED);
                                    newGen.add(newNode);
                                    break;
                                case END:
                                    // Endpoint found
                                    endpoint = newNode;
                                    this.isRunning = false;
                                    break;
                                default:
                            }
                        }
                    }
                }
            }
            // Handle no solution grid
            if (currGen.equals(newGen))
                throw new StackOverflowError("No solution...");
            // Draw entire generation before returning
            if (endpoint != null) {
                PathFinder.traverse(endpoint.getParent());
                return;
                // Check for final generation
            } else if (newGen.size() != 0) {
                // Invert speed parameter
                new Timer(((MazeApp) SwingUtilities.getWindowAncestor(newGen.iterator().next().getInner())).getSpeed(),
                        e -> {
                            // Change Cell State to visited
                            for (final Node<Cell> node : newGen) {
                                grid.get(node.getSeed()).setState(State.VISITED);
                            }
                            // Call method recursively until convergence
                            this.find(grid, newGen);
                            ((Timer) e.getSource()).stop();
                        }).start();
            }
        }

    }

}
