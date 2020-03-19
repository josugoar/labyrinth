package app.controller;

import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import app.model.Generator;
import app.model.MazeModel;
import app.model.PathFinder;
import app.view.MazeView;
import app.view.components.JWSlider.JWRange;

/**
 * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
 * pivotal <code>app.controller.MazeController</code> component, handling
 * <code>app.controller.MazeModel</code> and <code>app.view.MazeView</code>
 * multiple interactions.
 *
 * @author JoshGoA
 * @version 0.1
 * @see app.model.MazeModel MazeModel
 * @see app.view.MazeView MazeView
 */
public class MazeController {

    /**
     * Two-sided <code>app.model.MazeModel</code>
     * <code>app.controller.MazeController</code> interaction pipeline.
     *
     * @see app.model.MazeModel MazeModel
     */
    private final MazeModel model;

    /**
     * Two-sided <code>app.view.MazeView</code>
     * <code>app.controller.MazeController</code> interaction pipeline.
     *
     * @see app.view.MazeView MazeView
     */
    private final MazeView view;

    /**
     * <code>javax.swing.JTree</code> component displaying
     * <code>app.controller.Cell</code> and <code>app.model.Node</code> child
     * generations.
     *
     * @see javax.swing.JTree JTree
     * @see app.controller.Cell Cell
     * @see app.model.Node Node
     */
    private JTree treeComponent;

    /**
     * <code>javax.swing.JLabel</code> component displaying custom application
     * output messages.
     *
     * @see javax.swing.JLabel JLabel
     */
    private JLabel statusComponent;

    /**
     * <code>javax.swing.JSplitPane</code> component responsible of visual
     * interactions.
     *
     * @see javax.swing.JSplitPane JSplitPane
     */
    private JSplitPane splitComponent;

    /**
     * Current user input mode selection.
     *
     * @see app.controller.Cell.State State
     */
    private Cell.State mode = Cell.State.OBSTACLE;

    /**
     * Diagonal tile trasversal flag.
     */
    private boolean diagonals = true;

    /**
     * Arrow draw flag.
     */
    private boolean arrows = false;

    /**
     * <code>app.model.MazeModel</code> dimension resizing
     * <code>app.view.components.JWSlider.JWRange</code>.
     *
     * @see app.view.components.JWSlider.JWRange JWRange
     */
    private JWRange dimension = new JWRange(10, 100, 20);

    /**
     * Delay <code>app.view.components.JWSlider.JWRange</code> between draw cycles.
     *
     * @see app.view.components.JWSlider.JWRange JWRange
     */
    private JWRange delay = new JWRange(0, 250, 100);

    /**
     * Maze <code>Cell.State.OBSTACLE</code> <code>app.model.Generator</code>
     * density.
     *
     * @see app.view.components.JWSlider.JWRange JWRange
     */
    private JWRange density = new JWRange(1, 100, 10);

    /**
     * Current maze trasversal <code>app.model.PathFinder</code> algorithm.
     *
     * @see app.model.PathFinder PathFinder
     */
    private PathFinder pathfinder = new PathFinder.Dijkstra();

    /**
     * Current maze generation <code>app.model.Generator</code> algorithm.
     *
     * @see app.model.Generator Generator
     */
    private Generator generator = new Generator.BackTracker();

    /**
     * Create a new two-sided <code>app.controller.MazeController</code> and
     * <code>app.view.MazeView</code> interaction
     * <code>app.controller.MazeController</code> component.
     */
    public MazeController() {
        this.model = new MazeModel(this, this.dimension.getValue(), this.dimension.getValue());
        // Always initialize MazeView after MazeModel
        this.view = new MazeView(this);
    }

    /**
     * Run MazeApp.
     */
    public final void run() {
        SwingUtilities.invokeLater(view);
    }

    /**
     * Ruturn current <code>app.model.MazeModel</code> instance.
     *
     * @return MazeModel
     */
    public final MazeModel getModel() {
        return this.model;
    }

    /**
     * Fire <code>app.model.MazeModel.clear(Cell parent)</code> event.
     */
    public final void clearModel() {
        if (this.model.getStart() != null)
            MazeModel.clear(this.model.getStart());
        else
            this.statusComponent.setText("No nodes to clear...");
    }

    /**
     * Fire <code>app.model.MazeModel.reset()</code> event.
     */
    public final void resetModel() {
        this.model.reset();
    }

    /**
     * Ruturn current <code>app.view.MazeView</code> instance.
     *
     * @return MazeView
     */
    public final MazeView getView() {
        return this.view;
    }

    /**
     * Retun current <code>javax.swing.JTree</code> instance.
     *
     * @return JTree
     */
    public final JTree getTreeComponent() {
        return this.treeComponent;
    }

    /**
     * Set current <code>javax.swing.JTree</code> instance.
     */
    public final void setTreeComponent(final JTree treeComponent) {
        this.treeComponent = Objects.requireNonNull(treeComponent, "'treeComponent' must not be null");
    }

    /**
     * Return current <code>javax.swing.JLabel</code> instance.
     *
     * @return JLabel
     */
    public final JLabel getStatusComponent() {
        return this.statusComponent;
    }

    /**
     * Set current <code>javax.swing.JLabel</code> instance.
     */
    public final void setStatusComponent(final JLabel statusComponent) {
        this.statusComponent = Objects.requireNonNull(statusComponent, "'statusComponent' must not be null");
    }

    /**
     * Cycle between <code>javax.swing.JLabel</code>
     * <code>java.awt.Component.isVisible()</code> states.
     */
    public final void cycleStatusComponent() {
        this.statusComponent.setVisible(!this.statusComponent.isVisible());
    }

