package app.maze.components.algorithm.pathfinder;

import java.util.EventListener;
import java.util.EventObject;

import javax.swing.tree.TreeNode;


public abstract interface PathFinderListener extends EventListener {

    public abstract void nodeGerminated(final PathFinderEvent e);

    public abstract void nodeVisited(final PathFinderEvent e);

    public abstract void nodeFound(final PathFinderEvent e);

    public abstract void nodeTraversed(final PathFinderEvent e);

    public static final class PathFinderEvent extends EventObject {

        private static final long serialVersionUID = 1L;

        private final TreeNode[] gen;

        private final TreeNode node;

        public PathFinderEvent(final Object source, final TreeNode[] gen, final TreeNode node) {
            super(source);
            this.gen = gen;
            this.node = node;
        }

        public PathFinderEvent(final Object source, final TreeNode[] gen) {
            this(source, gen, null);
        }

        public PathFinderEvent(final Object source, final TreeNode node) {
            this(source, null, node);
        }

        public final TreeNode[] getGeneration() {
            return gen;
        }

        public final TreeNode getNode() {
            return node;
        }

    }

}
