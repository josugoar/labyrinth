package src.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;

/**
 * A <code>src.view.components.JWPanel</code> <code>java.awt.GridLayout</code>
 * <code>src.controller.Cell</code> controller.
 *
 * @see src.view.components.JWPanel JWPanel
 */
public final class JWGrid extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * <code>src.controller.JWGrid</code> <code>src.controller.Cell</code> storage.
     */
    private Map<Point, Cell> grid;

    /**
     * <code>src.controller.Cell</code> endpoint pointer.
     */
    private Cell start, end = null;

    /**
     * Create <code>java.awt.GridLayout</code>
     * <code>src.view.components.JWPanel</code> of given shape filled with
     * <code>src.controller.Cell</code>.
     *
     * @param rows          int
     * @param cols          int
     * @param preferredSize Dimension
     */
    public JWGrid(final int rows, final int cols, final Dimension preferredSize) {
        this.setPreferredSize(preferredSize);
        this.setGrid(rows, cols);
    }

    public final Map<Point, Cell> getGrid() {
        return this.grid;
    }

    public final void setGrid(final int rows, final int cols) {
        this.removeAll();
        this.setLayout(new GridLayout(rows, cols, 0, 0));
        // Override grid LinkedHashMap to preserve order
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
        this.start = start;
    }

    public final Cell getEnd() {
        return this.end;
    }

    public final void setEnd(final Cell end) {
        this.end = end;
    }

}
