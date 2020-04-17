package app.maze.model;

import java.io.Serializable;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import app.maze.components.cell.State;
import app.maze.components.cell.composite.CellComposite;
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

    public final void reset() {
        // Hard reset MazeModel endpoints
        root = null;
        target = null;
        // Collapse JTree
        mzController.collapse();
    }

    private final void clear(final CellComposite node) {
        // Range through DefaultMutableTreeNode children
        for (int i = 0; i < node.getChildCount(); i++) {
            final CellComposite child = (CellComposite) node.getChildAt(i);
            // Ignore if CellComposite orphan
            if (child.isOrphan())
                continue;
            // Reset CellView State
            if (!child.equals(root) && !child.equals(target))
                child.getView().setState(State.WALKABLE);
            // Remove DefaultMutableTreeNode parent
            child.setParent(null);
            // Remove CellComposite children parent
            clear(child);
        }
    }

    public final void clear() {
        // Ignore if no MazeModel root
        if (root == null)
            return;
        // Remove CellComposite parent relationships
        clear((CellComposite) root);
        // Collapse JTree
        mzController.collapse();
    }

    public final void initNeighbors(final CellComposite node) {
        // Ignore if CellComposite not walkable or not leaf
        if (!node.isWalkable() || !node.isLeaf())
            return;
        final PanelFlyweight flyweight = mzController.getFlyweight();
        // Range through CellComposite neighbors
        for (final Object neighbor : flyweight.getNeighbors(node)) {
            // Ignore if CellComposite not walkable neighbor
            if (!((CellComposite) neighbor).isWalkable())
                continue;
            // Add MutableTreeNode children
            node.add((MutableTreeNode) neighbor);
            // Add CellComposite children children
            initNeighbors((CellComposite) neighbor);
        }
    }

    public final Object getTarget() {
        return target;
    }

    public final void setTarget(final TreeNode target) {
        // Override MazeModel target
        if (this.target != null && this.target.equals(target)) {
            ((CellComposite) this.target).getView().setState(State.WALKABLE);
            this.target = null;
            mzController.collapse();
        } else {
            // Update Walkable state
            if (target != null)
                ((CellComposite) target).setWalkable(true);
            // Get old MazeModel target
            final CellComposite oldTarget = (CellComposite) this.target;
            this.target = target;
            // Override old MazeModel target
            if (oldTarget != null)
                oldTarget.getView().setState(State.WALKABLE);
            // Set new MazeModel target
            if (target != null)
                ((CellComposite) target).getView().setState(State.TARGET);
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
        // Override MazeModel root
        if (this.root != null && this.root.equals(root)) {
            ((CellComposite) this.root).getView().setState(State.WALKABLE);
            this.root = null;
            mzController.collapse();
        } else {
            // Update Walkable state
            if (root != null)
                ((CellComposite) root).setWalkable(true);
            // Get old MazeModel root
            final CellComposite oldRoot = (CellComposite) this.root;
            super.setRoot(root);
            // Override old MazeModel root
            if (oldRoot != null)
                oldRoot.getView().setState(State.WALKABLE);
            // Set new MazeModel root
            if (root != null)
                ((CellComposite) root).getView().setState(State.ROOT);
        }
    }

    private final class ModelListener implements TreeModelListener, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public final void treeStructureChanged(final TreeModelEvent e) {
            // Ignore if no MazeModel root
            if (e.getTreePath() == null) {
                mzController.collapse();
                return;
            }
            // Remove CellComposite relationships
            ((CellComposite) root).override();
            // Initialize all CellComposite neighbors
            initNeighbors((CellComposite) root);
            // Collapse JTree
            mzController.collapse();
        }

        @Override
        public final void treeNodesRemoved(final TreeModelEvent e) {
            // Collapse JTree
            mzController.collapse();
            // Ignore if MazeModel root not removed
            if (!((CellComposite) e.getTreePath().getLastPathComponent()).equals(root))
                return;
            // Remove CellComposite relationships
            for (final Object children : e.getChildren())
                ((CellComposite) children).override();
            // Reset MazeModel root
            root = null;
            // Collapse JTree
            mzController.collapse();
        }

        @Override
        public final void treeNodesInserted(final TreeModelEvent e) {
            // Collapse JTree
            mzController.collapse();
        }

        @Override
        public final void treeNodesChanged(final TreeModelEvent e) {
        }

    };

}
