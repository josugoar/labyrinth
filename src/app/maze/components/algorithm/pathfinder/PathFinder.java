package app.maze.components.algorithm.pathfinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import app.maze.components.algorithm.AlgorithmManager;
import utils.JWrapper;

public abstract class PathFinder extends AlgorithmManager {

    private static final long serialVersionUID = 1L;

    protected List<PathFinderListener> listeners = new ArrayList<PathFinderListener>(0);

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
                for (final PathFinderListener listener : this.listeners)
                    listener.nodeTraversed(parent);
        } catch (final NullPointerException | StackOverflowError | InterruptedException e) {
            JWrapper.dispatchException(e);
        } finally {
            // End running
            this.setRunning(false);
        }
    }

    public final void addListener(final PathFinderListener listener) {
        this.listeners.add(Objects.requireNonNull(listener, "PathFinderListener must not be null..."));
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
