package app.maze.components.cell;

import java.io.Serializable;

import javax.swing.tree.TreeNode;

public abstract interface Walkable extends TreeNode, Serializable {

    abstract boolean isWalkable();

    abstract void setWalkable(final boolean walkable);

}
