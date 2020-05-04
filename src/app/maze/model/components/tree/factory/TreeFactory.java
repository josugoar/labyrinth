package app.maze.model.components.tree.factory;

import java.io.Serializable;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * <code>javax.swing</code> tree factory provider, implementing
 * <code>java.io.Serializable</code>.
 *
 * @see java.io.Serializable Serializable
 */
public class TreeFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Create arbitrary <code>javax.swing.tree.TreeModel</code>.
     *
     * @return TreeModel
     */
    public static final TreeModel createTreeModel() {
        return new DefaultTreeModel(new DefaultMutableTreeNode("No root node..."));
    }

    /**
     * Reset <code>javax.swing.JTree</code> <code>javax.swing.tree.TreeModel</code>.
     *
     * @param tree  JTree
     * @param model TreeModel
     */
    public static final void putTreeModel(final JTree tree, final TreeModel model) {
        tree.setModel(null);
        tree.setModel(model);
    }

}
