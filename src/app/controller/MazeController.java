package app.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.Timer;

import app.controller.components.AbstractCell.CellState;
import app.model.Generator;
import app.model.MazeModel;
import app.model.PathFinder;
import app.model.components.CellPanel;
import app.view.MazeView;
import app.view.components.RangedSlider.BoundedRange;

/**
 * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
 * pivotal <code>app.controller.MazeController</code> component, handling
 * multiple pivotal interactions, implementing <code>java.io.Serializable</code>.
 *
 * @author JoshGoA
 * @version 0.1
 * @see java.io.Serializable Serializable
 */
public class MazeController implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Two-sided <code>app.model.MazeModel</code>
     * <code>app.controller.MazeController</code> interaction pipeline.
     *
     * @see app.model.MazeModel MazeModel
     */
    private MazeModel model;

    /**
     * Two-sided <code>app.view.MazeView</code>
     * <code>app.controller.MazeController</code> interaction pipeline.
     *
     * @see app.view.MazeView MazeView
     */
    private MazeView view;

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
     * @see app.model.components.CellPanel.CellState CellState
     * @deprecated Draw cycle made by mouse input
     */
    private CellState mode = CellState.OBSTACLE;

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
     * <code>app.view.components.RangedSlider.BoundedRange</code>.
     *
     * @see app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    private final BoundedRange dimension = new BoundedRange(10, 50, 20);

    /**
     * Delay <code>app.view.components.RangedSlider.BoundedRange</code> between draw
     * cycles.
     *
     * @see app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    private final BoundedRange delay = new BoundedRange(0, 250, 100);

    /**
     * Maze <code>app.model.components.CellPanel.CellState.OBSTACLE</code>
     * <code>app.model.Generator</code> density.
     *
     * @see app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    private final BoundedRange density = new BoundedRange(1, 100, 10);

    /**
     * Create a new isolated pipeline component.
     */
    public MazeController() { }

    /**
     * Create a new two-sided <code>app.model.MazeModel</code> and
     * <code>app.view.MazeView</code> interaction
     * <code>app.controller.MazeController</code> component.
     *
     * @param model MazeModel
     * @param view  MazeView
     */
    public MazeController(final MazeModel model, final MazeView view) {
        this.setModel(model);
        this.setView(view);
    }

    /**
     * Run MazeApp.
     */
    public final void run() {
        this.model.setGrid(this.dimension.getValue(), this.dimension.getValue());
        this.view.display();
    }

    /**
     * Fire <code>app.model.MazeModel.reset()</code> event.
     */
    public final void reset() {
        this.model.reset();
    }

    /**
     * Request <code>app.model.MazeModel.clear()</code> event.
     *
     * @throws NullPointerException if (model.getStart().getInner() == null)
     */
    public final void clear() {
        if (this.model.getStart().getInner() == null)
            throw new NullPointerException("No nodes to clear...");
        this.model.clear();
    }

    /**
     * Fire <code>app.view.MazeView.releaseCellPopup(CellPanel cell)</code> event.
     *
     * @param cell CellPanel
     * @return JPopupMenu
     */
    public final JPopupMenu releaseCellPopup(final CellPanel cell) {
        try {
            return this.view.releaseCellPopup(cell);
        } catch (final InvalidParameterException e) {
            System.err.println(e.toString());
            return null;
        }
    }

    /**
     * Return current <code>app.model.MazeModel</code> instance.
     *
     * @return MazeModel
     */
    public final MazeModel getModel() {
        return this.model;
    }

    /**
     * Set current <code>app.model.MazeModel</code> instance.
     *
     * @param model MazeModel
     */
    public final void setModel(final MazeModel model) {
        this.model = Objects.requireNonNull(model, "'model' must not be null");
    }

    /**
     * Return current <code>app.view.MazeView</code> instance.
     *
     * @return MazeView
     */
    public final MazeView getView() {
        return this.view;
    }

    /**
     * Set current <code>app.view.MazeView</code> instance.
     *
     * @param view MazeView
     */
    public final void setView(final MazeView view) {
        this.view = Objects.requireNonNull(view, "'view' must not be null");
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

    // TODO: resetStatusComponent
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
     * <code>app.model.components.CellPanel.CellState</code>.
     *
     * @return CellPanel.CellState
     * @deprecated Draw cycle made by mouse input
     */
    public final CellState getMode() {
        return this.mode;
    }

    /**
     * Set current <code>app.model.components.CellPanel</code>
     * <code>app.model.components.CellPanel.CellState</code>.
     *
     * @param mode CellState
     * @deprecated Draw cycle made by mouse input
     */
    public final void setMode(final CellState mode) {
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
     * Return current dimension
     * <code>app.view.components.RangedSlider.BoundedRange</code>.
     *
     * @return BoundedRange
     */
    public final BoundedRange getDimension() {
        return this.dimension;
    }

    /**
     * Set current dimension
     * <code>app.view.components.RangedSlider.BoundedRange</code> value and fire
     * <code>app.model.MazeModel.setGrid(int rows, int cols)</code> event.
     *
     * @param val int
     */
    public final void setDimension(final int val) {
        this.dimension.setValue(val);
        this.model.setGrid(this.dimension.getValue(), this.dimension.getValue());
        this.reset();
    }

    /**
     * Return current delay
     * <code>app.view.components.RangedSlider.BoundedRange</code>.
     *
     * @return BoundedRange
     */
    public final BoundedRange getDelay() {
        return this.delay;
    }

    /**
     * Set current delay <code>app.view.components.RangedSlider.BoundedRange</code>
     * value.
     *
     * @param val int
     */
    public final void setDelay(final int val) {
        this.delay.setValue(val);
    }

    /**
     * Return current density
     * <code>app.view.components.RangedSlider.BoundedRange</code>.
     *
     * @return BoundedRange
     */
    public final BoundedRange getDensity() {
        return this.density;
    }

    /**
     * Set current density
     * <code>app.view.components.RangedSlider.BoundedRange</code> value.
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
    public final PathFinder getPathFinder() {
        return this.model.getPathFinder();
    }

    /**
     * Update current <code>app.model.PathFinder</code> instance.
     */
    public final void setPathFinder(final PathFinder pathfinder) {
        this.model.setPathFinder(pathfinder);
    }

    /**
     * Run current <code>app.model.PathFinder</code> instance.
     */
    public final void runPathFinder() {
        this.model.clear();
        this.model.awakePathFinder();
    }

    /**
     * Request current <code>app.model.Generator</code> instance.
     *
     * @return Generator
     */
    public final Generator getGenerator() {
        return this.model.getGenerator();
    }

    /**
     * Update current <code>app.model.Generator</code> instance.
     */
    public final void setGenerator(final Generator generator) {
        this.model.setGenerator(generator);
    }

    /**
     * Run current <code>app.model.Generator</code> instance.
     */
    public final void runGenerator() {
        this.model.clear();
        this.model.awakeGenerator();
    }

    // TODO: Fix serialization
    public final void readMaze() {
        try {
            final FileInputStream file = new FileInputStream("test.ser");
            final ObjectInputStream in = new ObjectInputStream(file);
            this.model.override((MazeModel) in.readObject());
            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.toString());
        }
    }

    public final void writeMaze() {
        try {
            final FileOutputStream file = new FileOutputStream("test.ser");
            final ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this.model);
            out.close();
            file.close();
        } catch (final IOException e) {
            System.err.println(e.toString());
        }
    }

    @Override
    public final String toString() {
        return String.format("Maze");
    }

}
