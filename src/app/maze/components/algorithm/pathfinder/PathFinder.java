package app.maze.components.algorithm.pathfinder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import app.maze.components.algorithm.AlgorithmManager;
import utils.JWrapper;

public abstract class PathFinder extends AlgorithmManager {

    private static final long serialVersionUID = 1L;

    protected EventListenerList listeners = new EventListenerList();

    protected Set<MutableTreeNode> visited;

    protected MutableTreeNode start = null;

    protected MutableTreeNode target = null;

    protected abstract MutableTreeNode advance(final Set<MutableTreeNode> currGen)
            throws StackOverflowError, InterruptedException;

    public final void find(final MutableTreeNode start, final MutableTreeNode target) {
        try {
            if (start == null)
                throw new NullPointerException("No starting node found...");
            // Set endpoints
            this.setStart(start);
            this.setTarget(target);
            // Run Thread
            this.start();
        } catch (final NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    @Override
    protected final void awake() {
        try {
            if (this.start == null)
                throw new NullPointerException("PathFinder is not initialized...");
            this.visited = new HashSet<MutableTreeNode>(0);
            final MutableTreeNode target = this.advance(new HashSet<MutableTreeNode>() {
                private static final long serialVersionUID = 1L;
                {
                    // Construct first generation
                    this.add(PathFinder.this.start);
                    // Set running
                    PathFinder.this.setRunning(true);
                }
            });
            // Traverse entire MutableTreeNode structure
            for (TreeNode parent = target.getParent(); parent.getParent() != null; parent = parent.getParent())
                fireNodeTraversed(parent);
        } catch (final NullPointerException | StackOverflowError | InterruptedException e) {
            JWrapper.dispatchException(e);
        } finally {
            // End running
            this.setRunning(false);
        }
    }

    public final void addListener(final PathFinderListener l) {
        listeners.add(PathFinderListener.class, l);
    }

    public final void removeListener(final PathFinderListener l) {
        listeners.remove(PathFinderListener.class, l);
    }

    protected final void fireNodeGerminated(TreeNode node) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PathFinderListener.class) {
                if (node == null)
                    node = new DefaultMutableTreeNode();
                ((PathFinderListener) listeners[i + 1]).nodeGerminated(node);
            }
        }
    }

    protected final void fireNodeVisited(TreeNode node) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PathFinderListener.class) {
                if (node == null)
                    node = new DefaultMutableTreeNode();
                ((PathFinderListener) listeners[i + 1]).nodeVisited(node);
            }
        }
    }

    protected final void fireNodeFound(TreeNode node) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PathFinderListener.class) {
                if (node == null)
                    node = new DefaultMutableTreeNode();
                ((PathFinderListener) listeners[i + 1]).nodeFound(node);
            }
        }
    }

    protected final void fireNodeTraversed(TreeNode node) {
        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PathFinderListener.class) {
                if (node == null)
                    node = new DefaultMutableTreeNode();
                ((PathFinderListener) listeners[i + 1]).nodeTraversed(node);
            }
        }
    }

    public final MutableTreeNode getStart() {
        return this.start;
    }

    public synchronized final void setStart(final MutableTreeNode start) {
        try {
            this.assertRunning();
            this.start = Objects.requireNonNull(start, "Start must not be null...");
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final MutableTreeNode getTarget() {
        return this.target;
    }

    public synchronized final void setTarget(final MutableTreeNode target) {
        this.target = target;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.running ? 1231 : 1237);
        result = prime * result + ((this.target == null) ? 0 : this.target.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final PathFinder other = (PathFinder) obj;
        if (this.running != other.running)
            return false;
        if (this.target == null)
            if (other.target != null)
                return false;
            else if (!this.target.equals(other.target))
                return false;
        return true;
    }

}
