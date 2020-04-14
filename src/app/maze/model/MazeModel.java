package app.maze.model;

import java.io.Serializable;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import app.maze.components.cell.State;
import app.maze.components.cell.observer.CellObserver;
import app.maze.controller.MazeController;
import app.maze.controller.components.panel.flyweight.PanelFlyweight;

public final class MazeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    private TreeNode target = null;

    {
        addTreeModelListener(new ModelListener());
    }

    public MazeModel(final MazeController mzController) {
        super(null);
        setController(mzController);
    }

    public MazeModel() {
        this(null);
    }

    private final void update(final CellObserver node, final State state) {
        final PanelFlyweight flyweight = mzController.getFlyweight();
        // Set state background
        flyweight.request(node).setBackground(state.getColor());
    }

    public final void reset() {
        // Reset endpoints
        root = null;
        target = null;
        // Collapse tree
        mzController.collapse();
    }

    private final void clear(final CellObserver node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            final CellObserver child = (CellObserver) node.getChildAt(i);
            // Ignore if no parent
            if (child.isOrphan())
                continue;
            // Reset state
            if (!child.equals(root) && !child.equals(target))
                update(child, State.WALKABLE);
            // Remove parent and children parent
            child.setParent(null);
            clear(child);
        }
    }

    public final void clear() {
        // Ignore if no root
        if (root == null)
            return;
        // Remove node parent relationships
        clear((CellObserver) root);
        // Collapse tree
        mzController.collapse();
    }

    public final void initNeighbors(final CellObserver node) {
        // Ignore if not walkable or not leaf
        if (!node.isWalkable() || !node.isLeaf())
            return;
        final PanelFlyweight flyweight = mzController.getFlyweight();
        // Range through neighbors
        for (final CellObserver neighbor : flyweight.getNeighbors(node)) {
            // Ignore if not walkable neighbor
            if (!neighbor.isWalkable())
                continue;
            // Add children
            node.add(neighbor);
            initNeighbors(neighbor);
        }
    }

    public final Object getTarget() {
        return target;
    }

    public final void setTarget(final TreeNode target) {
        // Override target
        if (this.target != null && this.target.equals(target)) {
            update((CellObserver) this.target, State.WALKABLE);
            this.target = null;
            mzController.collapse();
        } else {
            // Update walkable state
            if (target != null)
                ((CellObserver) target).setWalkable(true);
            // Get old target
            final TreeNode oldTarget = this.target;
            this.target = target;
            // Override old target
            if (oldTarget != null)
                update((CellObserver) oldTarget, State.WALKABLE);
            // Set new target
            if (target != null)
                update((CellObserver) target, State.TARGET);
        }
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    @Override
    public final void setRoot(final TreeNode root) {
        // Override root
        if (this.root != null && this.root.equals(root)) {
            update((CellObserver) this.root, State.WALKABLE);
            this.root = null;
            mzController.collapse();
        } else {
            // Update walkable state
            if (root != null)
                ((CellObserver) root).setWalkable(true);
            // Get old root
            final TreeNode oldRoot = this.root;
            super.setRoot(root);
            // Override old root
            if (oldRoot != null)
                update((CellObserver) oldRoot, State.WALKABLE);
            // Set new root
            if (root != null)
                update((CellObserver) root, State.ROOT);
        }
    }

    private final class ModelListener implements TreeModelListener, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public final void treeStructureChanged(final TreeModelEvent e) {
            // Ignore if no root
            if (e.getTreePath() == null) {
                mzController.collapse();
                return;
            }
            // Remove node relationships
            ((CellObserver) root).override();
            // Initialize all node neighbors
            initNeighbors((CellObserver) root);
            // Collapse tree
            mzController.collapse();
        }

        @Override
        public final void treeNodesRemoved(final TreeModelEvent e) {
            // Collapse tree
            mzController.collapse();
            // Ignore if root not removed
            if (!((CellObserver) e.getTreePath().getLastPathComponent()).equals(root))
                return;
            // Remove node relationships
            for (final Object children : e.getChildren())
                ((CellObserver) children).override();
            // Reset root
            root = null;
            reload();
        }

        @Override
        public final void treeNodesInserted(final TreeModelEvent e) {
            // Collapse tree
            mzController.collapse();
        }

        @Override
        public final void treeNodesChanged(final TreeModelEvent e) {
        }

    };

}
