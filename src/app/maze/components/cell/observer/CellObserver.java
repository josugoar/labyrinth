package app.maze.components.cell.observer;

import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import app.maze.controller.MazeController;

public final class CellObserver extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    // TODO: Weights match with child index
    @SuppressWarnings("unused")
    private Vector<Integer> weights = new Vector<Integer>();

    private boolean walkable = true;

    public CellObserver(final MazeController mzController) {
        this.setController(mzController);
    }

    public CellObserver() {
        this(null);
    }

    public final void override() {
        // Ignore if no children
        if (this.getChildCount() == 0)
            return;
        // Remove children and children children
        final Object[] oldChildren = this.children.toArray();
        this.removeAllChildren();
        for (final Object oldChild : oldChildren)
            ((CellObserver) oldChild).override();
    }

    @Override
    public final void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
        // Initialize node with no parent
        newChild.setParent(null);
    }

    public final boolean isOrphan() {
        return this.parent == null ? true : false;
    }

    public final boolean isWalkable() {
        return this.walkable;
    }

    public final void setWalkable(final boolean walkable) {
        // Update walkable state
        this.walkable = walkable;
        if (this.walkable) {
            // Ignore if no root
            if (this.mzController.getModel().getRoot() == null)
                return;
            // Update neighbors
            for (final CellObserver neighbor : this.mzController.getFlyweight().getNeighbors((this))) {
                if (!neighbor.isWalkable())
                    continue;
                if (this.children != null && !this.children.contains(neighbor))
                    this.add(neighbor);
                if (neighbor.children != null && !neighbor.children.contains(this))
                    neighbor.add(this);
            }
            // Override root
            if (this.equals(this.mzController.getModel().getRoot()))
                this.mzController.getModel().setRoot(null);
            this.mzController.getModel().nodesWereInserted(this, IntStream.range(0, this.getChildCount()).toArray());
        } else {
            // Ignore if no children
            if (this.getChildCount() == 0) {
                this.mzController.getModel().nodesWereRemoved(this, new int[0], new Object[0]);
                return;
            }
            // Remove neighbors
            for (final Object child : this.children)
                ((CellObserver) child).children.remove(this);
            final int[] oldIndex = IntStream.range(0, this.getChildCount()).toArray();
            final Object[] oldChildren = this.children.toArray();
            this.removeAllChildren();
            this.mzController.getModel().nodesWereRemoved(this, oldIndex, oldChildren);
        }
    }

    public transient MazeController mzController;

    public final MazeController getController() {
        return this.mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    @Override
    public final String toString() {
        return String.format("Neighbors: %d", this.getChildCount());
    }

}
