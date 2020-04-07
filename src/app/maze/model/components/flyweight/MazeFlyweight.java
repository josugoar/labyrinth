package app.maze.model.components.flyweight;
// TODO: Declare component
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import app.maze.components.cell.observer.CellObserver;
import app.maze.components.cell.subject.CellSubject;
import app.maze.controller.MazeController;
import utils.Delegate;

public final class MazeFlyweight extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<CellObserver> reference = new ArrayList<CellObserver>(0);

    private boolean periodic = false;

    private boolean edged = false;

    {
        // Enable double buffer to prevent popping
        this.setDoubleBuffered(true);
        // Update draw for each Component change
        this.addContainerListener(new ContainerListener() {
            private final void update(final Component component) {
                component.revalidate();
                component.repaint();
            }
            @Override
            public void componentAdded(final ContainerEvent e) {
                this.update((Component) e.getSource());
            }

            @Override
            public void componentRemoved(final ContainerEvent e) {
                this.update((Component) e.getSource());
            }
        });
    }

    public MazeFlyweight(final MazeController mzController) {
        this.setController(mzController);
        // Initialize default dimension
        this.setDimension(20, 20);
    }

    public MazeFlyweight() {
        this(null);
    }

    public final void override() {
        // Override Component
        this.setDimension(this.getDimension()[0], this.getDimension()[1]);
    }

    public final int transform(final int x, final int y) {
        // Transform from 2D to 1D
        return x >= 0 && x < this.getDimension()[0] && y >= 0 && y < this.getDimension()[1]
                ? x * this.getDimension()[0] + y
                : -1;
    }

    public final int[] transform(final int i) {
        // Transform from 1D to 2D
        return i >= 0 && i < this.getComponentCount()
                ? new int[] { (i - i % this.getDimension()[0]) / this.getDimension()[0], i % this.getDimension()[0] }
                : new int[0];
    }

    public final CellSubject request(final CellObserver o) {
        return (CellSubject) this.getComponents()[Arrays.asList(this.getReferences()).indexOf(o)];
    }

    private final Set<Integer> neighbor(final int i) throws ArrayIndexOutOfBoundsException {
        if (i < 0 || i >= this.getComponentCount())
            throw new ArrayIndexOutOfBoundsException("Index out of bounds...");
        // Get transformed dimension
        final int[] dim = this.transform(i);
        // Initialize empty surrounding neighbors
        final Set<Integer> neighbors = new HashSet<Integer>(0);
        for (int row = dim[0] - 1; row <= dim[0] + 1; row++)
            // Initilize column index depending on row position
            for (int col = (this.edged ? 0 : Math.abs(row - dim[0])) + dim[1] - 1;
                    col <= dim[1] + 1;
                    col += Math.abs((this.edged ? Math.abs(row - dim[0]) : 0) - 2))
                // Periodic behaviour
                if (this.transform(row, col) == -1) {
                    if (!this.periodic)
                        continue;
                    neighbors.add(this.transform(
                            row < 0 || row > this.getDimension()[0] - 1 ? this.getDimension()[0] - Math.abs(row) : row,
                            col < 0 || col > this.getDimension()[1] - 1 ? this.getDimension()[1] - Math.abs(col) : col));
                // Default behaviour
                } else
                    neighbors.add(this.transform(row, col));
        return neighbors;
    }

    public final CellObserver[] getNeighbors(final CellObserver o) throws ArrayIndexOutOfBoundsException {
        return this.neighbor(Arrays.asList(this.getReferences()).indexOf(o)).stream().map(i -> this.getReferences()[i]).toArray(CellObserver[]::new);
    }

    public final int[] getDimension() {
        return new int[] { ((GridLayout) this.getLayout()).getRows(), ((GridLayout) this.getLayout()).getColumns() };
    }

    public synchronized final void setDimension(final int width, final int height) {
        // Override reference
        this.reference = new ArrayList<CellObserver>(width * height);
        // Remove Component to avoid overlapping
        this.removeAll();
        // Override LayoutManager
        this.setLayout(new GridLayout(width, height));
        // Insert new cell relationships
        for (int i = 0; i < width * height; i++) {
            this.reference.add(new CellObserver(this));
            this.add(new CellSubject(this.mzController, this.reference.get(i)));
        }
    }

    public final CellObserver[] getReferences() {
        return this.reference.toArray(new CellObserver[0]);
    }

    public final boolean isEdged() {
        return this.edged;
    }

    public final void setEdged(final boolean edged) {
        this.edged = edged;
    }

    public final boolean isPeriodic() {
        return this.periodic;
    }

    public final void setPeriodic(final boolean periodic) {
        this.periodic = periodic;
    }

    public MazeController mzController;

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    @Delegate(target = MazeController.class)
    public final CellObserver getRoot() {
        return (CellObserver) this.mzController.getModel().getRoot();
    }

    @Delegate(target = MazeController.class)
    public final void setRoot(final CellObserver root) {
        this.mzController.getModel().setRoot(root);
    }

    @Delegate(target = MazeController.class)
    public final void notifyRootChange() {
        this.mzController.getModel().reload();
    }

    @Delegate(target = MazeController.class)
    public final void notifyRemoval(final CellObserver node, final int[] childIndices, final Object[] removedChildren) {
        this.mzController.getModel().nodesWereRemoved(node, childIndices, removedChildren);
    }

    @Delegate(target = MazeController.class)
    public final void notifyInsertion(final CellObserver node, final int[] childIndices) {
        this.mzController.getModel().nodesWereInserted(node, childIndices);
    }

    @Delegate(target = MazeController.class)
    public final void notifyChange(final CellObserver node) {
        this.mzController.getModel().nodeChanged(node);
    }

}
