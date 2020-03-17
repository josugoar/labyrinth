package app.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import app.model.Node;
import app.view.MazeView;

public class Cell extends JPanel {

    public static enum State {
        START, END, OBSTACLE, EMPTY;

        public static final Map<State, Color> COLOR = new EnumMap<State, Color>(State.class) {
            private static final long serialVersionUID = 1L;
            {
                this.put(State.START, Color.RED);
                this.put(State.END, Color.GREEN);
                this.put(State.OBSTACLE, Color.BLACK);
                this.put(State.EMPTY, Color.WHITE);
            }
        };
    }

    private static final long serialVersionUID = 1L;

    private State state = State.EMPTY;

    private Node inner = null;

    private Map<Cell, Integer> neighbors;
    private Point seed;

    {
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.addMouseListener(new CellListener());
    }

    public Cell(final Point seed, final Map<Cell, Integer> neighbors) {
        this.seed = seed;
        this.neighbors = neighbors;
    }

    public Cell(final Point seed) {
        this(seed, null);
    }

    @Override
    public final void paintComponent(final Graphics g) {
        if (this.inner == null) {
            g.setColor(Cell.State.COLOR.get(this.state));
        } else {
            g.setColor(Node.State.COLOR.get(this.inner.getState()));
        }
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public final String toString() {
        return String.format("Cell(state: %s)", this.state);
    }

    public void setNeighbors(final Map<Cell, Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public final State getState() {
        return this.state;
    }

    public final void setState(final State state) {
        this.state = Objects.requireNonNull(state, "'state' must not be null");
        this.repaint();
    }

    public final Node getInner() {
        return this.inner;
    }

    public final void setInner(final Node inner) {
        this.inner = inner;
    }

    public final Map<Cell, Integer> getNeighbors() {
        return this.neighbors;
    }

    public final Point getSeed() {
        return this.seed;
    }

    private final class CellListener extends MouseAdapter {

        @Override
        public final void mousePressed(final MouseEvent e) throws NullPointerException {
            if (e.isShiftDown() && ((e.getModifiersEx() & (MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK
                    | MouseEvent.BUTTON3_DOWN_MASK)) != 0)) {
                if ((((MazeView) SwingUtilities.getWindowAncestor(Cell.this)).getController().getPathFinder().getIsRunning())) {
                    throw new NullPointerException("Invalid input while running...");
                }
                final MazeModel gridPanel = (MazeModel) Cell.this.getParent();
                switch (((MazeView) SwingUtilities.getWindowAncestor(Cell.this)).getController().getMode()) {
                    case START:
                        if (gridPanel.getStart() != null && !Cell.this.equals(gridPanel.getStart())) {
                            gridPanel.getStart().setState(State.EMPTY);
                            Cell.this.setState(State.START);
                            gridPanel.setStart(Cell.this);
                        } else {
                            if (Cell.this.getState() == State.START) {
                                Cell.this.setState(State.EMPTY);
                                gridPanel.setStart(null);
                            } else {
                                Cell.this.setState(State.START);
                                gridPanel.setStart(Cell.this);
                            }
                        }
                        break;
                    case END:
                        if (gridPanel.getEnd() != null && !Cell.this.equals(gridPanel.getEnd())) {
                            gridPanel.getEnd().setState(State.EMPTY);
                            Cell.this.setState(State.END);
                            gridPanel.setEnd(Cell.this);
                        } else {
                            if (Cell.this.getState() == State.END) {
                                Cell.this.setState(State.EMPTY);
                                gridPanel.setEnd(null);
                            } else {
                                Cell.this.setState(State.END);
                                gridPanel.setEnd(Cell.this);
                            }
                        }
                        break;
                    case OBSTACLE:
                        Cell.this.setState(State.OBSTACLE);
                        break;
                    case EMPTY:
                        Cell.this.setState(State.EMPTY);
                        break;
                }
            }
        }

        @Override
        public final void mouseEntered(final MouseEvent e) throws NullPointerException {
            if (e.isShiftDown() && (e.getModifiersEx() & (MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK)) != 0) {
                if (((MazeView) SwingUtilities.getWindowAncestor(Cell.this)).getController().getPathFinder().getIsRunning()) {
                    throw new NullPointerException("Invalid input while running...");
                }
                switch (((MazeView) SwingUtilities.getWindowAncestor(Cell.this)).getController().getMode()) {
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
