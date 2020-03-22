package app.model.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import app.controller.components.AbstractCell;
import app.model.MazeModel;
import app.view.components.FocusedPopup;

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
     * Ancestor <code>app.model.MazeModel</code> pointer.
     */
    public final MazeModel ancestor;

    /**
     * Euclidean space coordinate <code>java.awt.Point</code>.
     *
     * @see java.awt.Point Point
     */
    private Point seed;

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

    // TODO: Fix selection
    /**
     * Selected flag for menu.
     */
    public static boolean selected = false;

    {
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        // this.addMouseListener(this.new CellPanelListener());
    }

    /**
     * Create a new pointer storage enclosing ancestor, seed and neighbors.
     *
     * @param ancestor  MazeModel
     * @param seed      Point
     * @param neighbors Set<CellPanel>
     */
    public CellPanel(final MazeModel ancestor, final Point seed, final Set<CellPanel> neighbors) {
        this.ancestor = ancestor;
        this.seed = seed;
        this.setNeighbors(neighbors);
        this.addMouseListener(this.new CellPanelListener());
    }

    /**
     * Create a new isolated vertex.
     *
     * @param ancestor MazeModel
     * @param seed     Point
     */
    public CellPanel(final MazeModel ancestor, final Point seed) {
        this(ancestor, seed, null);
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
     * Recursively traverse entire <code>app.model.components.CellPanel</code> and
     * <code>app.model.components.Node</code> tree structure.
     */
    public final void clear() {
        if (this.getInner() != null) {
            this.setInner(null);
            for (CellPanel child : this.getNeighbors()) {
                child.clear();
            }
        }
    }

    /**
     * Paint border color selection.
     */
    protected final void paintSelection() {
        if (this.state == CellPanel.CellState.START || this.state == CellPanel.CellState.END) {
            this.setBorder(BorderFactory.createLineBorder(this.state.getColor()));
        } else {
            if (this.inner == null) {
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            } else {
                this.setBorder(BorderFactory.createLineBorder(this.inner.getState().getColor()));
            }
        }
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
        if (this.inner != null && this.getState() == CellPanel.CellState.EMPTY) {
            g.setColor(this.inner.getState().getColor());
        } else {
            g.setColor(this.state.getColor());
        }
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

        /**
         * <code>javax.swing.JPopupMenu</code> containing selection logic.
         *
         * @see javax.swing.JPopupMenu JPopupMenu
         */
        final JPopupMenu popup = new FocusedPopup(CellPanel.this.ancestor.getController().getView()) {
            private static final long serialVersionUID = 1L;
            {
                this.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        CellPanel.selected = true;
                    }
                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                         CellPanel.selected = false;
                    }
                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) { }
                });
                this.add(new JMenuItem("Start") {
                    private static final long serialVersionUID = 1L;
                    {
                        this.addActionListener(e -> CellPanel.this.ancestor.setStart(CellPanel.this));
                    }
                });
                this.add(new JMenuItem("End") {
                    private static final long serialVersionUID = 1L;
                    {
                        this.addActionListener(e -> CellPanel.this.ancestor.setEnd(CellPanel.this));
                    }
                });
            }
        };

        @Override
        public final void mousePressed(final MouseEvent e) {
            if (!(CellPanel.this.ancestor.getPathFinder().getIsRunning()))
                ancestor.fireClear();
            // Check for draw state
            if (e.isShiftDown()) {
                // Check for running action
                if (!(CellPanel.this.ancestor.getPathFinder().getIsRunning())) {
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                        CellPanel.this.setState(CellState.OBSTACLE);
                    else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                        CellPanel.this.setState(CellState.EMPTY);
                } else {
                    System.err.println("Invalid input while running...");
                }
                // Check for popup state
            } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                // Check for running action
                if (!(CellPanel.this.ancestor.getPathFinder().getIsRunning())) {
                    CellPanel.selected = true;
                    this.popup.show(CellPanel.this, e.getX(), e.getY());
                } else {
                    System.err.println("Invalid input while running...");
                }
            }
        }

        @Override
        public final void mouseEntered(final MouseEvent e) {
            // Select Cell
            if (!CellPanel.selected)
                CellPanel.this.paintSelection();
            if (e.isShiftDown()) {
                if (!(CellPanel.this.ancestor.getPathFinder().getIsRunning())) {
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {

                        CellPanel.this.setState(CellState.OBSTACLE);
                    } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {

                        CellPanel.this.setState(CellState.EMPTY);
                    }
                } else {
                    System.err.println("Invalid input while running...");
                }
            }
        }

        @Override
        public final void mouseExited(final MouseEvent e) {
            if (!CellPanel.selected) {
                CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }
        }

    }

}
