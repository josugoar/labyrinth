package app.maze.components.cell.composite;

import java.util.stream.IntStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import app.maze.components.cell.State;
import app.maze.components.cell.Walkable;
import app.maze.components.cell.view.CellView;
import app.maze.controller.MazeController;
import app.maze.controller.components.panel.flyweight.PanelFlyweight;
import app.maze.model.MazeModel;

public final class CellComposite extends DefaultMutableTreeNode implements Walkable {

    // TODO: Reference CellView

    private static final long serialVersionUID = 1L;

    private boolean walkable = true;

    public CellComposite(final MazeController mzController) {
        setController(mzController);
    }

    public CellComposite() {
        this(null);
    }

    public final void override() {
        // Ignore if DefaultMutableTreeNode leaf
        if (isLeaf())
            return;
        final Object[] oldChildren = children.toArray();
        // Remove MutableTreeNode children
        removeAllChildren();
        // Remove MutableTreeNode children children
        for (final Object oldChild : oldChildren)
            ((CellComposite) oldChild).override();
    }

    @Override
    public final void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
        // Initialize MutableTreeNode with no parent
        newChild.setParent(null);
    }

    public final boolean isOrphan() {
        return parent == null ? true : false;
    }

    @Override
    public final boolean isWalkable() {
        return walkable;
    }

    @Override
    public final void setWalkable(final boolean walkable) {
        final MazeModel mzModel = mzController.getModel();
        final PanelFlyweight flyweight = mzController.getFlyweight();
        // Update Walkable state
        this.walkable = walkable;
        if (walkable) {
            // Update CellView background
            ((CellView) flyweight.request(this)).setBackground(State.WALKABLE);
            // Ignore if no TreeModel root
            if (mzModel.getRoot() == null)
                return;
            // Insert CellComposite neighbors
            for (final Object neighbor : flyweight.getNeighbors(this)) {
                // Ignore if not Walkable
                if (!((CellComposite) neighbor).isWalkable())
                    continue;
                // Add CellComposite neighbors if not MutableTreeNode children
                if (children != null && !children.contains(neighbor))
                    add((MutableTreeNode) neighbor);
                if (((CellComposite) neighbor).children != null && !((CellComposite) neighbor).children.contains(this))
                    ((DefaultMutableTreeNode) neighbor).add(this);
            }
            // Override DefaultTreeModel root
            if (equals(mzModel.getRoot()))
                mzModel.setRoot(null);
            // Notify TreeModelListener insertion
            mzModel.nodesWereInserted(this, IntStream.range(0, getChildCount()).toArray());
        } else {
            // Update CellView background
            ((CellView) flyweight.request(this)).setBackground(State.UNWALKABLE);
            // Ignore if DefaultMutableTreeNode leaf
            if (isLeaf()) {
                // Notify TreeModelListener empty removal
                mzModel.nodesWereRemoved(this, new int[0], new Object[0]);
                return;
            }
            // Remove CellComposite neighbors
            for (final Object child : children)
                ((CellComposite) child).children.remove(this);
            // Get old DefaultMutableTreeNode enpoints
            final int[] oldIndex = IntStream.range(0, getChildCount()).toArray();
            final Object[] oldChildren = children.toArray();
            // Remove all DefaultMutableTreeNode children
            removeAllChildren();
            // Notify TreeModelListener removal
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
