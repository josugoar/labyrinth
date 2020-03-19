package app.model.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import app.controller.components.CellController;
import app.model.MazeModel;
import app.view.MazeView;

public class Cell extends JPanel implements CellController<Cell> {

    private static final long serialVersionUID = 1L;

    private Point seed;

    private Set<Cell> neighbors;

    private CellState state = CellState.EMPTY;

    private Node<Cell> inner = null;

    {
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.addMouseListener(new CellListener());
    }

    public Cell(final Point seed, final Set<Cell> neighbors) {
        this.seed = seed;
        this.setNeighbors(neighbors);
    }

    public Cell(final Point seed) {
        this(seed, null);
    }

    public final Point getSeed() {
        return this.seed;
    }

    @Override
    public final void setNeighbors(final Set<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public final CellState getState() {
        return this.state;
    }

    @Override
    public final void setState(final CellState state) {
        this.state = Objects.requireNonNull(state, "'state' must not be null");
        this.stateChange();
    }

    @Override
    public final Node<Cell> getInner() {
        return this.inner;
    }

    @Override
    public final void setInner(final Node<Cell> inner) {
        this.inner = inner;
        this.stateChange();
    }

    @Override
    public final Set<Cell> getNeighbors() {
        return this.neighbors;
    }

    @Override
    public final void stateChange() {
        this.revalidate();
        this.repaint();
    }

    @Override
    public final void paintComponent(final Graphics g) {
        if (this.inner != null && this.getState() == Cell.CellState.EMPTY) {
            g.setColor(this.inner.getState().getColor());
        } else {
            g.setColor(this.state.getColor());
        }
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public final String toString() {
        return String.format("Cell [state: %s, seed: (%d, %d)]", this.state, this.seed.x, this.seed.y);
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
                            gridPanel.getStart().setState(CellState.EMPTY);
                            Cell.this.setState(CellState.START);
                            gridPanel.setStart(Cell.this);
                        } else {
                            if (Cell.this.getState() == CellState.START) {
                                Cell.this.setState(CellState.EMPTY);
                                gridPanel.setStart(null);
                            } else {
                                Cell.this.setState(CellState.START);
                                gridPanel.setStart(Cell.this);
                            }
                        }
                        break;
                    case END:
                        if (gridPanel.getEnd() != null && !Cell.this.equals(gridPanel.getEnd())) {
                            gridPanel.getEnd().setState(CellState.EMPTY);
                            Cell.this.setState(CellState.END);
                            gridPanel.setEnd(Cell.this);
                        } else {
                            if (Cell.this.getState() == CellState.END) {
                                Cell.this.setState(CellState.EMPTY);
                                gridPanel.setEnd(null);
                            } else {
                                Cell.this.setState(CellState.END);
                                gridPanel.setEnd(Cell.this);
                            }
                        }
                        break;
                    case OBSTACLE:
                        Cell.this.setState(CellState.OBSTACLE);
                        break;
                    case EMPTY:
                        Cell.this.setState(CellState.EMPTY);
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
                        Cell.this.setState(CellState.OBSTACLE);
                        break;
                    case EMPTY:
                        Cell.this.setState(CellState.EMPTY);
                        break;
                    default:
                }
            }
        }

    }

}
