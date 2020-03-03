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

/**
 * A <code>src.view.components.JWPanel</code> <code>java.awt.GridLayout</code>
 * <code>src.controller.JWGrid.Cell</code> controller.
 *
 * @author JoshGoA
 * @see src.view.components.JWPanel JWPanel
 */
public class JWGrid extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * <code>src.controller.JWGrid</code> <code>src.controller.JWGrid.Cell</code>
     * storage.
     */
    private Map<Point, Cell> grid;

    /**
     * <code>src.controller.JWGrid.Cell</code> endpoint pointer.
     */
    private Cell start, end = null;

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
                @Override
                public void mousePressed(final MouseEvent e) {
                    switch (((MazeApp) SwingUtilities.getWindowAncestor(Cell.this)).getMode()) {
                        case START:
                            // Override previous starting Cell
                            if (((JWGrid) Cell.this.getParent()).getStart() != null) {
                                ((JWGrid) Cell.this.getParent()).getStart().setState(State.EMPTY);
                            }
                            ((JWGrid) Cell.this.getParent()).setStart(Cell.this);
                            Cell.this.setState(State.START);
                            break;
                        case END:
                            // Override previous endpoint Cell
                            if (((JWGrid) Cell.this.getParent()).getEnd() != null) {
                                ((JWGrid) Cell.this.getParent()).getEnd().setState(State.EMPTY);
                            }
                            ((JWGrid) Cell.this.getParent()).setEnd(Cell.this);
                            Cell.this.setState(State.END);
                            break;
                        case OBSTACLE:
                            Cell.this.setState(State.OBSTACLE);
                            break;
                        default:
                            Cell.this.setState(State.EMPTY);
                    }
                }
                @Override
                public void mouseEntered(final MouseEvent e) {
                    // Check MouseDown
                    if ((e.getModifiersEx() & (MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK
                            | MouseEvent.BUTTON3_DOWN_MASK)) != 0) {
                        switch (((MazeApp) SwingUtilities.getWindowAncestor(Cell.this)).getMode()) {
                            case OBSTACLE:
                                Cell.this.setState(State.OBSTACLE);
                                break;
                            case EMPTY:
                                Cell.this.setState(State.EMPTY);
                                break;
                            default:
                                break;
                        }
                    }
                }
            });
        }

        @Override
        public final void paintComponent(final Graphics g) {
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
