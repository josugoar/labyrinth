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

/**
 * Maze MVC Model representation, extending
 * <code>javax.swing.tree.DefaultTreeModel</code>.
 *
 * @see javax.swing.tree.DefaultTreeModel DefaultTreeModel
 */
public final class MazeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    /**
     * Target <code>javax.swing.tree.TreeNode</code>.
     */
    private TreeNode target = null;

    {
        addTreeModelListener(new ModelListener());
    }

    /**
     * Enclose MazeController.
     *
     * @param mzController MazeController
     */
    public MazeModel(final MazeController mzController) {
        super(null);
        setController(mzController);
    }

    /**
     * Create empty Model.
     */
    public MazeModel() {
        this(null);
    }

    /**
     * Hard reset endpoint <code>javax.swing.tree.TreeNode</code>.
     */
    public final void reset() {
        root = null;
        target = null;
        mzController.collapse();
    }

    /**
     * Clear <code>app.maze.components.cell.composite.CellComposite</code> node
     * parent relationships.
     *
     * @param node CellComposite
     */
    private final void clear(final CellComposite node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            final CellComposite child = (CellComposite) node.getChildAt(i);
            if (child.isOrphan())
                continue;
            if (!child.equals(root) && !child.equals(target))
                child.getView().setState(State.WALKABLE);
            child.setParent(null);
            clear(child);
        }
    }

    /**
     * Clear root level node parent relationships.
     */
    public final void clear() {
        if (root == null)
            return;
        clear((CellComposite) root);
        mzController.collapse();
    }

    /**
     * Initialize <code>app.maze.components.cell.composite.CellComposite</code> node
     * parent relationships.
     *
     * @param node CellComposite
     */
    public final void initNeighbors(final CellComposite node) {
        if (!node.isWalkable() || !node.isLeaf())
            return;
        final PanelFlyweight flyweight = mzController.getFlyweight();
        for (final Object neighbor : flyweight.getNeighbors(node)) {
            if (!((CellComposite) neighbor).isWalkable())
                continue;
            node.add((MutableTreeNode) neighbor);
            initNeighbors((CellComposite) neighbor);
        }
    }

    /**
     * Return current target <code>javax.swing.tree.TreeNode</code>.
     *
     * @return Object
     */
    public final Object getTarget() {
        return target;
    }

    /**
     * Set current target <code>javax.swing.tree.TreeNode</code>.
     *
     * @param target TreeNode
     */
    public final void setTarget(final TreeNode target) {
        if (this.target != null && this.target.equals(target)) {
            ((CellComposite) this.target).getView().setState(State.WALKABLE);
            this.target = null;
            mzController.collapse();
        } else {
            if (target != null)
                ((CellComposite) target).setWalkable(true);
            final CellComposite oldTarget = (CellComposite) this.target;
            this.target = target;
            if (oldTarget != null)
                oldTarget.getView().setState(State.WALKABLE);
            if (target != null)
                ((CellComposite) target).getView().setState(State.TARGET);
        }
    }

    @Override
    public final void setRoot(final TreeNode root) {
        if (this.root != null && this.root.equals(root)) {
            ((CellComposite) this.root).getView().setState(State.WALKABLE);
            this.root = null;
            mzController.collapse();
        } else {
            if (root != null)
                ((CellComposite) root).setWalkable(true);
            final CellComposite oldRoot = (CellComposite) this.root;
            super.setRoot(root);
            if (oldRoot != null)
                oldRoot.getView().setState(State.WALKABLE);
            if (root != null)
                ((CellComposite) root).getView().setState(State.ROOT);
        }
    }

    /**
     * <code>app.maze.controller.MazeController</code> relationship.
     */
    private transient MazeController mzController;

    /**
     * Return current <code>app.maze.controller.MazeController</code> relationship.
     *
     * @return MazeController
     */
    public final MazeController getController() {
        return mzController;
    }

    /**
     * Set current <code>app.maze.controller.MazeController</code> relationship.
     *
     * @param mzController MazeController
     */
    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    /**
     * <code>app.maze.model.MazeModel</code> listener, implementing
     * <code>javax.swing.event.TreeModelListener</code> and
     * <code>java.io.Serializable</code>.
     *
     * @see javax.swing.event.TreeModelListener
     * @see java.io.Serializable
     * @see app.maze.model.MazeModel
     */
    private final class ModelListener implements TreeModelListener, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public final void treeStructureChanged(final TreeModelEvent e) {
            if (e.getTreePath() == null) {
                mzController.collapse();
                return;
            }
            ((CellComposite) root).override();
            initNeighbors((CellComposite) root);
            mzController.collapse();
        }

        @Override
        public final void treeNodesRemoved(final TreeModelEvent e) {
            mzController.collapse();
            if (!((CellComposite) e.getTreePath().getLastPathComponent()).equals(root))
                return;
            for (final Object children : e.getChildren())
                ((CellComposite) children).override();
            root = null;
            mzController.collapse();
        }

        @Override
        public final void treeNodesInserted(final TreeModelEvent e) {
            mzController.collapse();
        }

        @Override
        public final void treeNodesChanged(final TreeModelEvent e) {
        }

    };

}
