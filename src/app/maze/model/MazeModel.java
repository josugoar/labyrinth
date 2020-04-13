package app.maze.model;

import java.io.Serializable;
import java.util.function.BiConsumer;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import app.maze.components.cell.State;
import app.maze.components.cell.observer.CellObserver;
import app.maze.controller.MazeController;

public final class MazeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    private TreeNode target = null;

    {
        this.addTreeModelListener(new ModelListener());
    }

    public MazeModel(final MazeController mzController) {
        super(null);
        this.setController(mzController);
    }

    public MazeModel() {
        this(null);
    }

    public final void reset() {
        // Reset endpoints
        this.root = null;
        this.target = null;
        // Collapse tree
        this.mzController.collapse();
    }

    private final void clear(final CellObserver node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            final CellObserver child = (CellObserver) node.getChildAt(i);
            // Ignore if no parent
            if (child.isOrphan())
                continue;
            // Reset state
            if (!child.equals(this.root) && !child.equals(this.target))
                this.update.accept(child, State.WALKABLE);
            // Remove parent and children parent
            child.setParent(null);
            this.clear(child);
        }
    }

    public final void clear() {
        // Ignore if no root
        if (this.root == null)
            return;
        // Remove node parent relationships
        this.clear((CellObserver) this.root);
    }

    public final void initNeighbors(final CellObserver node) {
        // Ignore if not walkable or not leaf
        if (!node.isWalkable() || !node.isLeaf())
            return;
        for (final CellObserver neighbor : this.mzController.getFlyweight().getNeighbors(node)) {
            // Ignore if not walkable neighbor
            if (!neighbor.isWalkable())
                continue;
            // Add children
            node.add(neighbor);
            // Call until convergence
            this.initNeighbors(neighbor);
        }
    }

    public final Object getTarget() {
        return this.target;
    }

    @SuppressWarnings("unchecked")
    private final BiConsumer<CellObserver, State> update = (BiConsumer<CellObserver, State> & Serializable) (node, state) ->
            this.mzController.getFlyweight().request(node).setBackground(state.getColor());

    public final void setTarget(final TreeNode target) {
        // Override target
        if (this.target != null && this.target.equals(target)) {
            this.update.accept((CellObserver) this.target, State.WALKABLE);
            this.target = null;
            MazeModel.this.mzController.collapse();
        } else {
            // Update walkable state
            if (target != null)
                ((CellObserver) target).setWalkable(true);
            // Get old target
            final TreeNode oldTarget = this.target;
            this.target = target;
            // Override old target
            if (oldTarget != null)
                this.update.accept((CellObserver) oldTarget, State.WALKABLE);
            // Set new target
            if (this.target != null)
                this.update.accept((CellObserver) this.target, State.TARGET);
        }
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return this.mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    @Override
    public final void setRoot(final TreeNode root) {
        // Override root
        if (this.root != null && this.root.equals(root)) {
            this.update.accept((CellObserver) this.root, State.WALKABLE);
            this.root = null;
            MazeModel.this.mzController.collapse();
        } else {
            // Update walkable state
            if (root != null)
                ((CellObserver) root).setWalkable(true);
            // Get old root
            final TreeNode oldRoot = this.root;
            super.setRoot(root);
            // Override old root
            if (oldRoot != null)
                this.update.accept((CellObserver) oldRoot, State.WALKABLE);
            // Set new root
            if (this.root != null)
                this.update.accept((CellObserver) this.root, State.ROOT);
        }
    }

    private static final class ModelListener implements TreeModelListener, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public final void treeStructureChanged(final TreeModelEvent e) {
            final MazeModel mzModel = (MazeModel) e.getSource();
            // Ignore if no root
            if (e.getTreePath() == null) {
                mzModel.mzController.collapse();
                return;
            }
            final CellObserver root = (CellObserver) e.getTreePath().getLastPathComponent();
            // Remove node relationships
            root.override();
            // Initialize all node neighbors
            mzModel.initNeighbors(root);
            // Collapse tree
            mzModel.mzController.collapse();
        }

        @Override
        public final void treeNodesRemoved(final TreeModelEvent e) {
            final MazeModel mzModel = (MazeModel) e.getSource();
            // Collapse tree
            mzModel.mzController.collapse();
            // Ignore if root not removed
            if (!((CellObserver) e.getTreePath().getLastPathComponent()).equals(mzModel.root))
                return;
            // Remove node relationships
            for (final Object children : e.getChildren())
                ((CellObserver) children).override();
            // Reset root
            mzModel.root = null;
            mzModel.reload();
        }

        @Override
        public final void treeNodesInserted(final TreeModelEvent e) {
            // Collapse tree
            ((MazeModel) e.getSource()).mzController.collapse();
        }

        @Override
        public final void treeNodesChanged(final TreeModelEvent e) {
            // Collapse tree
            ((MazeModel) e.getSource()).mzController.collapse();
        }

    };

}