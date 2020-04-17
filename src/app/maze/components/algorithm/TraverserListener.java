package app.maze.components.algorithm;

import java.io.Serializable;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.tree.TreeNode;

public abstract interface TraverserListener extends EventListener, Serializable {

    public abstract void nodeGerminated(final TraverserEvent e);

    public abstract void nodeVisited(final TraverserEvent e);

    public abstract void nodeReached(final TraverserEvent e);

    public abstract void nodeTraversed(final TraverserEvent e);

    public static final class TraverserEvent extends EventObject {

        private static final long serialVersionUID = 1L;

        private final TreeNode node;

        private final TreeNode[] gen;

        public TraverserEvent(final Object source, final TreeNode[] gen, final TreeNode node) {
            super(source);
            this.gen = gen;
            this.node = node;
        }

        public TraverserEvent(final Object source, final TreeNode[] gen) {
            this(source, gen, null);
        }

        public TraverserEvent(final Object source, final TreeNode node) {
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
