package app.model;

import java.awt.Component;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;
import app.model.components.CellPanel;
import app.model.components.Node;
import app.view.MazeView;

public abstract class PathFinder extends AbstractAlgorithm implements Serializable {

    private static final long serialVersionUID = 1L;

    protected boolean isRunning = false;

    public abstract boolean getIsRunning();

    public abstract void setIsRunning(final boolean isRunning);

    protected abstract <T extends AbstractCell<T>> void find(final T[][] grid, final Set<Node<T>> currGen) throws StackOverflowError;

    private static final <T extends AbstractCell<T>> void traverse(final Node<T> child) {
        if (child.getParent() != null) {
            child.setState(Node.NodeState.PATH);
            PathFinder.traverse(child.getParent());
        }
    }

    @Override
    public final <T extends AbstractCell<T>> void awake(final T[][] grid) {
        try {
            this.setIsRunning(true);
            this.find(grid, new HashSet<Node<T>>() {
                private static final long serialVersionUID = 1L;
                {
                    final boolean directAccess = true;
                    if (directAccess) {
                        final T start = (T) ((MazeModel) ((Component) grid[0][0]).getParent()).getStart();
                        if (start == null) {
                            PathFinder.this.setIsRunning(false);
                            throw new NullPointerException("No starting node found...");
                        }
                        this.add(new Node<T>(start));
                    } else {
                        outer: for (int row = 0; row < grid.length; row++) {
                            for (int col = 0; col < grid.length; col++) {
                                final T CellPanel = grid[row][col];
                                if (CellPanel.getState() == AbstractCell.CellState.START) {
                                    this.add(new Node<T>(CellPanel));
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

    public static final class Dijkstra extends PathFinder {

        private static final long serialVersionUID = 1L;

        @Override
        protected final <T extends AbstractCell<T>> void find(final T[][] grid, final Set<Node<T>> currGen)
                throws StackOverflowError {
            final Set<Node<T>> newGen = new HashSet<Node<T>>();
            for (final Node<T> node : currGen) {
                for (T CellPanel : node.getOuter().getNeighbors()) {
                    if (CellPanel.getInner() == null)
                        CellPanel.setInner(new Node<T>(node, CellPanel));
                    switch (CellPanel.getState()) {
                        case EMPTY:
                            if (CellPanel.getInner().getState() != Node.NodeState.VISITED) {
                                newGen.add(CellPanel.getInner());
                            }
                            break;
                        case END:
                            this.setIsRunning(false);
                            PathFinder.traverse(CellPanel.getInner().getParent());
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
            new Timer(((MazeView) SwingUtilities.getWindowAncestor((Component) newGen.iterator().next().getOuter()))
                    .getController().getDelay().getValue(), e -> {
                        for (final Node<T> node : newGen) {
                            node.setState(Node.NodeState.VISITED);
                        }
                        this.find(grid, newGen);
                        ((Timer) e.getSource()).stop();
                    }).start();
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
