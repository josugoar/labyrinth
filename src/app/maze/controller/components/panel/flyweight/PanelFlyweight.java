package app.maze.controller.components.panel.flyweight;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import app.maze.components.cell.composite.CellComposite;
import app.maze.components.cell.view.CellView;
import app.maze.controller.MazeController;
import app.maze.controller.components.panel.Transformable;

/**
 * Maze cell storage Flyweight representation, extending
 * <code>javax.swing.JPanel</code> and implementing
 * <code>app.maze.controller.components.panel.Transformable</code>.
 *
 * @see javax.swing.JPanel
 * @see app.maze.controller.components.panel.Transformable
 */
public final class PanelFlyweight extends JPanel implements Transformable {

    private static final long serialVersionUID = 1L;

    /**
     * Unidimensional cell storage reference.
     */
    private List<CellComposite> reference = new ArrayList<CellComposite>(0);

    /**
     * Periodic behaviour flag.
     */
    private boolean periodic = false;

    /**
     * Edged behaviour flag.
     */
    private boolean edged = false;

    {
        // IMPORTANT: Set JComponent double buffer to prevent popping
        setDoubleBuffered(true);
        addContainerListener(new FlyweightListener());
    }

    /**
     * Enclose <code>app.maze.controller.MazeController</code>.
     *
     * @param mzController MazeController
     */
    public PanelFlyweight(final MazeController mzController) {
        super(new GridLayout(1, 0, 0, 0));
        setController(mzController);
        resetDimension(20, 20);
    }

    /**
     * Create new maze cell storage Flyweight.
     */
    public PanelFlyweight() {
        this(null);
    }

    /**
     * Revert reference dimension to given size.
     *
     * @param width  int
     * @param height int
     */
    private final void revert(final int width, final int height) {
        removeAll();
        setLayout(new GridLayout(width, height, 0, 0));
        reference = new ArrayList<CellComposite>(width * height);
    }

    /**
     * Override PanelFlyweight dimension with current dimension.
     */
    public final void reset() {
        resetDimension(getRows(), getColumns());
    }

    /**
     * Request object link from reference.
     *
     * @param o Object
     * @return Object
     * @throws InvalidParameterException if (!(o instanceof CellComposite and o instanceof CellView))
     */
    public final Object request(final Object o) throws InvalidParameterException {
        Object[] in;
        Object[] out;
        synchronized (getTreeLock()) {
            if (o instanceof CellComposite) {
                in = getReferences();
                out = getComponents();
            } else if (o instanceof CellView) {
                in = getComponents();
                out = getReferences();
            } else
                throw new InvalidParameterException("Invalid Object...");
        }
        return out[Arrays.asList(in).indexOf(o)];
    }

    /**
     * Construct new overriden Flyweight from existing Flyweight.
     *
     * @param other PanelFlyweight
     */
    public final void override(final PanelFlyweight other) {
        revert(other.getColumns(), other.getRows());
        synchronized (getTreeLock()) {
            final CellComposite[] o1 = other.getReferences();
            final CellView[] o2 = other.getComponents();
            for (int i = 0; i < other.getColumns() * other.getRows(); i++)
                add(o1[i], o2[i]);
        }
    }

    /**
     * Compute neighbourig indices based on multiple spatial parameters.
     *
     * @param i int
     * @return Set<Integer>
     * @throws ArrayIndexOutOfBoundsException if (i < 0 || i >= getComponentCount())
     */
    private final Set<Integer> neighbor(final int i) throws ArrayIndexOutOfBoundsException {
        if (i < 0 || i >= getComponentCount())
            throw new ArrayIndexOutOfBoundsException("Index out of bounds...");
        final int[] dim = transform(i);
        final Set<Integer> neighbors = new HashSet<Integer>(0);
        for (int row = dim[0] - 1; row <= dim[0] + 1; row++)
            for (int col = (edged ? 0 : Math.abs(row - dim[0])) + dim[1] - 1;
                    col <= dim[1] + 1;
                    col += Math.abs((edged ? Math.abs(row - dim[0]) : 0) - 2))
                if (transform(new int[] { row, col }) == -1) {
                    if (!periodic)
                        continue;
                    neighbors.add(transform(new int[] {
                            row < 0 || row > getRows() - 1 ? getRows() - Math.abs(row) : row,
                            col < 0 || col > getColumns() - 1 ? getColumns() - Math.abs(col) : col }));
                } else
                    neighbors.add(transform(new int[] { row, col }));
        return neighbors;
    }

    /**
     * Add cell storage link reference.
     *
     * @param o1 CellComposite
     * @param o2 CellView
     * @throws NullPointerException if (o1 == null && o2 == null)
     */
    public final void add(CellComposite o1, CellView o2) throws NullPointerException {
        if (o1 == null && o2 == null)
            throw new NullPointerException("CellComposite and CellView must not be null...");
        if (o1 == null)
            o1 = new CellComposite(mzController);
        else if (o2 == null)
            o2 = new CellView(mzController);
        else {
            o1.setController(mzController);
            o2.setController(mzController);
        }
        o1.setView(o2);
        o2.setComposite(o1);
        reference.add(o1);
        add(o2);
    }