    /**
     * Reset current <code>javax.swing.JLabel</code> instance custom application
     * output message.
     */
    public final void resetStatusComponent() {
        new Timer(2500, e -> {
            // Set text to default state
            this.statusComponent.setText(this.toString());
            ((Timer) e.getSource()).stop();
        }).start();
    }

    /**
     * Return current <code>javax.swing.JSplitPane</code> instance.
     *
     * @return JSplitPane
     */
    public final JSplitPane getSplitComponent() {
        return this.splitComponent;
    }

    /**
     * Set current <code>javax.swing.JSplitPane</code> instance.
     */
    public final void setSplitComponent(final JSplitPane splitComponent) {
        this.splitComponent = Objects.requireNonNull(splitComponent, "'splitComponent' must not be null");
    }

    /**
     * Cycle between <code>javax.swing.JSplitPane</code>
     * <code>java.awt.Component.isVisible()</code> and
     * <code>java.awt.Component.isEnabled()</code> states and update
     * <code>javax.swing.JSplitPane.setDividerLocation(int location)</code>.
     */
    public final void cycleSplitComponent() {
        this.splitComponent.getLeftComponent().setVisible(!this.splitComponent.getLeftComponent().isVisible());
        this.splitComponent.setDividerLocation(-1);
        this.splitComponent.setEnabled(!this.splitComponent.isEnabled());
    }

    /**
     * Return current <code>app.model.PathFinder</code> instance.
     *
     * @return PathFinder
     */
    public final PathFinder getPathFinder() {
        return this.pathfinder;
    }

    /**
     * Set current <code>app.model.PathFinder</code> instance.
     */
    public final void setPathFinder(final PathFinder pathfinder) {
        this.pathfinder = Objects.requireNonNull(pathfinder, "'pathfinder' must not be null");
    }

    /**
     * Fire <code>app.model.PathFinder.awake(Cell[][] grid)</code> event.
     */
    public final void awakePathFinder() {
        this.pathfinder.awake(this.model.getGrid());
    }

    /**
     * Return current <code>app.model.Generator</code> instance.
     *
     * @return Generator
     */
    public final Generator getGenerator() {
        return this.generator;
    }

    /**
     * Set current <code>app.model.Generator</code> instance.
     */
    public final void setGenerator(final Generator generator) {
        this.generator = Objects.requireNonNull(generator, "'generator' must not be null");
    }

    /**
     * Fire <code>app.model.Generator.awake(Cell[][] grid)</code> event.
     */
    public final void awakeGenerator() {
        this.generator.awake(this.model.getGrid());
    }

    /**
     * Return current <code>app.controller.Cell</code>
     * <code>app.controller.Cell.State</code>.
     *
     * @return Cell.State
     */
    public final Cell.State getMode() {
        return this.mode;
    }

    /**
     * Set current <code>app.controller.Cell</code>
     * <code>app.controller.Cell.State</code>.
     *
     * @param mode Cell.State
     */
    public final void setMode(final Cell.State mode) {
        this.mode = mode;
    }

    /**
     * Return current diagonals state.
     *
     * @return boolean
     */
    public final boolean isDiagonals() {
        return this.diagonals;
    }

    /**
     * Set current diagonals state.
     *
     * @param diagonals boolean
     */
    public final void setDiagonals(final boolean diagonals) {
        this.diagonals = diagonals;
    }

    /**
     * Cycle between diagonals states.
     */
    public final void cycleDiagonals() {
        this.setDiagonals(!this.diagonals);
    }

    /**
     * Return current arrows state.
     *
     * @return boolean
     */
    public final boolean isArrows() {
        return this.arrows;
    }

    /**
     * Set current arrows state.
     *
     * @param arrows boolean
     */
    public final void setArrows(final boolean arrows) {
        this.arrows = arrows;
    }

    /**
     * Cycle between arrows states.
     */
    public final void cycleArrows() {
        this.setArrows(!this.arrows);
    }

    /**
     * Return current dimension <code>app.view.components.JWSlider.JWRange</code>.
     *
     * @return JWRange
     */
    public final JWRange getDimension() {
        return this.dimension;
    }

    /**
     * Set current dimension <code>app.view.components.JWSlider.JWRange</code> value
     * and fire <code>app.controller.MazeModel.setGrid(int rows, int cols)</code>
     * event.
     *
     * @param val int
     */
    public final void setDimension(final int val) {
        this.dimension.setValue(val);
        this.model.setGrid(this.dimension.getValue(), this.dimension.getValue());
    }

    /**
     * Return current delay <code>app.view.components.JWSlider.JWRange</code>.
     *
     * @return JWRange
     */
    public final JWRange getDelay() {
        return this.delay;
    }

    /**
     * Set current delay <code>app.view.components.JWSlider.JWRange</code> value.
     *
     * @param val int
     */
    public final void setDelay(final int val) {
        this.delay.setValue(val);
    }

    /**
     * Return current density <code>app.view.components.JWSlider.JWRange</code>.
     *
     * @return JWRange
     */
    public final JWRange getDensity() {
        return this.density;
    }

    /**
     * Set current density <code>app.view.components.JWSlider.JWRange</code> value.
     *
     * @param val int
     */
    public final void setDensity(final int val) {
        this.density.setValue(val);
    }

    /**
     * Return a String representing the maze.
     *
     * @return String
     */
    @Override
    public final String toString() {
        return String.format("Maze");
    }

}
