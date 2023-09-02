package app.maze.components.cell.composite;

import java.util.Arrays;
import java.util.Vector;
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

    private static final long serialVersionUID = 1L;

    private boolean walkable = true;

    public CellComposite(final MazeController mzController) {
        setController(mzController);
    }

    public CellComposite() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public final void override() {
        // Ignore if DefaultMutableTreeNode leaf
        if (isLeaf())
            return;
        // Remove MutableTreeNode children
        ((Vector<CellComposite>)children.clone()).forEach(child -> {
            removeAllChildren();
            child.override();
        });
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

    @SuppressWarnings("unchecked")
    @Override
    public final void setWalkable(final boolean walkable) {
        final MazeModel mzModel = mzController.getModel();
        final PanelFlyweight flyweight = mzController.getFlyweight();
        // Update Walkable state
        this.walkable = walkable;
        if (walkable) {
            final Object root = mzModel.getRoot();
            // Update CellView background
            clView.setState(State.WALKABLE);
            // Ignore if no TreeModel root
            if (root == null)
                return;
            // Get Object neighbors
            final Object[] neighbors = flyweight.getNeighbors(this);
            // Insert CellComposite neighbors
            for (final CellComposite neighbor : Arrays.copyOf(neighbors, neighbors.length, CellComposite[].class)) {
                // Ignore if not Walkable
                if (!neighbor.isWalkable())
                    continue;
                // Add CellComposite neighbors if not MutableTreeNode children
                if (children != null && !children.contains(neighbor))
                    add(neighbor);
                if (neighbor.children != null && !neighbor.children.contains(this))
                    neighbor.add(this);
            }
            // Override DefaultTreeModel root
            if (equals(root))
                mzModel.setRoot(null);
            // Notify TreeModelListener insertion
            mzModel.nodesWereInserted(this, IntStream.range(0, getChildCount()).toArray());
        } else {
            // Update CellView background
            clView.setState(State.UNWALKABLE);
            if (isLeaf())
                // Notify TreeModelListener empty removal
                mzModel.nodesWereRemoved(this, new int[0], new Object[0]);
            else {
                // Remove CellComposite neighbors
                ((Vector<CellComposite>)children.clone()).forEach(child -> child.children.remove(this));
                // Get old DefaultMutableTreeNode enpoints
                final int[] oldIndex = IntStream.range(0, getChildCount()).toArray();
                final Object[] oldChildren = children.toArray();
                // Remove all DefaultMutableTreeNode children
                removeAllChildren();
                // Notify TreeModelListener removal
                mzModel.nodesWereRemoved(this, oldIndex, oldChildren);
            }
        }
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    private CellView clView;

    public final CellView getView() {
        return clView;
    }

    public final void setView(final CellView clView) {
        this.clView = clView;
    }

    @Override
    public final String toString() {
        return String.format("Neighbors: %d", getChildCount());
    }

}