    @Override
    public final int transform(final int[] dim) throws ArrayIndexOutOfBoundsException {
        if (dim.length != 2)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds...");
        return dim[0] >= 0 && dim[0] < getRows() && dim[1] >= 0 && dim[1] < getColumns()
                ? dim[0] * getRows() + dim[1]
                : -1;
    }

    @Override
    public final int[] transform(final int i) {
        return i >= 0 && i < getComponentCount()
                ? new int[] { (i - i % getRows()) / getRows(), i % getRows() }
                : new int[0];
    }

    /**
     * Override PanelFlyweight dimension.
     *
     * @param width  int
     * @param height int
     */
    public final void resetDimension(final int width, final int height) {
        revert(width, height);
        for (int i = 0; i < width * height; i++)
            add(new CellComposite(), new CellView());
    }

    @Override
    protected final void addImpl(Component comp, Object constraints, int index)
            throws InvalidParameterException, ArrayIndexOutOfBoundsException {
        if (!(comp instanceof CellView))
            throw new InvalidParameterException("Component must be CellView...");
        synchronized (getTreeLock()) {
            if (getComponents().length + 1 != getReferences().length)
                throw new ArrayIndexOutOfBoundsException("Index out of bounds...");
        }
        super.addImpl(comp, constraints, index);
    }

    /**
     * Get neighbor storage links from neighbouring indices.
     *
     * @param o Object
     * @return Object[]
     * @throws InvalidParameterException      if (!(o instanceof CellComposite and o
     *                                        instanceof CellView))
     * @throws ArrayIndexOutOfBoundsException if (i < 0 || i >= getComponentCount())
     */
    public final Object[] getNeighbors(final Object o)
            throws InvalidParameterException, ArrayIndexOutOfBoundsException {
        Object[] a;
        synchronized (getTreeLock()) {
            if (o instanceof CellComposite)
                a = getReferences();
            else if (o instanceof CellView)
                a = getComponents();
            else
                throw new InvalidParameterException("Invalid Object...");
        }
        return neighbor(Arrays.asList(a).indexOf(o)).stream().map(i -> a[i]).toArray();
    }

    /**
     * Return <code>app.maze.components.cell.composite.CellComposite</code> references.
     *
     * @return CellComposite[]
     */
    public final CellComposite[] getReferences() {
        return reference.toArray(new CellComposite[0]);
    }

    /**
     * Return Flyweight dimension rows.
     *
     * @return int
     */
    public final int getRows() {
        return ((GridLayout) getLayout()).getRows();
    }

    /**
     * Return Flyweight dimension columns.
     *
     * @return int
     */
    public final int getColumns() {
        return ((GridLayout) getLayout()).getColumns();
    }

    /**
     * Return whether Flyweight is periodic.
     *
     * @return boolean
     */
    public final boolean isPeriodic() {
        return periodic;
    }

    /**
     * Set Flyweight periodic behaviour.
     *
     * @param periodic boolean
     */
    public final void setPeriodic(final boolean periodic) {
        this.periodic = periodic;
        mzController.reset();
    }

    /**
     * Return whether Flyweight is edged.
     *
     * @return boolean
     */
    public final boolean isEdged() {
        return edged;
    }

    /**
     * Set Flyweight edged behaviour.
     *
     * @param edged boolean
     */
    public final void setEdged(final boolean edged) {
        this.edged = edged;
        mzController.reset();
    }

    @Override
    public CellView[] getComponents() {
        final Component[] component = super.getComponents();
        return Arrays.copyOf(component, component.length, CellView[].class);
    }

    @Override
    public final void setLayout(final LayoutManager mgr) throws InvalidParameterException {
        if (!(mgr instanceof GridLayout))
            throw new InvalidParameterException("LayoutManager must be GridLayout...");
        super.setLayout(mgr);
    }

    /**
     * Current <code>app.maze.controller.MazeController</code> reference.
     */
    private transient MazeController mzController;

    /**
     * Return current <code>app.maze.controller.MazeController</code> reference.
     *
     * @return MazeController
     */
    public final MazeController getController() {
        return mzController;
    }

    /**
     * Set current <code>app.maze.controller.MazeController</code> reference.
     *
     * @param mzController MazeController
     */
    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    /**
     * <code>app.maze.controller.components.panel.flyweight.PanelFlyweight</code>
     * listener, implementing <code>java.awt.event.ContainerListener</code> and
     * <code>java.io.Serializable</code>.
     *
     * @see java.awt.event.ContainerListener
     * @see java.io.Serializable
     */
    private final class FlyweightListener implements ContainerListener, Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Update draw for each Component change.
         */
        private final void update() {
            PanelFlyweight.this.revalidate();
            PanelFlyweight.this.repaint();
        }

        @Override
        public void componentAdded(final ContainerEvent e) {
            update();
        }

        @Override
        public void componentRemoved(final ContainerEvent e) {
            update();
        }

    }

}
