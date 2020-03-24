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
public class CellPanel extends JPanel implements AbstractCell<CellPanel> {

    private static final long serialVersionUID = 1L;

    /**
     * Selected flag for menu.
     */
    public static boolean selected = false;

    /**
     * Ancestor <code>app.model.MazeModel</code> pointer.
     *
     * @see app.model.MazeModel MazeModel
     */
    public MazeModel ancestor;

    /**
     * Euclidean space coordinate <code>java.awt.Point</code>.
     *
     * @see java.awt.Point Point
     */
    private final Point seed;

    /**
     * Tree-like graph neighbor pointer storage.
     */
    private Set<CellPanel> neighbors;

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
    private Node<CellPanel> inner = null;

    {
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    }

    /**
     * Create a new pointer storage enclosing ancestor, seed and neighbors.
     *
     * @param seed      Point
     * @param neighbors Set<CellPanel>
     * @param ancestor  MazeModel
     */
    public CellPanel(final MazeModel ancestor, final Point seed, final Set<CellPanel> neighbors) {
        this.seed = seed;
        this.setNeighbors(neighbors);
        this.setAncestor(ancestor);
        this.addMouseListener(this.new CellPanelListener());
    }

    /**
     * Create a new isolated vertex.
     *
     * @param seed     Point
     * @param ancestor MazeModel
     */
    public CellPanel(final MazeModel ancestor, final Point seed) {
        this(ancestor, seed, null);
    }

    /**
     * Recursively traverse entire <code>app.model.components.CellPanel</code> and
     * <code>app.model.components.Node</code> tree structure for maximum
     * performance.
     */
    public final void clear() {
        if (this.getInner() != null) {
            this.setInner(null);
            for (final CellPanel child : this.getNeighbors())
                child.clear();
        }
    }

    /**
     * Check wheter a <code>app.controller.components.AbstractCell.CellState</code>
     * can be overriden.
     */
    private final void checkOverride() {
        if (this.getState() == CellState.START)
            this.ancestor.setStart(null);
        else if (this.getState() == CellState.END)
            this.ancestor.setEnd(null);
    }

    /**
     * Paint border color selection.
     */
    protected final void paintSelection() {
        if (this.state != CellState.EMPTY)
            this.setBorder(BorderFactory.createLineBorder(this.state.getReference()));
        else
            if (this.inner == null)
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            else
                this.setBorder(BorderFactory.createLineBorder(this.inner.getState().getReference()));
    }

    /**
     * Return selected flag for menu.
     *
     * @return boolean
     */
    public final boolean isSelected() {
        return CellPanel.selected;
    }

    /**
     * Set selected flag for menu.
     *
     * @param selected boolean
     */
    public final void setSelected(final boolean selected) {
        if (selected)
            CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        else
            CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        CellPanel.selected = selected;
    }

    /**
     * Return current <code>app.model.MazeModel</code> pointer.
     *
     * @return MazeModel
     */
    public final MazeModel getAncestor() {
        return this.ancestor;
    }

    /**
     * Set <code>app.model.MazeModel</code> pointer.
     *
     * @param ancestor MazeModel
     */
    public final void setAncestor(final MazeModel ancestor) {
        this.ancestor = Objects.requireNonNull(ancestor, "'ancestor' must not be null");
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
    public final Set<CellPanel> getNeighbors() {
        return this.neighbors;
    }

    @Override
    public final void setNeighbors(final Set<CellPanel> neighbors) {
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
        this.notifyChange();
    }

    @Override
    public final Node<CellPanel> getInner() {
        return this.inner;
    }

    @Override
    public final void setInner(final Node<CellPanel> inner) {
        this.inner = inner;
        this.notifyChange();
    }

    @Override
    public final void notifyChange() {
        this.revalidate();
        this.repaint();
    }

    @Override
    public final void paintComponent(final Graphics g) {
        if (this.inner != null && this.getState() == CellPanel.CellState.EMPTY)
            g.setColor(this.inner.getState().getReference());
        else
            g.setColor(this.state.getReference());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public final String toString() {
        return String.format("CellPanel [state: %s, seed: (%d, %d)]", this.state, this.seed.x, this.seed.y);
    }

    /**
     * Convenient <code>app.model.components.CellPanel</code>
     * <code>java.awt.event.MouseAdapter</code>.
     *
     * @see app.model.components.CellPanel CellPanel
     * @see java.awt.event.MouseAdapter MouseAdapter
     */
    private final class CellPanelListener extends MouseAdapter {

        @Override
        public final void mousePressed(final MouseEvent e) {
            CellPanel.this.ancestor.fireClear();
            // Check for running action
            try {
                // Check for draw state
                if (e.isShiftDown()) {
                    if (CellPanel.this.ancestor.getPathFinder().getIsRunning())
                        throw new InterruptedException("Invalid input while running...");
                    // Left input
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                        CellPanel.this.setState(CellState.OBSTACLE);
                    // Right input
                    else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                        CellPanel.this.setState(CellState.EMPTY);
                    // Check for popup state
                } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                    if (CellPanel.this.ancestor.getPathFinder().getIsRunning())
                        throw new InterruptedException("Invalid input while running...");
                    CellPanel.this.ancestor.requestCellPopup(CellPanel.this).show(CellPanel.this, e.getX(), e.getY());
                }
            } catch (InterruptedException l) {
                System.err.println(l.toString());
            }
        }

        @Override
        public final void mouseEntered(final MouseEvent e) {
            // Select Cell
            if (!CellPanel.selected)
                CellPanel.this.paintSelection();
            try {
                if (e.isShiftDown()) {
                    if (CellPanel.this.ancestor.getPathFinder().getIsRunning())
                        throw new InterruptedException("Invalid input while running...");
                    // Left input
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                        CellPanel.this.setState(CellState.OBSTACLE);
                    // Right input
                    else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                        CellPanel.this.setState(CellState.EMPTY);
                }
            } catch (InterruptedException l) {
                System.err.println(l.toString());
            }
        }

        @Override
        public final void mouseExited(final MouseEvent e) {
            if (!CellPanel.selected)
                CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }

    }

}