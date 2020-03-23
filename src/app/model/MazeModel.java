package app.model;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.HashSet;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;

import app.controller.MazeController;
import app.controller.components.AbstractCell.CellState;
import app.model.components.CellPanel;

/**
 * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
 * pivotal <code>app.model.MazeModel</code> component, extending
 * <code>javax.swing.JPanel</code> and storing
 * <code>app.model.components.CellPanel</code>.
 *
 * @author JoshGoA
 * @version 0.1
 * @see javax.swing.JPanel JPanel
 * @see app.model.components.CellPanel CellPanel
 */
public class MazeModel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Two-sided <code>app.controller.MazeController</code>
     * <code>app.view.MazeView</code> interaction pipeline.
     *
     * @see app.controller.MazeController MazeController
     */
    private MazeController controller;

    /**
     * Bi-dimensional <code>app.model.components.CellPanel</code> array.
     */
    private CellPanel[][] grid;

    /**
     * Grid starting <code>app.model.components.CellPanel</code> pointer.
     */
    private CellPanel start = null;

    /**
     * Grid ending <code>app.model.components.CellPanel</code> pointer.
     */
    private CellPanel end = null;

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

    {
        this.setBorder(new EtchedBorder());
        this.setBackground(Color.WHITE);
    }

    /**
     * Create a new isolated pipeline component.
     */
    public MazeModel() { }

    /**
     * Create a new two-sided <code>app.controller.MazeController</code> interaction
     * <code>app.model.MazeModel</code> pipeline component.
     *
     * @param controller MazeController
     */
    public MazeModel(final MazeController controller) {
        this.setController(controller);
    }

    /**
     * Reset grid with identical row and column values.
     *
     * @throws ClassCastException Layout might not have been initialized
     */
    public final void reset() throws ClassCastException {
        this.pathfinder.setIsRunning(false);
        this.setGrid(((GridLayout) this.getLayout()).getRows(), ((GridLayout) this.getLayout()).getColumns());
    }

    /**
     * Request <code>app.view.MazeView.releaseCellPopup(CellPanel cell)</code>
     * event.
     *
     * @param cell CellPanel
     * @return JPopupMenu
     */
    public final JPopupMenu requestCellPopup(final CellPanel cell) {
        return this.controller.fireCellPopup(cell);
    }

    /**
     * Fire <code>app.model.components.CellPanel.clear()</code> event.
     */
    public final void fireClear() {
        if (this.pathfinder.getIsRunning() || this.start == null)
            return;
        this.start.clear();
    }

    /**
     * Return current <code>app.controller.MazeController</code> instance.
     *
     * @return MazeController
     */
    public final MazeController getController() {
        return this.controller;
    }

    /**
     * Set current <code>app.controller.MazeController</code> instance.
     *
     * @param controller MazeController
     */
    public final void setController(final MazeController controller) {
        this.controller = Objects.requireNonNull(controller, "'controller' must not be null");
    }

    /**
     * Return current grid <code>app.model.components.CellPanel</code> structure.
     *
     * @return CellPanel[][]
     */
    public final CellPanel[][] getGrid() {
        return this.grid;
    }

    /**
     * Set current grid <code>app.model.components.CellPanel</code> row and column
     * structure.
     *
     * @param rows int
     * @param cols int
     * @throws NegativeArraySizeException if (rows < 0 || cols < 0)
     */
    public final void setGrid(final int rows, final int cols) throws NegativeArraySizeException {
        // Remove previous components
        this.removeAll();
        // Update layout and grid
        this.setLayout(new GridLayout(rows, cols));
        this.grid = new CellPanel[rows][cols];
        // Initialize CellPanel with only seed
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.grid[row][col] = new CellPanel(this, new Point(row, col));
                this.add(this.grid[row][col]);
            }
        }
        // Set CellPanel neighbors
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // TODO: Add diagonals by calling controller
                final int CellPanelRow = row;
                final int CellPanelCol = col;
                this.grid[row][col].setNeighbors(new HashSet<CellPanel>() {
                    private static final long serialVersionUID = 1L;
                    {
                        if (CellPanelRow - 1 >= 0)
                            this.add(grid[CellPanelRow - 1][CellPanelCol]);
                        if (CellPanelCol + 1 < cols)
                            this.add(grid[CellPanelRow][CellPanelCol + 1]);
                        if (CellPanelRow + 1 < rows)
                            this.add(grid[CellPanelRow + 1][CellPanelCol]);
                        if (CellPanelCol - 1 >= 0)
                            this.add(grid[CellPanelRow][CellPanelCol - 1]);
                    }
                });
            }
        }
        // Update draw changes
        this.revalidate();
        this.repaint();
    }

    /**
     * Return current grid starting <code>app.model.components.CellPanel</code>
     * pointer.
     *
     * @return CellPanel
     */
    public final CellPanel getStart() {
        return this.start;
    }

    /**
     * Set current grid starting <code>app.model.components.CellPanel</code>
     * pointer.
     *
     * @param start CellPanel
     */
    public final void setStart(final CellPanel start) {
        if (start == null) {
            this.start = null;
        } else {
            // Override start
            if (this.start != null && !start.equals(this.start)) {
                start.setState(CellState.START);
                this.start.setState(CellState.EMPTY);
                this.start = start;
            } else {
                // Delete start
                if (start.getState() == CellState.START) {
                    start.setState(CellState.EMPTY);
                    this.start = null;
                    // Set new start
                } else {
                    start.setState(CellState.START);
                    this.start = start;
                }
            }
        }
    }

    /**
     * Return current grid ending <code>app.model.components.CellPanel</code>
     * pointer.
     *
     * @return CellPanel
     */
    public final CellPanel getEnd() {
        return this.end;
    }

    /**
     * Set current grid ending <code>app.model.components.CellPanel</code> pointer.
     *
     * @param end CellPanel
     */
    public final void setEnd(final CellPanel end) {
        if (end == null) {
            this.end = null;
        } else {
            // Override start
            if (this.end != null && !end.equals(this.end)) {
                end.setState(CellState.END);
                this.end.setState(CellState.EMPTY);
                this.end = end;
            } else {
                // Delete start
                if (end.getState() == CellState.END) {
                    end.setState(CellState.EMPTY);
                    this.end = null;
                    // Set new start
                } else {
                    end.setState(CellState.END);
                    this.end = end;
                }
            }
        }
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
     * Fire <code>app.model.PathFinder.awake(CellPanel[][] grid)</code> event.
     */
    public final void awakePathFinder() {
        this.pathfinder.awake(this.getGrid());
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
     * Fire <code>app.model.Generator.awake(CellPanel[][] grid)</code> event.
     */
    public final void awakeGenerator() {
        this.generator.awake(this.getGrid());
    }

}
