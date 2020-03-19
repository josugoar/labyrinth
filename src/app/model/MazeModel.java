package app.model;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import app.controller.MazeController;
import app.model.components.Cell;

/**
 * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
 * pivotal <code>app.model.MazeModel</code> component, extending
 * <code>javax.swing.JPanel</code>.
 *
 * @author JoshGoA
 * @version 0.1
 * @see javax.swing.JPanel JPanel
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
     *
     * @see app.model.components.Cell Cell
     */
    private Cell[][] grid;

    /**
     * Grid starting <code>app.model.components.Cell</code> pointer.
     *
     * @see app.model.components.Cell Cell
     */
    private Cell start = null;

    /**
     * Grid ending <code>app.model.components.Cell</code> pointer.
     *
     * @see app.model.components.Cell Cell
     */
    private Cell end = null;

    {
        this.setBorder(new EtchedBorder());
        this.setBackground(Color.WHITE);
    }

    /**
     * Create a new two-sided <code>app.controller.MazeController</code> interaction
     * <code>app.model.MazeModel</code> component.
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
     * structure
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
        this.start = start;
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
        this.end = end;
    }

}
