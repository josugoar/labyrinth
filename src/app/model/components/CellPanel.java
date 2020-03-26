package app.model.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import app.controller.components.AbstractCell;
import app.model.MazePanel;
import utils.JWrapper;

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
     * Focused flag for menu.
     */
    private transient static boolean focused = false;

    /**
     * Slected <code>focused</code> pointer.
     */
    private static CellPanel selected = null;

    /**
     * Ancestor <code>app.model.MazePanel</code> pointer.
     *
     * @see app.model.MazePanel MazePanel
     */
    public MazePanel ancestor;

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
     * Current inner <code>app.model.components.Node</code> pointer.
     *
     * @see app.model.components.Node Node
     */
    private Node<CellPanel> inner = null;

    /**
     * Current <code>app.controller.components.AbstractCell.CellState</code>.
     *
     * @see app.controller.components.AbstractCell.CellState CellState
     */
    private CellState state = CellState.EMPTY;

    {
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    }

    /**
     * Create a new pointer storage enclosing ancestor, seed and neighbors.
     *
     * @param seed      Point
     * @param neighbors Set<CellPanel>
     * @param ancestor  MazePanel
     */
    public CellPanel(final MazePanel ancestor, final Point seed, final Set<CellPanel> neighbors) {
        this.seed = seed;
        this.setNeighbors(neighbors);
        this.setAncestor(ancestor);
        this.addMouseListener(this.new CellPanelListener());
    }

    /**
     * Create a new isolated vertex.
     *
     * @param seed     Point
     * @param ancestor MazePanel
     */
    public CellPanel(final MazePanel ancestor, final Point seed) {
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
        else if (this.inner == null)
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        else
            this.setBorder(BorderFactory.createLineBorder(this.inner.getState().getReference()));
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
    public final void notifyChange() {
        this.revalidate();
        this.repaint();
    }

    /**
     * Return current selected pointer.
     *
     * @return CellPanel
     */
    public static final CellPanel getSelected() {
        return CellPanel.selected;
    }

    /**
     * Set current selected pointer.
     *
     * @param selected CellPanel
     */
    public synchronized static final void setSelected(final CellPanel selected) {
        if (selected != null)
            selected.paintSelection();
        else if (CellPanel.selected != null)
            CellPanel.selected.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        CellPanel.selected = selected;
    }

    /**
     * Return selected flag for menu.
     *
     * @return boolean
     */
    public static final boolean isFocused() {
        return CellPanel.focused;
    }

    /**
     * Set focused flag for menu.
     *
     * @param focused boolean
     */
    public synchronized final void setFocused(final boolean focused) {
        if (focused)
            CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        else
            CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        CellPanel.focused = focused;
    }

    /**
     * Return current <code>app.model.MazePanel</code> pointer.
     *
     * @return MazePanel
     */
    public final MazePanel getAncestor() {
        return this.ancestor;
    }

    /**
     * Set <code>app.model.MazePanel</code> pointer.
     *
     * @param ancestor MazePanel
     */
    public final void setAncestor(final MazePanel ancestor) {
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
    public final Node<CellPanel> getInner() {
        return this.inner;
    }

    @Override
    public final void setInner(final Node<CellPanel> inner) {
        this.inner = inner;
        this.notifyChange();
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
    private final class CellPanelListener extends MouseAdapter implements Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public synchronized final void mousePressed(final MouseEvent e) {
            CellPanel.this.ancestor.clear();
            // Check for running action
            try {
                // Check for draw state
                if (e.isShiftDown()) {
                    CellPanel.this.ancestor.assertIsRunning();
                    // Left input
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                        CellPanel.this.setState(CellState.OBSTACLE);
                    // Right input
                    else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                        CellPanel.this.setState(CellState.EMPTY);
                    // Check for popup state
                } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                    CellPanel.this.ancestor.assertIsRunning();
                    CellPanel.this.ancestor.releaseCellPopup(CellPanel.this).show(CellPanel.this, e.getX(), e.getY());
                }
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        }

        @Override
        public synchronized final void mouseEntered(final MouseEvent e) {
            // Select Cell
            if (!CellPanel.focused)
                CellPanel.setSelected(CellPanel.this);
            try {
                if (e.isShiftDown()) {
                    // Left input
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                        CellPanel.this.ancestor.assertIsRunning();
                        CellPanel.this.setState(CellState.OBSTACLE);
                        // Right input
                    } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                        CellPanel.this.ancestor.assertIsRunning();
                        CellPanel.this.setState(CellState.EMPTY);
                    }
                }
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        }

        @Override
        public synchronized final void mouseExited(final MouseEvent e) {
            if (!CellPanel.focused)
                CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }

    }

}
