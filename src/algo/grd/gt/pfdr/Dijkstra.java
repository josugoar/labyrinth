package algo.grd.gt.pfdr;

import java.security.spec.AlgorithmParameterSpec;
import java.util.HashSet;
import java.util.Set;

import algo.grd.dsa.AbstractCell;
import algo.grd.dsa.Node;
import algo.grd.gt.PathFinder;

/**
 * Dijkstra pathfinding algorithm implementation, extending
 * <code>app.model.PathFinder</code>.
 *
 * @see app.model.PathFinder PathFinder
 */
public final class Dijkstra<T extends AbstractCell<T>> extends PathFinder<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Visit all <code>app.model.components.Node</code> in generation.
     *
     * @param <T> AbstractCell<T>
     * @param gen Set<Node<T>>
     */
    protected static final <T extends AbstractCell<T>> void visit(final Set<Node<T>> gen) {
        for (final Node<T> node : gen)
            node.setState(Node.NodeState.VISITED);
    }

    @Override
    protected final Node<T> advance(final Set<Node<T>> currGen) throws StackOverflowError, InterruptedException {
        // Check for waiting state
        this.assertWaiting();
        // Check for running state
        if (!this.running)
            throw new InterruptedException("Invokation interrupted...");
        // Visit nodes
        Dijkstra.visit(currGen);
        // Initialize new empty generation
        final Set<Node<T>> newGen = new HashSet<Node<T>>();
        // Range through current generaton nodes cell neighbors
        for (final Node<T> node : currGen)
            for (final T cell : node.getOuter().getNeighbors()) {
                // Set new node
                if (cell.getInner() == null)
                    cell.setInner(new Node<T>(node, cell));
                // Check state
                switch (cell.getState()) {
                    case EMPTY:
                        // Visit node
                        if (cell.getInner().getState() != Node.NodeState.VISITED)
                            newGen.add(cell.getInner());
                        break;
                    case END:
                        // End reached
                        Dijkstra.visit(newGen);
                        return cell.getInner();
                    default:
                }
            }
        if (newGen.size() == 0)
            throw new StackOverflowError("No solution...");
        // Delay iteration
        Thread.sleep(this.delay);
        // Call method recursively until convergence
        return this.advance(newGen);
    }

    @Override
    public AlgorithmParameterSpec getParameterSpec() {
        return null;
    }

}
