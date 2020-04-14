package app.maze.controller.components.panel.flyweight;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import app.maze.components.cell.observer.CellObserver;
import app.maze.components.cell.subject.CellSubject;
import app.maze.controller.MazeController;

public final class PanelFlyweight extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<CellObserver> reference = new ArrayList<CellObserver>(0);

    private boolean periodic = false;

    private boolean edged = false;

    {
        // Set double buffer to prevent popping
        setDoubleBuffered(true);
        addContainerListener(new FlyweightListener());
    }

    public PanelFlyweight(final MazeController mzController) {
        setController(mzController);
        // Initialize default dimension
        setDimension(20, 20);
    }

    public PanelFlyweight() {
        this(null);
    }

    private final int transform(final int x, final int y) {
        // Transform from 2D to 1D
        return x >= 0 && x < getDimension()[0] && y >= 0 && y < getDimension()[1]
                ? x * getDimension()[0] + y
                : -1;
    }

    private final int[] transform(final int i) {
        // Transform from 1D to 2D
        return i >= 0 && i < getComponentCount()
                ? new int[] { (i - i % getDimension()[0]) / getDimension()[0], i % getDimension()[0] }
                : new int[0];
    }

    public final void reset() {
        // Override dimension
        setDimension(getDimension()[0], getDimension()[1]);
    }

    public final CellSubject request(final CellObserver o) {
        return (CellSubject) getComponents()[Arrays.asList(this.getReferences()).indexOf(o)];
    }

    public final void override(final PanelFlyweight other) {
        // Override reference
        reference = other.reference;
        // Remove components
        removeAll();
        // Override component
        for (final Component component : other.getComponents()) {
            ((CellSubject) component).setController(this.mzController);
            ((CellSubject) component).getObserver().setController(this.mzController);
            add(component);
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
                if (transform(row, col) == -1) {
                    if (!periodic)
                        continue;
                    neighbors.add(transform(
                            row < 0 || row > getDimension()[0] - 1 ? getDimension()[0] - Math.abs(row) : row,
                            col < 0 || col > getDimension()[1] - 1 ? getDimension()[1] - Math.abs(col) : col));
                // Default behaviour
                } else
                    neighbors.add(transform(row, col));
        return neighbors;
    }

    public final CellObserver[] getNeighbors(final CellObserver o) throws ArrayIndexOutOfBoundsException {
        return neighbor(Arrays.asList(getReferences()).indexOf(o)).stream()
                .map(i -> getReferences()[i])
                .toArray(CellObserver[]::new);
    }

    public final int[] getDimension() {
        final GridLayout layout = (GridLayout) getLayout();
        return new int[] { layout.getRows(), layout.getColumns() };
    }

    public synchronized final void setDimension(final int width, final int height) {
        // Override reference
        reference = new ArrayList<CellObserver>(width * height);
        // Remove components
        removeAll();
        // Override layout
        setLayout(new GridLayout(width, height));
        // Insert new cell relationships
        for (int i = 0; i < width * height; i++) {
            reference.add(new CellObserver(mzController));
            add(new CellSubject(mzController, reference.get(i)));
        }
    }

    public final CellObserver[] getReferences() {
        return reference.toArray(new CellObserver[0]);
    }

    public final boolean isEdged() {
        return edged;
    }

    public final void setEdged(final boolean edged) {
        this.edged = edged;
        mzController.reset();
    }

    public final boolean isPeriodic() {
        return periodic;
    }

    public final void setPeriodic(final boolean periodic) {
        this.periodic = periodic;
        mzController.reset();
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
