package src.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import src.MazeApp;
import src.view.components.JWPanel;

/**
 * GridLayout controller
 *
 * @author JoshGoA
 */
public class JWGrid extends JWPanel {

    private static final long serialVersionUID = 1L;

    private final Map<Point, Component> grid;

    /**
     * Create GridLayout JPanel of given shape
     *
     * @param rows          int
     * @param cols          int
     * @param preferredSize Dimension
     */
    public JWGrid(final int rows, final int cols, final Dimension preferredSize) {
        super(new GridLayout(rows, cols, 0, 0), preferredSize);
        this.grid = new HashMap<Point, Component>(rows * cols) {
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
    }

    public final Map<Point, Component> getGrid() {
        return this.grid;
    }

    public static final class Cell extends JWPanel {

        private static enum State {
            START, END, OBSTACLE, EMPTY, CURR_NODE, NEXT_NODE
        }

        private static final long serialVersionUID = 1L;

        private State state = State.EMPTY;

        public Cell() {
            super(new FlowLayout(), null, null);
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
                        case EMPTY:
                            Cell.this.setState(State.EMPTY);
                            break;
                        default:
                            Cell.this.setState(State.OBSTACLE);
                    }
                }
            });
        }

        @Override
        protected final void paintComponent(final Graphics g) {
            Color color;
            switch (this.getState()) {
                case START:
                    color = Color.GREEN;
                    break;
                case END:
                    color = Color.RED;
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
                default:
                    color = Color.WHITE;
            }
            g.setColor(color);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        @Override
        public final String toString() {
            return String.format("%s (state: %s)", this.getClass(), this.getState());
        }

        public final State getState() {
            return this.state;
        }

        private final void setState(final State state) {
            this.state = Objects.requireNonNull(state, "'state' must not be null");
            this.repaint();
        }

    }

}
