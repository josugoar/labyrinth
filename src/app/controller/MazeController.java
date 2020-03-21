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
import app.model.components.CellPanel;
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
     * <code>app.model.components.CellPanel</code> and
     * <code>app.model.components.Node</code> child generations.
     *
     * @see javax.swing.JTree JTree
     * @see app.model.components.CellPanel CellPanel
     * @see app.model.components.Node Node
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
     * @see app.model.ColoredState.CellPanel.State State
     */
    private CellPanel.CellState mode = CellPanel.CellState.OBSTACLE;

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
     * Maze <code>app.model.components.CellPanel.State.OBSTACLE</code>
     * <code>app.model.Generator</code> density.
     *
     * @see app.view.components.JWSlider.JWRange JWRange
     */
    private JWRange density = new JWRange(1, 100, 10);

    /**
     * Create a new two-sided <code>app.model.MazeModel</code> and
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
     * Fire <code>app.model.MazeModel.clear(CellPanel parent)</code> event.
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
        this.splitComponent.setDividerLocation(-1);
        this.splitComponent.setEnabled(!this.splitComponent.isEnabled());
        this.splitComponent.getLeftComponent().setVisible(!this.splitComponent.getLeftComponent().isVisible());
    }

    /**
     * Return current <code>app.model.components.CellPanel</code>
     * <code>app.model.components.CellPanel.State</code>.
     *
     * @return CellPanel.State
     */
    public final CellPanel.CellState getMode() {
        return this.mode;
    }

    /**
     * Set current <code>app.model.components.CellPanel</code>
     * <code>app.model.components.CellPanel.State</code>.
     *
     * @param mode CellPanel.State
     */
    public final void setMode(final CellPanel.CellState mode) {
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
     * and fire <code>app.model.MazeModel.setGrid(int rows, int cols)</code> event.
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
     * Request current <code>app.model.PathFinder</code> instance.
     *
     * @return PathFinder
     */
    public final PathFinder requestPathFinder() {
        return this.model.getPathFinder();
    }

    /**
     * Update current <code>app.model.PathFinder</code> instance.
     */
    public final void updatePathFinder(final PathFinder pathfinder) {
        this.model.setPathFinder(pathfinder);
    }

    /**
     * Run current <code>app.model.PathFinder</code> instance.
     */
    public final void runPathFinder() {
        this.clearModel();
        this.model.awakePathFinder();
    }

    /**
     * Request current <code>app.model.Generator</code> instance.
     *
     * @return Generator
     */
    public final Generator requestGenerator() {
        return this.model.getGenerator();
    }

    /**
     * Update current <code>app.model.Generator</code> instance.
     */
    public final void updateGenerator(final Generator generator) {
        this.model.setGenerator(generator);
    }

    /**
     * Run current <code>app.model.Generator</code> instance.
     */
    public final void runGenerator() {
        this.model.awakeGenerator();
    }

    @Override
    public final String toString() {
        return String.format("Maze");
    }

}
