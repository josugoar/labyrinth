package app.model.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import app.controller.components.AbstractCell;
import app.model.MazeModel;

/**
 * Component neighbor pointer responsible of self-reference via inner
 * <code>app.model.components.Node</code>, extending
 * <code>javax.swing.JPanel</code> and implementing
 * <code>app.controller.components.AbstractCell</code>.
 *
 * @see javax.swing.JPanel JPanel
 * @see app.controller.components.AbstractCell AbstractCell
 */
public class Cell extends JPanel implements AbstractCell<Cell> {

    private static final long serialVersionUID = 1L;

    /**
     * Euclidean space coordinate <code>java.awt.Point</code>.
     *
     * @see java.awt.Point Point
     */
    private Point seed;

    /**
     * Tree-like graph neighbor pointer storage.
     */
    private Set<Cell> neighbors;

    /**
     * Current <code>app.controller.components.AbstractCell.CellState</code>.
     *
     * @see app.controller.components.AbstractCell.CellState CellState
     */
    private CellState state = CellState.EMPTY;

    /**
     * Current inner <code>app.model.components.Node</code> pointer.
     *
     * @see app.model.components.Node Node
     */
    private Node<Cell> inner = null;

    {
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.addMouseListener(new CellListener());
    }

    /**
     * Create a new pointer storage enclosing seed and neighbors.
     *
     * @param seed      Point
     * @param neighbors Set<Cell>
     */
    public Cell(final Point seed, final Set<Cell> neighbors) {
        this.seed = seed;
        this.setNeighbors(neighbors);
    }

    /**
     * Create a new isolated vertex.
     *
     * @param seed Point
     */
    public Cell(final Point seed) {
        this(seed, null);
    }

    /**
     * Check wheter a <code>app.controller.components.AbstractCell.CellState</code>
     * can be overriden.
     */
    private final void checkOverride() {
        if (this.getState() == CellState.START)
            ((MazeModel) this.getParent()).setStart(null);
        else if (this.getState() == CellState.END)
            ((MazeModel) this.getParent()).setEnd(null);
    }

    /**
     * Return current space coordinate <code>java.awt.Point</code>.
     *
     * @return Point
     */
    public final Point getSeed() {
        return this.seed;
    }

    @Override
    public final Set<Cell> getNeighbors() {
        return this.neighbors;
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
        if (state == null)
            throw new NullPointerException("'state' must not be null");
        // Check override
        this.checkOverride();
        this.state = state;
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

    /**
     * Convenient <code>app.model.components.Cell</code>
     * <code>java.awt.event.MouseAdapter</code>.
     *
     * @see app.model.components.Cell Cell
     * @see java.awt.event.MouseAdapter MouseAdapter
     */
    private final class CellListener extends MouseAdapter {

        @Override
        public final void mousePressed(final MouseEvent e) {
            // 'model' name collides with ButtonModel 'model'
            final MazeModel ancestorModel = (MazeModel) Cell.this.getParent();
            // Check for draw state
            if (e.isShiftDown()) {
                // Check for running action
                if (!(ancestorModel.getPathFinder().getIsRunning())) {
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                        Cell.this.setState(CellState.OBSTACLE);
                    } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                        Cell.this.setState(CellState.EMPTY);
                    }
                } else {
                    System.err.println("Invalid input while running...");
                }
                // Check for popup state
            } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                // Check for running action
                if (!(ancestorModel.getPathFinder().getIsRunning())) {
                    new JPopupMenu() {
                        private static final long serialVersionUID = 1L;
                        {
                            this.addPropertyChangeListener("visible", e -> {
                                // Select Cell
                                if ((boolean) e.getNewValue())
                                    Cell.this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                                else
                                    Cell.this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                            });
                            this.add(new JMenuItem("Start") {
                                private static final long serialVersionUID = 1L;
                                {
                                    this.addActionListener(e -> ancestorModel.setStart(Cell.this));
                                }
                            });
                            this.add(new JMenuItem("End") {
                                private static final long serialVersionUID = 1L;
                                {
                                    this.addActionListener(e -> ancestorModel.setEnd(Cell.this));
                                }
                            });
                        }
                    }.show(Cell.this, e.getX(), e.getY());
                } else {
                    System.err.println("Invalid input while running...");
                }
            }
        }

        @Override
        public final void mouseEntered(final MouseEvent e) {
            // 'model' name collides with ButtonModel 'model'
            final MazeModel ancestorModel = (MazeModel) Cell.this.getParent();
            if (e.isShiftDown()) {
                if (!(ancestorModel.getPathFinder().getIsRunning())) {
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {

                        Cell.this.setState(CellState.OBSTACLE);
                    } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {

                        Cell.this.setState(CellState.EMPTY);
                    }
                } else {
                    System.err.println("Invalid input while running...");
                }
            }
        }

    }

}
