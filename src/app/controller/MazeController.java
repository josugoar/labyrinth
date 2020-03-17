package app.controller;

import java.awt.Cursor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import app.model.Generator;
import app.model.Generator.BackTracker;
import app.model.PathFinder;
import app.view.MazeView;

public class MazeController {

    private final MazeView view;

    private MazeModel gridPanel;
    private JTree nodeTree;
    private JLabel statusLabel;
    private JSplitPane viewWrapper;

    private Cell.State mode = Cell.State.OBSTACLE;

    private boolean diagonals = true, arrows = false;

    private int dimension = 20, speed = 20, density = 20;

    private PathFinder pathfinder = new PathFinder.Dijkstra();
    private Generator generator = new BackTracker();

    public MazeController(final MazeView mazeView) {
        this.view = mazeView;
        this.initController();
    }

    private final void initController() {
        this.view.addFocusListener(new FocusAdapter() {
            @Override
            public final void focusLost(final FocusEvent e) {
                MazeController.this.view.requestFocus();
            }
        });
        this.view.addKeyListener(new KeyAdapter() {
            @Override
            public final void keyPressed(final KeyEvent e) {
                if (e.isShiftDown()) {
                    MazeController.this.view.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
            }

            @Override
            public final void keyReleased(final KeyEvent e) {
                if (!e.isShiftDown()) {
                    MazeController.this.view.setCursor(Cursor.getDefaultCursor());
                }

            }
        });
    }

    public final MazeModel getGridPanel() {
        return this.gridPanel;
    }

    public final void setGridPanel(final MazeModel gridPanel) {
        this.gridPanel = Objects.requireNonNull(gridPanel, "'gridPanel' must not be null");
    }

    public final void resetGridPanel() {
        this.gridPanel.setGrid(this.dimension, this.dimension);
    }

    public final JTree getNodeTree() {
        return this.nodeTree;
    }

    public final void setNodeTree(final JTree nodeTree) {
        this.nodeTree = Objects.requireNonNull(nodeTree, "'nodeTree' must not be null");
    }

    public final JLabel getStatusLabel() {
        return this.statusLabel;
    }

    public final void setStatusLabel(final JLabel statusLabel) {
        this.statusLabel = Objects.requireNonNull(statusLabel, "'statusLabel' must not be null");
    }

    public final JSplitPane getViewWrapper() {
        return this.viewWrapper;
    }

    public final void setViewWrapper(final JSplitPane viewWrapper) {
        this.viewWrapper = Objects.requireNonNull(viewWrapper, "'viewWrapper' must not be null");
    }

    public final PathFinder getPathFinder() {
        return this.pathfinder;
    }

    public final void setPathFinder(final PathFinder pathfinder) {
        this.pathfinder = Objects.requireNonNull(pathfinder, "'pathfinder' must not be null");
    }

    public final void awakePathFinder() {
        this.pathfinder.awake(this.gridPanel.getGrid());
    }

    public final Generator getGenerator() {
        return this.generator;
    }

    private final void setGenerator(final Generator generator) {
        this.generator = Objects.requireNonNull(generator, "'generator' must not be null");
    }

    public final void awakeGenerator() {
        this.generator.awake();
    }

    public final Cell.State getMode() {
        return this.mode;
    }

    public final void setMode(final Cell.State mode) {
        this.mode = mode;
    }

    public final boolean isDiagonals() {
        return this.diagonals;
    }

    public final void setDiagonals(final boolean diagonals) {
        this.diagonals = diagonals;
    }

    public final boolean isArrows() {
        return this.arrows;
    }

    public final void setArrows(final boolean arrows) {
        this.arrows = arrows;
    }

    public final int getDimension() {
        return this.dimension;
    }

    public final void setDimension(final int dimension) {
        this.dimension = dimension;
        this.resetGridPanel();
    }

    public final int getSpeed() {
        return this.speed;
    }

    public final void setSpeed(final int speed) {
        this.speed = speed;
    }

    public final int getDensity() {
        return this.density;
    }

    public final void setDensity(final int density) {
        this.density = density;
    }

}
