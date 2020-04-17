package app.maze.components.algorithm;

import java.io.Serializable;
import java.util.function.BiConsumer;

import app.maze.components.algorithm.TraverserListener.TraverserEvent;

public abstract interface Listenable extends Serializable {

    abstract void addListener(final TraverserListener l);

    abstract void removeListener(final TraverserListener l);

    abstract void fireEvent(final TraverserEvent e, final BiConsumer<TraverserListener, TraverserEvent> fire);

    default void fireNodeGerminated(final TraverserEvent e) {
        fireEvent(e, (l, n) -> l.nodeGerminated(n));
    }

    default void fireNodeVisited(final TraverserEvent e) {
        fireEvent(e, (l, n) -> l.nodeVisited(n));
    }

    default void fireNodeReached(final TraverserEvent e) {
        fireEvent(e, (l, n) -> l.nodeReached(n));
    }

    default void fireNodeTraversed(final TraverserEvent e) {
        fireEvent(e, (l, n) -> l.nodeTraversed(n));
    }


}
