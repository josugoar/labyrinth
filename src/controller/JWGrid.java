package src.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import src.MazeApp;
import src.view.components.JWPanel;

/**
 * A <code>src.view.components.JWPanel</code> <code>java.awt.GridLayout</code>
 * <code>src.controller.JWGrid.Cell</code> controller.
 *
 * @author JoshGoA
 * @see src.view.components.JWPanel JWPanel
 */
public class JWGrid extends JWPanel {

    private static final long serialVersionUID = 1L;

    /**
     * <code>src.controller.JWGrid</code> <code>src.controller.JWGrid.Cell</code>
     * storage.
     */
    private Map<Point, Cell> grid;

    /**
     * Create <code>java.awt.GridLayout</code>
     * <code>src.view.components.JWPanel</code> of given shape filled with
     * <code>src.controller.JWGrid.Cell</code>.
     *
     * @param rows          int
     * @param cols          int
     * @param preferredSize Dimension
     */
    public JWGrid(final int rows, final int cols, final Dimension preferredSize) {
        super(new GridLayout(rows, cols, 0, 0), preferredSize);
        this.setGrid(rows, cols);
    }

    public final Map<Point, Cell> getGrid() {
        return this.grid;
    }

    public final void setGrid(final int rows, final int cols) {
        this.removeAll();
        this.grid = new LinkedHashMap<Point, Cell>(rows * cols) {
            private static final long serialVersionUID = 1L;
            {
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        this.put(new Point(row, col), new Cell());
                    }
                }
            }
        };
        this.addJW(this.getGrid().values());
        this.revalidate();
    }

    /**
     * Internal <code>src.controller.JWGrid</code>
     * <code>src.view.components.JWPanel</code>.
     *
     * @see javax.swing.JPanel JPanel
     */
    public static final class Cell extends JPanel {

        /**
         * Enum of <code>src.controller.JWGrid.Cell</code> states: START, END, OBSTACLE,
         * EMPTY, CURR_NODE, NEXT_NODE, PATH_NODE.
         */
        public static enum State {
            START, END, OBSTACLE, EMPTY, CURR_NODE, NEXT_NODE, PATH_NODE
        }

        private static final long serialVersionUID = 1L;

        /**
         * <code>src.controller.JWGrid.Cell</code>
         * <code>src.controller.JWGrid.Cell.State</code> selection.
         */
        private State state = State.EMPTY;

        {
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(final MouseEvent e) {
                    switch (((MazeApp) SwingUtilities.getWindowAncestor(Cell.this)).getMode()) {
                        case START:
                            Cell.this.setState(State.START);
                            break;
                        case END:
                            Cell.this.setState(State.END);
                            break;
                        case OBSTACLE:
                            Cell.this.setState(State.OBSTACLE);
                            break;
                        default:
                            Cell.this.setState(State.EMPTY);
                    }
                }
            });
        }

        @Override
        protected final void paintComponent(final Graphics g) {
            Color color;
            switch (this.state) {
                case START:
                    color = Color.RED;
                    break;
                case END:
                    color = Color.GREEN;
                    break;
                case OBSTACLE:
                    color = Color.BLACK;
                    break;
                case CURR_NODE:
                    color = Color.CYAN;
                    break;
                case NEXT_NODE:
                    color = Color.BLUE;
                    break;
                case PATH_NODE:
                    color = Color.YELLOW;
                    break;
                default:
                    color = Color.WHITE;
            }
            g.setColor(color);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        @Override
        public final String toString() {
            return String.format("Cell(state: %s)", this.state);
        }

        public final State getState() {
            return this.state;
        }

        public final void setState(final State state) {
            this.state = state;
            this.repaint();
        }

    }

}
