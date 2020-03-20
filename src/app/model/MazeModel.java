package app.model;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.HashSet;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import app.controller.MazeController;
import app.controller.components.AbstractCell.CellState;
import app.model.components.Cell;

/**
 * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
 * pivotal <code>app.model.MazeModel</code> component, extending
 * <code>javax.swing.JPanel</code> and storing
 * <code>app.model.components.Cell</code>.
 *
 * @author JoshGoA
 * @version 0.1
 * @see javax.swing.JPanel JPanel
 * @see app.model.components.Cell Cell
 */
public class MazeModel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Two-sided <code>app.controller.MazeController</code>
     * <code>app.view.MazeView</code> interaction pipeline.
     *
     * @see app.controller.MazeController MazeController
     */
    private final MazeController controller;

    /**
     * Bi-dimensional <code>app.model.components.Cell</code> array.
     */
    private Cell[][] grid;

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
     * Grid starting <code>app.model.components.Cell</code> pointer.
     */
    private Cell start = null;

    /**
     * Grid ending <code>app.model.components.Cell</code> pointer.
     */
    private Cell end = null;

    {
        this.setBorder(new EtchedBorder());
        this.setBackground(Color.WHITE);
    }

    /**
     * Create a new two-sided <code>app.controller.MazeController</code> interaction
     * <code>app.model.MazeModel</code> pipeline component.
     *
     * @param controller MazeController
     * @param rows       int
     * @param cols       int
     */
    public MazeModel(final MazeController controller, final int rows, final int cols) {
        this.controller = controller;
        this.setGrid(rows, cols);
    }

    /**
     * Recursively traverse entire <code>app.model.components.Cell</code> and
     * <code>app.model.components.Node</code> tree structure.
     *
     * @param parent Cell
     */
    public static final void clear(final Cell parent) {
        if (parent.getInner() != null) {
            parent.setInner(null);
            for (Cell child : parent.getNeighbors()) {
                MazeModel.clear(child);
            }
        }
    }

    /**
     * Reset grid with identical row and column values.
     */
    public final void reset() {
        this.setGrid(((GridLayout) this.getLayout()).getRows(), ((GridLayout) this.getLayout()).getColumns());
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
     * Return current grid <code>app.model.components.Cell</code> structure.
     *
     * @return Cell[][]
     */
    public final Cell[][] getGrid() {
        return this.grid;
    }

    /**
     * Set current grid <code>app.model.components.Cell</code> row and column
     * structure.
     *
     * @param rows int
     * @param cols int
     */
    public final void setGrid(final int rows, final int cols) {
        // Remove previous components
        this.removeAll();
        // Update layout and grid
        this.setLayout(new GridLayout(rows, cols));
        this.grid = new Cell[rows][cols];
        // Initialize Cell with only seed
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.grid[row][col] = new Cell(new Point(row, col));
                this.add(this.grid[row][col]);
            }
        }
        // Set Cell neighbors
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final int cellRow = row;
                final int cellCol = col;
                this.grid[row][col].setNeighbors(new HashSet<Cell>() {
                    private static final long serialVersionUID = 1L;
                    {
                        if (cellRow - 1 >= 0)
                            this.add(grid[cellRow - 1][cellCol]);
                        if (cellCol + 1 < cols)
                            this.add(grid[cellRow][cellCol + 1]);
                        if (cellRow + 1 < rows)
                            this.add(grid[cellRow + 1][cellCol]);
                        if (cellCol - 1 >= 0)
                            this.add(grid[cellRow][cellCol - 1]);
                    }
                });
            }
        }
        // Update draw changes
        this.revalidate();
        this.repaint();
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
     * Fire <code>app.model.Generator.awake(Cell[][] grid)</code> event.
     */
    public final void awakeGenerator() {
        this.generator.awake(this.getGrid());
    }

    /**
     * Return current grid starting <code>app.model.components.Cell</code> pointer.
     *
     * @return Cell
     */
    public final Cell getStart() {
        return this.start;
    }

    /**
     * Set current grid starting <code>app.model.components.Cell</code> pointer.
     *
     * @param start Cell
     */
    public final void setStart(final Cell start) {
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
     * Return current grid ending <code>app.model.components.Cell</code> pointer.
     *
     * @return Cell
     */
    public final Cell getEnd() {
        return this.end;
    }

    /**
     * Set current grid ending <code>app.model.components.Cell</code> pointer.
     *
     * @param end Cell
     */
    public final void setEnd(final Cell end) {
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

}
