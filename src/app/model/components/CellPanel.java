package app.model.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

    /**
     * Selected flag for menu.
     */
    public static boolean selected = false;

    {
        CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.addMouseListener(new CellPanelListener());
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
        this.stateChange();
    }

    @Override
    public final Node<CellPanel> getInner() {
        return this.inner;
    }

    @Override
    public final void setInner(final Node<CellPanel> inner) {
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

        // TODO: When selecting CellPanel check color and set same colored background

        final JPopupMenu popup = new JPopupMenu() {
            private static final long serialVersionUID = 1L;
            {
                // this.addPopupMenuListener(new PopupMenuListener() {
                //     @Override
                //     public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                //         CellPanel.selected = true;
                //     }
                //     @Override
                //     public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                //         CellPanel.selected = false;
                //     }
                //     @Override
                //     public void popupMenuCanceled(PopupMenuEvent e) {

                //     }
                // });
                this.addPropertyChangeListener("visible", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ((boolean) e.getNewValue())
                            CellPanel.selected = true;
                        else {
                            CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                            CellPanel.selected = false;
                        }
                    }
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
                CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
            if (!CellPanel.selected)
                CellPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }

    }

}
