package tests;

import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;

public abstract class PathFinder implements Serializable {

    public enum State {
        DIJKSTRA, ASTAR
    }

    private static final long serialVersionUID = -7742894452794723957L;

    protected static final int DELAY = 200;

    private static Grid grid = null;
    private static int iter = 0;

    public abstract Node find(final List<Node> curr_gen);

    public static final void awake(final Grid grid) {
        PathFinder.setGrid(grid);
        try {
            Node last_child = find(new ArrayList<Node>() {
                private static final long serialVersionUID = 1L;
                {
                    add(new Node(PathFinder.getGrid().getStart()));
                }
            });
            PathFinder.traverse(last_child.getParent());
        } catch (final StackOverflowError e) {
            System.out.println(e.getMessage());
        }
    }

    private static final Node traverse(final Node child) {
        if (child.getParent() != null) {
            new Timer(PathFinder.iter++ * DELAY, e -> {
                MazeApp.paintPanel(grid.getLayout().get(child.getParent().getSeed()));
                ((Timer) e.getSource()).stop();
            }).start();
            child.setPath(true);
            return PathFinder.traverse(child.getParent());
        } else {
            return child;
        }
    }

    @Override
    public final String toString() {
        return String.format("%s(root: %s)", getClass(), this.getRoot());
    }

    public static Grid getGrid() {
        return grid;
    }

    public static void setGrid(final Grid grid) {
        PathFinder.grid = grid;
    }

    public final int getIter() {
        return this.iter;
    }

    public static final class Dijkstra extends PathFinder {

        private static final long serialVersionUID = -5681531921187779771L;

        public Dijkstra(final int[][] grid, final Point root) {
            super(grid, root);
        }

    }

}
