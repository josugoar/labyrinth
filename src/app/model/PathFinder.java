package app.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import app.controller.Cell;
import app.controller.MazeModel;
import app.view.MazeView;

public abstract class PathFinder implements Serializable {

    private static final long serialVersionUID = 1L;

    protected boolean isRunning = false;

    public abstract void setIsRunning(final boolean isRunning);

    protected abstract void find(final Cell[][] grid, final Set<Node> currGen) throws StackOverflowError;

    private static final void traverse(final Node child) {
        if (child.getParent() != null) {
            child.setState(Node.State.PATH);
            PathFinder.traverse(child.getParent());
        }
    }

    public final void awake(final Cell[][] grid) {
        try {
            this.setIsRunning(true);
            this.find(grid, new HashSet<Node>() {
                private static final long serialVersionUID = 1L;
                {
                    final boolean directAccess = true;
                    if (directAccess) {
                        final Cell start = ((MazeModel) grid[0][0].getParent()).getStart();
                        if (start == null)
                            throw new NullPointerException("No starting node found...");
                        this.add(new Node(null, start));
                    } else {
                        outer: for (int row = 0; row < grid.length; row++) {
                            for (int col = 0; col < grid.length; col++) {
                                final Cell cell = grid[row][col];
                                if (cell.getState() == Cell.State.START) {
                                    this.add(new Node(null, cell));
                                    break outer;
                                }
                            }
                        }
                        if (this.size() == 0)
                            throw new NullPointerException("No starting node found...");
                    }
                }
            });
        } catch (final StackOverflowError e) {
            System.err.println(e.toString());
        }
    }

    public final boolean getIsRunning() {
        return this.isRunning;
    }

    public static final class Dijkstra extends PathFinder {

        private static final long serialVersionUID = 1L;

        @Override
        protected final void find(final Cell[][] grid, final Set<Node> currGen) throws StackOverflowError {
            final Set<Node> newGen = new HashSet<Node>();
            for (final Node node : currGen) {
                for (Cell cell : node.getOuter().getNeighbors().keySet()) {
                    if (cell.getInner() == null)
                        cell.setInner(new Node(node, cell));
                    switch (cell.getState()) {
                        case EMPTY:
                            if (cell.getInner().getState() != Node.State.VISITED) {
                                newGen.add(cell.getInner());
                            }
                            break;
                        case END:
                            this.setIsRunning(false);
                            PathFinder.traverse(cell.getInner().getParent());
                            break;
                        default:
                    }
                }
            }
            if (newGen.size() == 0) {
                this.setIsRunning(false);
                throw new StackOverflowError("No solution...");
            }
            if (!this.isRunning) {
                return;
            }
            new Timer(((MazeView) SwingUtilities.getWindowAncestor(newGen.iterator().next().getOuter())).getController().getSpeed(),
                    e -> {
                        for (final Node node : newGen) {
                            node.setState(Node.State.VISITED);
                        }
                        this.find(grid, newGen);
                        ((Timer) e.getSource()).stop();
                    }).start();
        }

        @Override
        public final void setIsRunning(final boolean isRunning) {
            // TODO: Glass pane
            this.isRunning = isRunning;
        }

    }

}
