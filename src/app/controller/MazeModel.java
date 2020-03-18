package app.controller;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class MazeModel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final MazeController controller;

    private Cell[][] grid;

    private Cell start = null;
    private Cell end = null;

    {
        this.setBorder(new EtchedBorder());
        this.setBackground(Color.WHITE);
    }

    public MazeModel(final MazeController controller, final int rows, final int cols) {
        this.controller = controller;
        this.setGrid(rows, cols);
    }

    public final MazeController getController() {
        return this.controller;
    }

    public final Cell[][] getGrid() {
        return this.grid;
    }

    public final void setGrid(final int rows, final int cols) {
        this.removeAll();
        this.setLayout(new GridLayout(rows, cols));
        this.grid = new Cell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final Cell cell = new Cell(new Point(row, col));
                this.grid[row][col] = cell;
                this.add(cell);
            }
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final int cellRow = row;
                final int cellCol = col;
                this.grid[row][col].setNeighbors(new HashMap<Cell, Integer>() {
                    private static final long serialVersionUID = 1L;
                    {
                        if (cellRow - 1 >= 0)
                            this.put(grid[cellRow - 1][cellCol], new Integer(0));
                        if (cellCol + 1 < cols)
                            this.put(grid[cellRow][cellCol + 1], new Integer(0));
                        if (cellRow + 1 < rows)
                            this.put(grid[cellRow + 1][cellCol], new Integer(0));
                        if (cellCol - 1 >= 0)
                            this.put(grid[cellRow][cellCol - 1], new Integer(0));
                    }
                });
            }
        }
        this.revalidate();
        this.repaint();
    }

    public final Cell getStart() {
        return this.start;
    }

    public final void setStart(final Cell start) {
        this.start = start;
    }

    public final Cell getEnd() {
        return this.end;
    }

    public final void setEnd(final Cell end) {
        this.end = end;
    }

}
