package app.controller;

import java.awt.Cursor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.Timer;

import algo.grd.GridAlgorithm;
import algo.grd.dsa.AbstractCell.CellState;
import algo.grd.gt.Generator;
import algo.grd.gt.PathFinder;
import app.model.MazePanel;
import app.model.components.CellPanel;
import app.view.MazeFrame;
import utils.JWrapper;

/**
 * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
 * pivotal <code>app.controller.MazeDelegator</code> component, handling
 * multiple pivotal interactions, implementing
 * <code>java.io.Serializable</code>.
 *
 * @author JoshGoA
 * @version 0.1
 * @see java.io.Serializable Serializable
 */
public class MazeDelegator implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Two-sided <code>app.model.MazePanel</code>
     * <code>app.controller.MazeDelegator</code> interaction pipeline.
     *
     * @see app.model.MazePanel MazePanel
     */
    private MazePanel panel;

    /**
     * Two-sided <code>app.view.MazeFrame</code>
     * <code>app.controller.MazeDelegator</code> interaction pipeline.
     *
     * @see app.view.MazeFrame MazeFrame
     */
    private MazeFrame frame;

    /**
     * Diagonal tile trasversal flag.
     */
    private boolean diagonals = true;

    /**
     * Arrow draw flag.
     */
    private boolean arrows = false;

    /**
     * Current user input mode selection.
     *
     * @see app.model.components.CellPanel.CellState CellState
     * @deprecated Draw cycle made by mouse input
     */
    private CellState mode = CellState.OBSTACLE;

    /**
     * Create a new two-sided <code>app.model.MazePanel</code> and
     * <code>app.view.MazeFrame</code> interaction
     * <code>app.controller.MazeDelegator</code> component.
     *
     * @param panel MazePanel
     * @param frame MazeFrame
     */
    public MazeDelegator(final MazePanel panel, final MazeFrame frame) {
        this.setPanel(panel);
        this.setFrame(frame);
    }

    /**
     * Create a new isolated pipeline component.
     */
    public MazeDelegator() {
        this(null, null);
    }

    /**
     * Read <code>app.model.MazePanel</code> from stream.
     */
    public final void readMaze() {
        try {
            final FileInputStream file = new FileInputStream(MazePanel.class.getResource("ser/maze.ser").getPath());
            final ObjectInputStream in = new ObjectInputStream(file);
            final MazePanel other = (MazePanel) in.readObject();
            in.close();
            file.close();
            this.panel.override(other);
        } catch (final IOException | ClassNotFoundException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Store <code>app.model.MazePanel</code> in stream.
     */
    public final void writeMaze() {
        try {
            this.panel.assertRunning();
            final FileOutputStream file = new FileOutputStream(MazePanel.class.getResource("ser/maze.ser").getPath());
            final ObjectOutputStream out = new ObjectOutputStream(file);
            CellPanel.setSelected(null);
            this.panel.clear();
            out.writeObject(this.panel);
            out.close();
            file.close();
        } catch (final InterruptedException | IOException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Return current <code>app.model.MazePanel</code> instance.
     *
     * @return MazePanel
     */
    public final MazePanel getPanel() {
        return this.panel;
    }

    /**
     * Set current <code>app.model.MazePanel</code> instance.
     *
     * @param panel MazePanel
     */
    public final void setPanel(final MazePanel panel) {
        this.panel = panel;
    }

    /**
     * Fire <code>app.model.MazePanel.reset()</code> event.
     */
    public final void reset() {
        this.panel.reset();
    }

    /**
     * Request <code>app.model.MazePanel.clear()</code> event.
     */
    public final void clear() {
        try {
            if (this.panel.getStart() == null || this.panel.getStart().getInner() == null)
                throw new NullPointerException("No nodes to clear...");
            this.panel.clear();
        } catch (final NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Return current dimension.
     *
     * @return int
     */
    public final int getDimension() {
        return this.panel.getDimension();
    }

    /**
     * Set current dimension and fire
     * <code>app.model.MazePanel.setGrid(int rows, int cols)</code> event.
     *
     * @param dimension int
     */
    public final void setDimension(final int dimension) {
        this.panel.setDimension(dimension);
    }

    /**
     * Request current <code>algo.grd.gt.PathFinder</code> instance.
     *
     * @return PathFinder
     */
    public final PathFinder<CellPanel> getPathFinder() {
        return this.panel.getPathFinder();
    }

    /**
     * Update current <code>algo.grd.gt.PathFinder</code> instance.
     */
    public final void setPathFinder(final PathFinder<CellPanel> pathfinder) {
        this.panel.setPathFinder(pathfinder);
    }

    /**
     * Run current <code>algo.grd.gt.PathFinder</code> instance.
     */
    public final void awakePathFinder() {
        this.frame.requestFocusInWindow();
        try {
            this.panel.awakePathFinder();
        } catch (InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Request current <code>app.model.Generator</code> instance.
     *
     * @return Generator
     */
    public final Generator<CellPanel> getGenerator() {
        return this.panel.getGenerator();
    }

    /**
     * Update current <code>app.model.Generator</code> instance.
     */
    public final void setGenerator(final Generator<CellPanel> generator) {
        this.panel.setGenerator(generator);
    }

    /**
     * Run current <code>app.model.Generator</code> instance.
     */
    public final void awakeGenerator() {
        this.frame.requestFocusInWindow();
        try {
            this.panel.awakeGenerator();
        } catch (InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Return current algorithms delay.
     *
     * @return Integer
     */
    public final <T extends GridAlgorithm<?>> Integer getDelay(Class<T> algorithm) {
        try {
            if (algorithm.equals(PathFinder.class))
                return this.panel.getPathFinder().getDelay();
            else if (algorithm.equals(Generator.class))
                return this.panel.getGenerator().getDelay();
            else
                throw new UnsupportedClassVersionError("Algorithm not supported...");
        } catch (final UnsupportedClassVersionError e) {
            JWrapper.dispatchException(e);
            return null;
        }
    }

    /**
     * Set current algorithms delay.
     *
     * @param delay int
     */
    public final void setDelay(final int delay) {
        this.panel.getPathFinder().setDelay(delay);
        this.panel.getGenerator().setDelay(delay);
    }

    /**
     * Return current density.
     *
     * @return int
     */
    public final int getDensity() {
        return this.panel.getGenerator().getDensity();
    }

    /**
     * Set current density.
     *
     * @param density int
     */
    public final void setDensity(final int density) {
        this.panel.getGenerator().setDensity(density);
    }

    /**
     * Return current <code>app.view.MazeFrame</code> instance.
     *
     * @return MazeFrame
     */
    public final MazeFrame getFrame() {
        return this.frame;
    }

    /**
     * Set current <code>app.view.MazeFrame</code> instance.
     *
     * @param frame MazeFrame
     */
    public final void setFrame(final MazeFrame frame) {
        this.frame = frame;
    }

    /**
     * Fire <code>app.view.MazeFrame.releaseCellPopup(CellPanel cell)</code> event.
     *
     * @param cell CellPanel
     * @return JPopupMenu
     */
    public final JPopupMenu releaseCellPopup(final CellPanel cell) {
        try {
            return this.frame.releaseCellPopup(cell);
        } catch (final InvalidParameterException e) {
            JWrapper.dispatchException(e);
            return null;
        }
    }

    /**
     * Dispatch <code>java.awt.Cursor</code> <code>java.awt.event.KeyEvent</code>.
     */
    public final void dispatchShift() {
        try {
            this.panel.assertRunning();
            this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Dispatch <code>algo.AbstractAlgorithm.isWaiting()</code> state.
     */
    public final void dispatchSpace() {
        if (this.panel.getPathFinder().isRunning())
            this.panel.getPathFinder().setWaiting(!this.panel.getPathFinder().isWaiting());
        else if (this.panel.getGenerator().isRunning())
            this.panel.getGenerator().setWaiting(!this.panel.getGenerator().isWaiting());
    }

    /**
     * Request current <code>javax.swing.JTree</code> instance.
     *
     * @return JTree
     */
    public final JTree getTreeComponent() {
        return this.frame.getTreeComponent();
    }

    /**
     * Update current <code>javax.swing.JTree</code> instance.
     */
    public final void setTreeComponent(final JTree treeComponent) {
        this.frame.setTreeComponent(treeComponent);
    }

    /**
     * Request current <code>javax.swing.JLabel</code> instance.
     *
     * @return JLabel
     */
    public final JLabel getStatusComponent() {
        return this.frame.getStatusComponent();
    }

    /**
     * Upadte current <code>javax.swing.JLabel</code> instance.
     */
    public final void setStatusComponent(final JLabel statusComponent) {
        this.frame.setStatusComponent(statusComponent);
    }

    /**
     * Cycle between <code>javax.swing.JLabel</code>
     * <code>java.awt.Component.isVisible()</code> states.
     */
    public final void cycleStatusComponent() {
        this.frame.getStatusComponent().setVisible(!this.frame.getStatusComponent().isVisible());
    }

    // TODO: statusComponent
    /**
     * Reset current <code>javax.swing.JLabel</code> instance custom application
     * output message.
     */
    public final void resetStatusComponent() {
        new Timer(2500, e -> {
            // Set text to default state
            this.frame.getStatusComponent().setText(this.toString());
            ((Timer) e.getSource()).stop();
        }).start();
    }

    /**
     * Request current <code>javax.swing.JSplitPane</code> instance.
     *
     * @return JSplitPane
     */
    public final JSplitPane getSplitComponent() {
        return this.frame.getSplitComponent();
    }

    /**
     * Update current <code>javax.swing.JSplitPane</code> instance.
     */
    public final void setSplitComponent(final JSplitPane splitComponent) {
        this.frame.setSplitComponent(splitComponent);
    }

    /**
     * Cycle between <code>javax.swing.JSplitPane</code>
     * <code>java.awt.Component.isVisible()</code> and
     * <code>java.awt.Component.isEnabled()</code> states and update
     * <code>javax.swing.JSplitPane.setDividerLocation(int location)</code>.
     */
    public final void cycleSplitComponent() {
        this.frame.getSplitComponent().setDividerLocation(-1);
        this.frame.getSplitComponent().setEnabled(!this.frame.getSplitComponent().isEnabled());
        this.frame.getSplitComponent().getLeftComponent().setVisible(!this.frame.getSplitComponent().getLeftComponent().isVisible());
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
     * Return current <code>app.model.components.CellPanel</code>
     * <code>app.model.components.CellPanel.CellState</code>.
     *
     * @return CellPanel.CellState
     * @deprecated Draw cycle made by mouse input
     */
    @Deprecated
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
    @Deprecated
    public final void setMode(final CellState mode) {
        this.mode = mode;
    }

    @Override
    public final String toString() {
        return String.format("Maze");
    }

}
