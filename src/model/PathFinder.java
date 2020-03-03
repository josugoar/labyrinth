package src.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Timer;

import src.controller.JWGrid;
import src.controller.JWGrid.Cell;
import src.controller.JWGrid.Cell.State;

/**
 * A <code>java.io.Serializable</code> abstract class containing common
 * pathfinding algorithm algorithm methods.
 *
 * @see java.io.Serializable Serializable
 */
public abstract class PathFinder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Enum of <code>src.model.PathFinder</code> algorithms.
     */
    public enum Algorithm {
        DIJKSTRA, A_STAR
    }

    /**
     * Draw delay between recursive iterations.
     */
    private static int delay = 200;

    /**
     * Run given algorithm to find last child <code>src.model.Node<Cell></code>.
     *
     * @param arrayList List<Node>
     * @return Children Node
     * @throws StackOverflowError
     */
    protected abstract void find(final Map<Point, Cell> grid, final Set<Node<Cell>> currGen) throws StackOverflowError;

    /**
     * Traverse through child nodes until no parent <code>src.model.Node</code> is
     * reached.
     *
     * @param child Node
     */
    private static final void traverse(final Node<Cell> child) {
        if (child.getParent() != null) {
            child.getInner().setState(State.PATH_NODE);
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
            this.find(grid, new HashSet<Node<Cell>>() {
                private static final long serialVersionUID = 1L;
                {
                    final boolean directAccess = true;
                    // Get starting Cell
                    final Cell start = ((JWGrid) grid.get(new Point(0, 0)).getParent()).getStart();
                    // No starting Cell
                    if (start == null) {
                        throw new NullPointerException("No starting node found...");
                        // Find starting Point
                    } else {
                        if (directAccess) {
                            for (final Point seed : grid.keySet()) {
                                if (grid.get(seed) == start) {
                                    this.add(new Node<Cell>(seed, start));
                                    break;
                                }
                            }
                        } else {
                            outer: for (final Cell cell : grid.values()) {
                                if (cell.getState() == State.START) {
                                    for (final Point seed : grid.keySet()) {
                                        if (grid.get(seed) == cell) {
                                            this.add(new Node<Cell>(seed, cell));
                                            break outer;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Self-made recursive <code>src.model.PathFinder</code> algorithm resembling
     * Dijkstra's from scratch and extending <code>src.model.PathFinder</code>.
     *
     * @see src.model.PathFinder PathFinder
     */
    public static final class Dijkstra extends PathFinder {

        private static final long serialVersionUID = -5681531921187779771L;

        @Override
        protected final void find(final Map<Point, Cell> grid, final Set<Node<Cell>> currGen)
                throws StackOverflowError {
            // Endpoint flag
            Node<Cell> endpoint = null;
            // Initialize empty new generation HashSet
            // for enhanced speeds in non-duplicate Node
            final Set<Node<Cell>> newGen = new HashSet<Node<Cell>>();
            // Range through neighbors
            for (final Node<Cell> node : currGen) {
                for (int row = node.getSeed().x - 1; row < node.getSeed().x + 2; row++) {
                    for (int col = node.getSeed().y - 1; col < node.getSeed().y + 2; col++) {
                        // Check if neighbor exists
                        if ((row < Math.sqrt(grid.size())) && (row >= 0) && (col < Math.sqrt(grid.size()))
                                && (col >= 0)) {
                            // Get Cell to check its State
                            final Cell cell = grid.get(new Point(row, col));
                            // Create new Node pointing to parent and Cell
                            final Node<Cell> newNode = new Node<Cell>(node, new Point(row, col), cell);
                            switch (cell.getState()) {
                                case EMPTY:
                                    // Store node in generation
                                    cell.setState(State.NEXT_NODE);
                                    newGen.add(newNode);
                                    break;
                                case END:
                                    // Endpoint found
                                    endpoint = newNode;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
            // Draw entire generation before returning
            if (endpoint != null) {
                PathFinder.traverse(endpoint.getParent());
                return;
            }
            // Change Cell State to visited
            final Timer timer = new Timer(PathFinder.delay, e -> {
                for (final Node<Cell> node : newGen) {
                    grid.get(node.getSeed()).setState(State.CURR_NODE);
                }
                this.find(grid, newGen);
                ((Timer) e.getSource()).stop();
            });
            timer.setRepeats(false);
            timer.start();
        }

    }

}
