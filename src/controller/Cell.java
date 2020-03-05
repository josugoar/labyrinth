package src.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import src.MazeApp;

/**
 * Internal <code>src.controller.JWGrid</code>
 * <code>src.view.components.JPanel</code>.
 *
 * @see javax.swing.JPanel JPanel
 */
public final class Cell extends JPanel {

    /**
     * Enum of <code>src.controller.Cell</code> states: <code>START</code>,
     * <code>END</code>, <code>OBSTACLE</code>, <code>EMPTY</code>,
     * <code>VISITED</code>, <code>GERMINATED</code>, <code>PATH</code>.
     */
    public static enum State {
        START, END, OBSTACLE, EMPTY, VISITED, GERMINATED, PATH
    }

    private static final long serialVersionUID = 1L;

    /**
     * <code>src.controller.Cell</code> <code>src.controller.Cell.State</code>
     * selection.
     */
    private State state = State.EMPTY;

    {
        // Initialize Cell with border and CellListener
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.addMouseListener(new CellListener());
    }

    @Override
    public final void paintComponent(final Graphics g) {
        Color color;
        // Paint Cell depending on state
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
            case VISITED:
                color = Color.CYAN;
                break;
            case GERMINATED:
                color = Color.BLUE;
                break;
            case PATH:
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
        // Refresh Cell Color on state change
        this.state = state;
        this.repaint();
    }

    /**
     * An extended <code>java.awt.event.MouseAdapter</code> implementation for
     * <code>src.controller.Cell</code>
     */
    private final class CellListener extends MouseAdapter {

        @Override
        public void mousePressed(final MouseEvent e) {
            // Change Cell depending on MazeApp mode
            final JWGrid panel = (JWGrid) Cell.this.getParent();
            switch (((MazeApp) SwingUtilities.getWindowAncestor(Cell.this)).getMode()) {
                case START:
                    // Override previous starting Cell
                    if (panel.getStart() != null)
                        panel.getStart().setState(State.EMPTY);
                    panel.setStart(Cell.this);
                    Cell.this.setState(State.START);
                    break;
                case END:
                    // Override previous endpoint Cell
                    if (panel.getEnd() != null)
                        panel.getEnd().setState(State.EMPTY);
                    panel.setEnd(Cell.this);
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
            // Check mousedown
            if ((e.getModifiersEx() & (MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK
                    | MouseEvent.BUTTON3_DOWN_MASK)) != 0) {
                // Drag mouse to quickly change desired Cell state
                switch (((MazeApp) SwingUtilities.getWindowAncestor(Cell.this)).getMode()) {
                    case OBSTACLE:
                        Cell.this.setState(State.OBSTACLE);
                        break;
                    case EMPTY:
                        Cell.this.setState(State.EMPTY);
                        break;
                    default:
                }
            }
        }

    }

}
