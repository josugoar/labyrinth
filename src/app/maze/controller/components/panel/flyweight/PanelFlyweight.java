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

    // TODO: Iterable

    private static final long serialVersionUID = 1L;

    private List<CellComposite> reference = new ArrayList<CellComposite>(0);

    private boolean periodic = false;

    private boolean edged = false;

    {
        // Set double buffer to prevent popping
        setDoubleBuffered(true);
        addContainerListener(new FlyweightListener());
    }

    public PanelFlyweight(final MazeController mzController) {
        super(new GridLayout());
        setController(mzController);
        // Initialize default dimension
        resetDimension(20, 20);
    }

    public PanelFlyweight() {
        this(null);
    }

    private final int transform(final int x, final int y) {
        // Transform from 2D to 1D
        return x >= 0 && x < getRows() && y >= 0 && y < getColumns() ? x * getRows() + y : -1;
    }

    public final void reset() {
        // Override dimension
        resetDimension(getRows(), getColumns());
    }

    public final Object request(final Object o) throws InvalidParameterException {
        // Initialize empty arrays
        Object[] in;
        Object[] out;
        if (o instanceof CellComposite) {
            in = getReferences();
            out = getComponents();
        } else if (o instanceof CellView) {
            in = getComponents();
            out = getReferences();
        } else
            throw new InvalidParameterException("Invalid Object...");
        // Return other representation
        return out[Arrays.asList(in).indexOf(o)];
    }

    public synchronized final void override(final PanelFlyweight other) {
        // Override reference
        reference = other.reference;
        // Remove components
        removeAll();
        // Update layout
        setLayout(new GridLayout(other.getColumns(), other.getRows()));
        // Override component
        for (final CellView component : other.getComponents())
            add(component.getComposite(), component);
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
                if (transform(row, col) == -1) {
                    if (!periodic)
                        continue;
                    neighbors.add(transform(
                            row < 0 || row > getRows() - 1 ? getRows() - Math.abs(row) : row,
                            col < 0 || col > getColumns() - 1 ? getColumns() - Math.abs(col) : col));
                    // Default behaviour
                } else
                    neighbors.add(transform(row, col));
        return neighbors;
    }

    public final void add(CellComposite o1, CellView o2) throws NullPointerException {
        if (o1 == null && o2 == null)
            throw new NullPointerException("CellComposite and CellView must not be null...");
        // Single relationships
        if (o1 == null)
            o1 = new CellComposite();
        else if (o2 == null)
            o2 = new CellView();
        // Set relationships
        o1.setController(mzController);
        o2.setController(mzController);
        o2.setComposite(o1);
        // Add components
        add(o2);
        reference.add(o1);
    }

    @Override
    public final int[] transform(final int i) {
        // Transform from 1D to 2D
        return i >= 0 && i < getComponentCount()
                ? new int[] { (i - i % getRows()) / getRows(), i % getRows() }
                : new int[0];
    }

    public synchronized final void resetDimension(final int width, final int height) {
        // Override reference
        reference = new ArrayList<CellComposite>(width * height);
        // Remove components
        removeAll();
        // Override layout
        setLayout(new GridLayout(width, height, 0, 0));
        // Insert new cell relationships
        for (int i = 0; i < width * height; i++)
            add(new CellComposite(), new CellView());
    }

    @Override
    protected final void addImpl(Component comp, Object constraints, int index) throws InvalidParameterException {
        if (!(comp instanceof CellView))
            throw new InvalidParameterException("Component must be CellView...");
        super.addImpl(comp, constraints, index);
    }

    public final CellComposite[] getReferences() {
        return reference.toArray(new CellComposite[0]);
    }

    public final CellComposite[] getNeighbors(final CellComposite o) throws ArrayIndexOutOfBoundsException {
        return neighbor(reference.indexOf(o)).stream().map(i -> reference.get(i)).toArray(CellComposite[]::new);
    }

    public final int getColumns() {
        return ((GridLayout) getLayout()).getColumns();
    }

    public final int getRows() {
        return ((GridLayout) getLayout()).getRows();
    }

    public final boolean isPeriodic() {
        return periodic;
    }

    public final void setPeriodic(final boolean periodic) {
        this.periodic = periodic;
        mzController.reset();
    }

    public final boolean isEdged() {
        return edged;
    }

    public final void setEdged(final boolean edged) {
        this.edged = edged;
        mzController.reset();
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
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

    private final class FlyweightListener implements ContainerListener, Serializable {

        private static final long serialVersionUID = 1L;

        private final void update(final Component component) {
            // Update draw for each Component change
            component.revalidate();
            component.repaint();
        }

        @Override
        public void componentAdded(final ContainerEvent e) {
            update(PanelFlyweight.this);
        }

        @Override
        public void componentRemoved(final ContainerEvent e) {
            update(PanelFlyweight.this);
        }

    }

}
