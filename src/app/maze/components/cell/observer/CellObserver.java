package app.maze.components.cell.observer;

import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import app.maze.model.components.flyweight.MazeFlyweight;

public final class CellObserver extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    // TODO: Weights match with child index
    @SuppressWarnings("unused")
    private Vector<Integer> weights = new Vector<Integer>();

    private boolean walkable = true;

    public CellObserver(final MazeFlyweight mzFlyweight) {
        this.setFlyweight(mzFlyweight);
    }

    public CellObserver() {
        this(null);
    }

    public final void reset() {
        // Ignore if no children
        if (this.getChildCount() == 0)
            return;
        // Remove children and children children
        final Object[] oldChildren = this.children.toArray();
        this.removeAllChildren();
        for (final Object oldChild : oldChildren)
            ((CellObserver) oldChild).reset();
    }

    public final void clear() {
        for (final Object child : this.children)
            // Ignore if no parent
            if (((CellObserver) child).getParent() != null) {
                // Remove parent and children parent
                ((CellObserver) child).setParent(null);
                ((CellObserver) child).clear();
            }
    }

    @Override
    public final void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
        // Initialize node with no parent
        newChild.setParent(null);
    }

    public final boolean isOrphan() {
        return this.parent == null ? false : true;
    }

    public final boolean isWalkable() {
        return this.walkable;
    }

    public final void setWalkable(final boolean walkable) {
        // Update walkable state
        this.walkable = walkable;
        if (this.walkable) {
            // Ignore if no root
            if (this.mzFlyweight.getRoot() == null)
                return;
            // Update neighbors
            for (final CellObserver neighbor : this.mzFlyweight.getNeighbors((this))) {
                if (!neighbor.isWalkable())
                    continue;
                if (this.children != null && !this.children.contains(neighbor))
                    this.add(neighbor);
                if (neighbor.children != null && !neighbor.children.contains(this))
                    neighbor.add(this);
            }
            // Override root
            if (this.equals(this.mzFlyweight.getRoot())) {
                this.mzFlyweight.setRoot(null);
                this.mzFlyweight.notifyRootChange();
            }
            this.mzFlyweight.notifyInsertion(this, IntStream.range(0, this.getChildCount()).toArray());
        } else {
            // Ignore if no children
            if (this.getChildCount() != 0) {
                // Remove neighbors
                for (final Object child : this.children)
                    ((CellObserver) child).children.remove(this);
                final int[] oldIndex = IntStream.range(0, this.getChildCount()).toArray();
                final Object[] oldChildren = this.children.toArray();
                this.removeAllChildren();
                this.mzFlyweight.notifyRemoval(this, oldIndex, oldChildren);
            }
            this.mzFlyweight.notifyRemoval(this, new int[0], new Object[0]);
        }
    }

    public MazeFlyweight mzFlyweight;

    public final void setFlyweight(final MazeFlyweight mzFlyweight) {
        this.mzFlyweight = mzFlyweight;
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
        super.setParent(newParent);
        this.mzFlyweight.notifyChange(this);
    }

    @Override
    public String toString() {
        return String.format("Neighbors: %d", this.getChildCount());
    }

}
