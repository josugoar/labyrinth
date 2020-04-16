package app.maze.model;

import java.io.Serializable;
import java.util.Objects;

import javax.swing.JComponent;
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

    private final void update(final CellComposite node, final State state) {
        Objects.requireNonNull(state, "State must not be null...");
        final PanelFlyweight flyweight = mzController.getFlyweight();
        // Update background color matching State
        ((JComponent) flyweight.request(node)).setBackground(state.getColor());
    }

    public final void reset() {
        // Reset endpoints
        root = null;
        target = null;
        // Collapse JTree
        mzController.collapse();
    }

    private final void clear(final CellComposite node) {
        // Range through children
        for (int i = 0; i < node.getChildCount(); i++) {
            final CellComposite child = (CellComposite) node.getChildAt(i);
            // Ignore if orphan
            if (child.isOrphan())
                continue;
            // Reset State
            if (!child.equals(root) && !child.equals(target))
                update(child, State.WALKABLE);
            // Remove parent
            child.setParent(null);
            // Remove children parent
            clear(child);
        }
    }

    public final void clear() {
        // Ignore if no root
        if (root == null)
            return;
        // Remove node parent relationships
        clear((CellComposite) root);
        // Collapse JTree
        mzController.collapse();
    }

    public final void initNeighbors(final CellComposite node) {
        // Ignore if not walkable or not leaf
        if (!node.isWalkable() || !node.isLeaf())
            return;
        final PanelFlyweight flyweight = mzController.getFlyweight();
        // Range through neighbors
        for (final Object neighbor : flyweight.getNeighbors(node)) {
            // Ignore if not walkable neighbor
            if (!((CellComposite) neighbor).isWalkable())
                continue;
            // Add children
            node.add((MutableTreeNode) neighbor);
            // Add children children
            initNeighbors((CellComposite) neighbor);
        }
    }

    public final Object getTarget() {
        return target;
    }

    public final void setTarget(final TreeNode target) {
        // Override target
        if (this.target != null && this.target.equals(target)) {
            update((CellComposite) this.target, State.WALKABLE);
            this.target = null;
            mzController.collapse();
        } else {
            // Update walkable state
            if (target != null)
                ((CellComposite) target).setWalkable(true);
            // Get old target
            final TreeNode oldTarget = this.target;
            this.target = target;
            // Override old target
            if (oldTarget != null)
                update((CellComposite) oldTarget, State.WALKABLE);
            // Set new target
            if (target != null)
                update((CellComposite) target, State.TARGET);
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
            update((CellComposite) this.root, State.WALKABLE);
            this.root = null;
            mzController.collapse();
        } else {
            // Update walkable state
            if (root != null)
                ((CellComposite) root).setWalkable(true);
            // Get old root
            final TreeNode oldRoot = this.root;
            super.setRoot(root);
            // Override old root
            if (oldRoot != null)
                update((CellComposite) oldRoot, State.WALKABLE);
            // Set new root
            if (root != null)
                update((CellComposite) root, State.ROOT);
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
            ((CellComposite) root).override();
            // Initialize all node neighbors
            initNeighbors((CellComposite) root);
            // Collapse JTree
            mzController.collapse();
        }

        @Override
        public final void treeNodesRemoved(final TreeModelEvent e) {
            // Collapse JTree
            mzController.collapse();
            // Ignore if root not removed
            if (!((CellComposite) e.getTreePath().getLastPathComponent()).equals(root))
                return;
            // Remove node relationships
            for (final Object children : e.getChildren())
                ((CellComposite) children).override();
            // Reset root
            root = null;
            reload();
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
