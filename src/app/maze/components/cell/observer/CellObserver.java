package app.maze.components.cell.observer;

import java.util.stream.IntStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import app.maze.controller.MazeController;
import app.maze.controller.components.panel.flyweight.PanelFlyweight;
import app.maze.model.MazeModel;

public final class CellObserver extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    private boolean walkable = true;

    public CellObserver(final MazeController mzController) {
        setController(mzController);
    }

    public CellObserver() {
        this(null);
    }

    public final void override() {
        // Ignore if leaf
        if (isLeaf())
            return;
        final Object[] oldChildren = children.toArray();
        // Remove children and children children
        removeAllChildren();
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
        return parent == null ? true : false;
    }

    public final boolean isWalkable() {
        return walkable;
    }

    public final void setWalkable(final boolean walkable) {
        final MazeModel mzModel = mzController.getModel();
        // Update walkable state
        this.walkable = walkable;
        if (walkable) {
            // Ignore if no root
            if (mzModel.getRoot() == null)
                return;
            final PanelFlyweight flyweight = mzController.getFlyweight();
            // Insert neighbors
            for (final CellObserver neighbor : flyweight.getNeighbors((this))) {
                if (!neighbor.isWalkable())
                    continue;
                if (children != null && !children.contains(neighbor))
                    add(neighbor);
                if (neighbor.children != null && !neighbor.children.contains(this))
                    neighbor.add(this);
            }
            // Override root
            if (equals(mzModel.getRoot()))
                mzModel.setRoot(null);
            // Notify insertion
            mzModel.nodesWereInserted(this, IntStream.range(0, getChildCount()).toArray());
        } else {
            // Ignore if leaf
            if (isLeaf()) {
                // Notify empty removal
                mzModel.nodesWereRemoved(this, new int[0], new Object[0]);
                return;
            }
            // Remove neighbors
            for (final Object child : this.children)
                ((CellObserver) child).children.remove(this);
            final int[] oldIndex = IntStream.range(0, getChildCount()).toArray();
            final Object[] oldChildren = children.toArray();
            removeAllChildren();
            // Notify removal
            mzModel.nodesWereRemoved(this, oldIndex, oldChildren);
        }
    }

    public transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    @Override
    public final String toString() {
        return String.format("Neighbors: %d", getChildCount());
    }

}
