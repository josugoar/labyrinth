package app.maze.components.algorithm.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.swing.event.EventListenerList;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import app.maze.components.algorithm.AlgorithmManager;
import app.maze.components.algorithm.Listenable;
import app.maze.components.algorithm.TraverserListener;
import app.maze.components.algorithm.TraverserListener.TraverserEvent;
import utils.JWrapper;

public abstract class PathFinder extends AlgorithmManager implements Listenable{

    private static final long serialVersionUID = 1L;

    protected Set<TreeNode> visited;

    protected final EventListenerList listeners = new EventListenerList();

    protected MutableTreeNode root = null;

    protected MutableTreeNode target = null;

    protected abstract TreeNode advance(final Set<MutableTreeNode> currGen)
            throws StackOverflowError, InterruptedException;

    protected final void traverse(final TreeNode node) {
        final List<TreeNode> path = new ArrayList<TreeNode>(0);
        // Traverse entire TreeNode minimum spanning tree
        for (TreeNode parent = node.getParent(); parent.getParent() != null; parent = parent.getParent())
            path.add(parent);
        // Reverse TreeNodePath
        Collections.reverse(path);
        // Traverse generation
        fireNodeTraversed(new TraverserEvent(this, path.toArray(new TreeNode[0])));
    }

    public final void find(final MutableTreeNode start, final MutableTreeNode target) {
        try {
            if (start == null)
                throw new NullPointerException("No starting node found...");
            // Set endpoints
            setRoot(start);
            setTarget(target);
            // Run Thread
            start();
        } catch (final NullPointerException | InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    @Override
    protected final void awake() {
        try {
            if (root == null)
                throw new NullPointerException("PathFinder is not initialized...");
            visited = new HashSet<TreeNode>(0);
            final TreeNode tagret = advance(new HashSet<MutableTreeNode>() {
                private static final long serialVersionUID = 1L;
                {
                    // Construct first generation
                    add(root);
                    // Set running
                    setRunning(true);
                }
            });
            if (target == null)
                return;
            traverse(tagret);
        } catch (final NullPointerException | StackOverflowError | InterruptedException e) {
            JWrapper.dispatchException(e);
        } finally {
            // End running
            setRunning(false);
        }
    }

    @Override
    public final void addListener(final TraverserListener l) {
        listeners.add(TraverserListener.class, l);
    }

    @Override
    public final void removeListener(final TraverserListener l) {
        listeners.remove(TraverserListener.class, l);
    }

    @Override
    public void fireEvent(final TraverserEvent e, final BiConsumer<TraverserListener, TraverserEvent> fire) {
        Object[] listeners = this.listeners.getListenerList();
        // Range through listeners
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == TraverserListener.class)
                // Accept event
                fire.accept((TraverserListener) listeners[i + 1], e);
    }

    public final TreeNode getRoot() {
        return root;
    }

    public synchronized final void setRoot(final MutableTreeNode root) throws InterruptedException {
        assertRunning();
        this.root = Objects.requireNonNull(root, "Start must not be null...");
    }

    public final TreeNode getTarget() {
        return target;
    }

    public synchronized final void setTarget(final MutableTreeNode target) {
        this.target = target;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (running ? 1231 : 1237);
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PathFinder other = (PathFinder) obj;
        if (running != other.running)
            return false;
        if (target == null)
            if (other.target != null)
                return false;
            else if (!target.equals(other.target))
                return false;
        return true;
    }

}
