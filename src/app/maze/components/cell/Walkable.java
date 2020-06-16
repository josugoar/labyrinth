package app.maze.components.cell;

import java.io.Serializable;

import javax.swing.tree.TreeNode;

/**
 * Walkable node, extending <code>javax.swing.tree.TreeNode</code> and
 * <code>java.io.Serializable</code>.
 */
public abstract interface Walkable extends TreeNode, Serializable {

    /**
     * Return wheter node is walkable.
     *
     * @return boolean
     */
    abstract boolean isWalkable();

    /**
     * Set node walkable state.
     *
     * @param walkable boolean
     */
    abstract void setWalkable(final boolean walkable);

}
