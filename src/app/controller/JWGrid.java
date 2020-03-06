package app.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.JPanel;

/**
 * A <code>app.view.components.JWPanel</code> <code>java.awt.GridLayout</code>
 * <code>app.controller.Cell</code> controller.
 *
 * @see app.view.components.JWPanel JWPanel
 */
public final class JWGrid extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * <code>app.controller.JWGrid</code> <code>app.controller.Cell</code> storage.
     */
    private Map<Point, Cell> grid;

    /**
     * <code>app.controller.Cell</code> starting pointer.
     */
    private Cell start = null;

    /**
     * <code>app.controller.Cell</code> endpoint pointer.
     */
    private Cell end = null;

    /**
     * Create <code>java.awt.GridLayout</code>
     * <code>app.view.components.JWPanel</code> of given shape filled with
     * <code>app.controller.Cell</code>.
     *
     * @param rows          int
     * @param cols          int
     * @param preferredSize Dimension
     */
    public JWGrid(final int rows, final int cols, final Dimension preferredSize) {
        this.setGrid(rows, cols);
        this.setPreferredSize(preferredSize);
    }

    public final Map<Point, Cell> getGrid() {
        return this.grid;
    }

    public final void setGrid(final int rows, final int cols) {
        // Remove all Cell from JWGrid
        this.removeAll();
        // Reset layout with new rows and columns
        this.setLayout(new GridLayout(rows, cols, 0, 0));
        // Override JWGrid LinkedHashMap to preserve order
        this.grid = new LinkedHashMap<Point, Cell>(rows * cols) {
            private static final long serialVersionUID = 1L;
            {
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        final Cell cell = new Cell();
                        this.put(new Point(row, col), cell);
                        JWGrid.this.add(cell);
                    }
                }
            }
        };
        this.revalidate();
        this.repaint();
    }

    public final Cell getStart() {
        return this.start;
    }

    public final void setStart(final Cell start) {
        this.start = Objects.requireNonNull(start, "'start' must not be null");
    }

    public final Cell getEnd() {
        return this.end;
    }

    public final void setEnd(final Cell end) {
        this.end = Objects.requireNonNull(end, "'end' must not be null");
    }

}
