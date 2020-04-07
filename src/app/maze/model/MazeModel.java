package app.maze.model;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import app.maze.components.cell.observer.CellObserver;
import app.maze.components.cell.subject.CellSubject;
import app.maze.controller.MazeController;

public final class MazeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    private TreeNode target = null;

    {
        this.addTreeModelListener(new TreeModelListener() {
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
                root.reset();
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
                if (!((CellObserver) e.getTreePath().getLastPathComponent()).equals(mzModel.getRoot()))
                    return;
                // Remove node relationships
                for (final Object children : e.getChildren())
                    ((CellObserver) children).reset();
                // Reset root
                mzModel.root = null;
                mzModel.reload();
            }
            @Override
            public final void treeNodesInserted(final TreeModelEvent e) {
                // Collapse tree
                ((MazeModel) e.getSource()).mzController.collapse();
            }
            @SuppressWarnings("unused")
            @Override
            public final void treeNodesChanged(final TreeModelEvent e) {
                // TODO: Path
                // Ignore if no children
                if (e.getChildren() == null)
                    return;
                for (final Object node : e.getChildren())
                    // Node changed
                    return;
            }
        });
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

    public final void clear() {
        // Ignore if no root
        if (this.root == null)
            return;
        // Remove node parent relationships
        ((CellObserver) this.root).clear();
    }

    public final void initNeighbors(final CellObserver node) {
        // Ignore if not walkable or not leaf
        if (!node.isWalkable() || !node.isLeaf())
            return;
        for (final CellObserver neighbor : this.mzController.getFlyweight().getNeighbors(node)) {
            // Ignore if not walkable
            if (!((CellObserver) neighbor).isWalkable())
                continue;
            // Add children
            node.add((CellObserver) neighbor);
            // Call until convergence
            this.initNeighbors((CellObserver) neighbor);
        }
    }

    public final TreeNode getTarget() {
        return this.target;
    }

    // TODO: Refactor

    public final void setTarget(final TreeNode target) {
        // Override target
        if (this.target != null && this.target.equals(target)) {
            this.mzController.getFlyweight().request((CellObserver) this.target).setBackground(CellSubject.State.WALKABLE.getColor());
            this.target = null;
            MazeModel.this.mzController.collapse();
        } else {
            // Update walkable state
            if (target != null)
                ((CellObserver) target).setWalkable(true);
            // Get old target
            final CellObserver oldTarget = (CellObserver) this.target;
            this.target = target;
            // Override old target
            if (oldTarget != null)
                this.mzController.getFlyweight().request(oldTarget).setBackground(CellSubject.State.WALKABLE.getColor());
            // Set new target
            if (this.target != null)
                this.mzController.getFlyweight().request((CellObserver) this.target).setBackground(CellSubject.State.TARGET.getColor());
        }
    }

    private MazeController mzController;

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
            this.mzController.getFlyweight().request((CellObserver) this.root).setBackground(CellSubject.State.WALKABLE.getColor());
            super.setRoot(null);
            MazeModel.this.mzController.collapse();
        } else {
            // Update walkable state
            if (root != null)
                ((CellObserver) root).setWalkable(true);
            // Get old root
            final CellObserver oldRoot = (CellObserver) this.root;
            super.setRoot(root);
            // Override old root
            if (oldRoot != null)
                this.mzController.getFlyweight().request(oldRoot).setBackground(CellSubject.State.WALKABLE.getColor());
            // Set new root
            if (this.root != null)
                this.mzController.getFlyweight().request((CellObserver) this.root).setBackground(CellSubject.State.ROOT.getColor());
        }
    }

}
