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
import app.model.PathFinder;
import app.view.MazeView;
import app.view.components.Range;

public class MazeController {

    private final MazeView view;

    private MazeModel gridPanel;
    private JTree nodeTree;
    private JLabel statusLabel;
    private JSplitPane viewWrapper;

    private Cell.State mode = Cell.State.OBSTACLE;

    private boolean diagonals = true, arrows = false;

    private Range dimension = new Range(10, 100, 20), speed = new Range(0, 100, 10), density = new Range(1, 100, 10);

    private PathFinder pathfinder = new PathFinder.Dijkstra();
    private Generator generator = new Generator.BackTracker();

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

    public final void clearGridPanel() {

    }

    public final void resetGridPanel() {
        this.gridPanel.setGrid(this.dimension.getValue(), this.dimension.getValue());
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

    public final void cycleStatusLabel() {
        this.statusLabel.setVisible(!this.statusLabel.isVisible());
    }

    public final JSplitPane getViewWrapper() {
        return this.viewWrapper;
    }

    public final void setViewWrapper(final JSplitPane viewWrapper) {
        this.viewWrapper = Objects.requireNonNull(viewWrapper, "'viewWrapper' must not be null");
    }

    public final void cycleViewWrapper() {
        this.viewWrapper.setDividerLocation(-1);
        this.viewWrapper.setEnabled(!this.viewWrapper.isEnabled());
        this.viewWrapper.getLeftComponent().setVisible(!this.viewWrapper.getLeftComponent().isVisible());
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

    public final void setGenerator(final Generator generator) {
        this.generator = Objects.requireNonNull(generator, "'generator' must not be null");
    }

    public final void awakeGenerator() {
        this.generator.awake(this.gridPanel.getGrid());
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

    public final void cycleDiagonals() {
        this.setDiagonals(!this.diagonals);
    }

    public final boolean isArrows() {
        return this.arrows;
    }

    public final void setArrows(final boolean arrows) {
        this.arrows = arrows;
    }

    public final void cycleArrows() {
        this.setArrows(!this.arrows);
    }

    public final Range getDimension() {
        return this.dimension;
    }

    public final void setDimension(final int val) {
        this.dimension.setValue(val);
        this.resetGridPanel();
    }

    public final Range getDelay() {
        return this.speed;
    }

    public final void setDelay(final int val) {
        this.speed.setValue(val);
    }

    public final Range getDensity() {
        return this.density;
    }

    public final void setDensity(final int val) {
        this.density.setValue(val);
    }

}
