package app.maze.components.algorithm.generator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.swing.event.EventListenerList;
import javax.swing.tree.TreeNode;

import app.maze.components.algorithm.AlgorithmManager;
import app.maze.components.algorithm.Listenable;
import app.maze.components.algorithm.TraverserListener;
import app.maze.components.algorithm.TraverserListener.TraverserEvent;
import app.maze.components.cell.Walkable;
import utils.JWrapper;

public abstract class Generator extends AlgorithmManager implements Listenable {

    private static final long serialVersionUID = 1L;

    protected Set<Walkable> visited;

    protected final EventListenerList listeners = new EventListenerList();

    protected Walkable root = null;

    protected int density = 50;

    protected abstract void advance(final Walkable node) throws InterruptedException;

    public final void generate(final Walkable start) {
        try {
            if (start == null)
                throw new NullPointerException("No starting node found...");
            // Set start
            setRoot(start);
            // Run Thread
            start();
        } catch (final NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    @Override
    protected final void awake() {
        try {
            if (root == null)
                throw new NullPointerException("Generator is not initialized...");
            visited = new HashSet<Walkable>(0);
            setRunning(true);
            advance(root);
            fireNodeReached(new TraverserEvent(this, (TreeNode) null));
        } catch (final NullPointerException | InterruptedException e) {
            JWrapper.dispatchException(e);
        } finally {
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
        final Object[] listeners = this.listeners.getListenerList();
        // Range through listeners
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == TraverserListener.class)
                // Accept event
                fire.accept((TraverserListener) listeners[i + 1], e);
    }

    public final Walkable getRoot() {
        return root;
    }

    public final void setRoot(final Walkable root) {
        this.root = Objects.requireNonNull(root, "Walkable must not be null...");
    }

    public final int getDensity() {
        return density;
    }

    public final void setDensity(final int density) {
        this.density = density;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + density;
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
        final Generator other = (Generator) obj;
        if (density != other.density)
            return false;
        return true;
    }

}
