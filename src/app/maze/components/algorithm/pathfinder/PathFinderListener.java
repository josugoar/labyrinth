package app.maze.components.algorithm.pathfinder;

import java.util.EventListener;
import java.util.EventObject;

import javax.swing.tree.TreeNode;


public abstract interface PathFinderListener extends EventListener {

    // TODO: EventObject

    public abstract void nodeGerminated(final TreeNode node);

    public abstract void nodeVisited(final TreeNode node);

    public abstract void nodeFound(final TreeNode node);

    public abstract void nodeTraversed(final TreeNode node);

    public static final class PathFinderEvent extends EventObject {

        private static final long serialVersionUID = 1L;

        public PathFinderEvent(final Object source) {
            super(source);
        }

    }

}
