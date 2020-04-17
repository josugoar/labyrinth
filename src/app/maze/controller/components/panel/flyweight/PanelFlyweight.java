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

public final class PanelFlyweight extends JPanel implements Transformable {

    private static final long serialVersionUID = 1L;

    private List<CellComposite> reference = new ArrayList<CellComposite>(0);

    private boolean periodic = false;

    private boolean edged = false;

    {
        // Set JComponent double buffer to prevent popping
        setDoubleBuffered(true);
        addContainerListener(new FlyweightListener());
    }

    public PanelFlyweight(final MazeController mzController) {
        super(new GridLayout(1, 0, 0, 0));
        setController(mzController);
        // Initialize PanelFlyweight default dimension
        resetDimension(20, 20);
    }

    public PanelFlyweight() {
        this(null);
    }

    private final void revert(final int width, final int height) {
        // Remove Component to prevent overlapping
        removeAll();
        // Update LayoutManager to pack components
        setLayout(new GridLayout(width, height, 0, 0));
        // Override PanelFlyweight reference
        reference = new ArrayList<CellComposite>(width * height);
    }

    public final void reset() {
        // Override PanelFlyweight dimension with current dimension
        resetDimension(getRows(), getColumns());
    }

    public final Object request(final Object o) throws InvalidParameterException {
        // Initialize empty arrays
        Object[] in;
        Object[] out;
        // Set Object I/O order
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
        // Return other Object entry
        return out[Arrays.asList(in).indexOf(o)];
    }

    public final void override(final PanelFlyweight other) {
        // Revert PanelFlyweight components with other dimension
        revert(other.getColumns(), other.getRows());
        // Get other PanelFlyweight components
        synchronized (getTreeLock()) {
            final CellComposite[] o1 = other.getReferences();
            final CellView[] o2 = other.getComponents();
            // Override PanelFlyweight components
            for (int i = 0; i < other.getColumns() * other.getRows(); i++)
                add(o1[i], o2[i]);
        }
    }

    private final Set<Integer> neighbor(final int i) throws ArrayIndexOutOfBoundsException {
        if (i < 0 || i >= getComponentCount())
            throw new ArrayIndexOutOfBoundsException("Index out of bounds...");
        // Get transformed dimension
        final int[] dim = transform(i);
        // Initialize empty surrounding neighbors
        final Set<Integer> neighbors = new HashSet<Integer>(0);
        for (int row = dim[0] - 1; row <= dim[0] + 1; row++)
            // Initilize column index depending on row position
            for (int col = (edged ? 0 : Math.abs(row - dim[0])) + dim[1] - 1;
                    col <= dim[1] + 1;
                    col += Math.abs((edged ? Math.abs(row - dim[0]) : 0) - 2))
                // Periodic behaviour
                if (transform(new int[] { row, col }) == -1) {
                    if (!periodic)
                        continue;
                    neighbors.add(transform(new int[] {
                            row < 0 || row > getRows() - 1 ? getRows() - Math.abs(row) : row,
                            col < 0 || col > getColumns() - 1 ? getColumns() - Math.abs(col) : col }));
                // Default behaviour
                } else
                    neighbors.add(transform(new int[] { row, col }));
        return neighbors;
    }

    public final void add(CellComposite o1, CellView o2) throws NullPointerException {
        if (o1 == null && o2 == null)
            throw new NullPointerException("CellComposite and CellView must not be null...");
        // Single Object relationships
        if (o1 == null)
            o1 = new CellComposite(mzController);
        else if (o2 == null)
            o2 = new CellView(mzController);
        // Multiple Object relationships
        else {
            o1.setController(mzController);
            o2.setController(mzController);
        }
        o1.setView(o2);
        o2.setComposite(o1);
        // Add components
        reference.add(o1);
        add(o2);
    }

    @Override
    public final int transform(final int[] dim) throws ArrayIndexOutOfBoundsException {
        if (dim.length != 2)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds...");
        // Transform from 2D to 1D
        return dim[0] >= 0 && dim[0] < getRows() && dim[1] >= 0 && dim[1] < getColumns()
                ? dim[0] * getRows() + dim[1]
                : -1;
    }

    @Override
    public final int[] transform(final int i) {
        // Transform from 1D to 2D
        return i >= 0 && i < getComponentCount()
                ? new int[] { (i - i % getRows()) / getRows(), i % getRows() }
                : new int[0];
    }

    public final void resetDimension(final int width, final int height) {
        // Revert PanelFlyweight components with new dimension
        revert(width, height);
        // Insert new Object relationships
        for (int i = 0; i < width * height; i++)
            add(new CellComposite(), new CellView());
    }

    @Override
    protected final void addImpl(Component comp, Object constraints, int index)
            throws InvalidParameterException, ArrayIndexOutOfBoundsException {
        // Prevent non-CellView Component from being added
        if (!(comp instanceof CellView))
            throw new InvalidParameterException("Component must be CellView...");
        // Prevent non-linked CellView from being added
        synchronized (getTreeLock()) {
            if (getComponents().length + 1 != getReferences().length)
                throw new ArrayIndexOutOfBoundsException("Index out of bounds...");
        }
        super.addImpl(comp, constraints, index);
    }

    public final Object[] getNeighbors(final Object o)
            throws InvalidParameterException, ArrayIndexOutOfBoundsException {
        // Initialize empty array
        Object[] a;
        synchronized (getTreeLock()) {
            if (o instanceof CellComposite)
                a = getReferences();
            else if (o instanceof CellView)
                a = getComponents();
            else
                throw new InvalidParameterException("Invalid Object...");
        }
        // Map index to Object neighbors
        return neighbor(Arrays.asList(a).indexOf(o)).stream().map(i -> a[i]).toArray();
    }

    public final CellComposite[] getReferences() {
        // Return CellComposite only array
        return reference.toArray(new CellComposite[0]);
    }

    public final int getRows() {
        return ((GridLayout) getLayout()).getRows();
    }

    public final int getColumns() {
        return ((GridLayout) getLayout()).getColumns();
    }

    public final boolean isPeriodic() {
        return periodic;
    }

    public final void setPeriodic(final boolean periodic) {
        this.periodic = periodic;
        // Reset structure
        mzController.reset();
    }

    public final boolean isEdged() {
        return edged;
    }

    public final void setEdged(final boolean edged) {
        this.edged = edged;
        // Reset structure
        mzController.reset();
    }

    @Override
    public CellView[] getComponents() {
        final Component[] component = super.getComponents();
        // Return CellView only array
        return Arrays.copyOf(component, component.length, CellView[].class);
    }

    @Override
    public final void setLayout(final LayoutManager mgr) throws InvalidParameterException {
        // Prevent from setting non-GridLayout LayoutManager
        if (!(mgr instanceof GridLayout))
            throw new InvalidParameterException("LayoutManager must be GridLayout...");
        super.setLayout(mgr);
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    private final class FlyweightListener implements ContainerListener, Serializable {

        private static final long serialVersionUID = 1L;

        private final void update() {
            // Update draw for each Component change
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
