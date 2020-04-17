package app.maze.model.components.tree.factory;

import java.io.Serializable;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class TreeFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final TreeModel createTreeModel() {
        // Construct new DefaultTreeModel with empty TreeNode
        return new DefaultTreeModel(new DefaultMutableTreeNode("No root node..."));
    }

    public static final void putTreeModel(final JTree tree, final TreeModel model) {
        // Delete JTree TreeModel to collapse all paths
        tree.setModel(null);
        // Reset JTree TreeModel
        tree.setModel(model);
    }

}
