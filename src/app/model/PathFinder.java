package app.model;

import java.awt.Component;
import java.io.Serializable;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Timer;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;
import app.controller.components.AbstractCell.CellState;
import app.model.components.Node;
import app.model.components.Node.NodeState;

public abstract class PathFinder implements AbstractAlgorithm, Serializable {

    // TODO: Refactor PathFinder

    private static final long serialVersionUID = 1L;

    protected boolean isRunning = false;

    protected abstract <T extends AbstractCell<T>> void find(final T[][] grid, final Set<Node<T>> currGen)
            throws StackOverflowError;

    public static final <T extends AbstractCell<T>> void traverse(final Node<T> child) {
        if (child.getParent() != null) {
            child.setState(NodeState.PATH);
            PathFinder.traverse(child.getParent());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T extends AbstractCell<T>> void awake(final T[][] grid) throws NullPointerException {
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
                            final T cell = grid[row][col];
                            if (cell.getState() == CellState.START) {
                                this.add(new Node<T>(cell));
                                break outer;
                            }
                        }
                    }
                    if (this.size() == 0) {
                        PathFinder.this.setIsRunning(false);
                        throw new NullPointerException("No starting node found...");
                    }
                }
                PathFinder.this.setIsRunning(true);
            }
        });
    }

    @Override
    public final String getAlgorithm() {
        return this.getClass().getSimpleName();
    }

    public static final class Dijkstra extends PathFinder {

        private static final long serialVersionUID = 1L;

        @Override
        protected final <T extends AbstractCell<T>> void find(final T[][] grid, final Set<Node<T>> currGen)
                throws StackOverflowError {
            final Set<Node<T>> newGen = new HashSet<Node<T>>();
            for (final Node<T> node : currGen) {
                for (final T cell : node.getOuter().getNeighbors()) {
                    if (cell.getInner() == null)
                        cell.setInner(new Node<T>(node, cell));
                    switch (cell.getState()) {
                        case EMPTY:
                            if (cell.getInner().getState() != Node.NodeState.VISITED) {
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
                throw new StackOverflowError("No solution...");
            }
            if (!this.isRunning) {
                return;
            }
            new Timer((((MazeModel) ((Component) newGen.iterator().next().getOuter()).getParent()).getController().getDelay().getValue()), e -> {
                try {
                    for (final Node<T> node : newGen) {
                        node.setState(Node.NodeState.VISITED);
                    }
                    this.find(grid, newGen);
                } catch (final StackOverflowError l) {
                    this.setIsRunning(false);
                    System.err.println(l.toString());
                } finally {
                    ((Timer) e.getSource()).stop();
                }
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

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

}
