package app.maze.components.algorithm.pathfinder.traversers;

import java.security.spec.AlgorithmParameterSpec;
import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.MutableTreeNode;

import app.maze.components.algorithm.TraverserListener.TraverserEvent;
import app.maze.components.algorithm.pathfinder.PathFinder;

public class Dijkstra extends PathFinder {

    private static final long serialVersionUID = 1L;

    @Override
    protected final MutableTreeNode advance(final Set<MutableTreeNode> currGen) throws StackOverflowError, InterruptedException {
        // Check for waiting state
        assertWaiting();
        // Check for running state
        if (!running)
            throw new InterruptedException("Invokation interrupted...");
        // Fire visited PathFinderEvent
        fireNodeVisited(new TraverserEvent(this, currGen.toArray(new MutableTreeNode[0])));
        // Initialize new empty generation
        final Set<MutableTreeNode> newGen = new HashSet<MutableTreeNode>(0);
        // Range through current generaton nodes cell neighbors
        for (final MutableTreeNode node : currGen)
            for (int i = 0; i < node.getChildCount(); i++) {
                final MutableTreeNode child = (MutableTreeNode) node.getChildAt(i);
                // Check endpoint
                if (child.equals(target)) {
                    child.setParent(node);
                    // Fire reached PathFinderEvent
                    fireNodeReached(new TraverserEvent(this, newGen.toArray(new MutableTreeNode[0])));
                    return child;
                }
                // Check visited
                if (!visited.contains(child)) {
                    newGen.add(child);
                    child.setParent(node);
                    visited.add(child);
                    // Fire germinated PathFinderEvent
                    fireNodeGerminated(new TraverserEvent(this, child));
                }
                if (child.equals(root))
                    child.setParent(null);
            }
        if (newGen.size() == 0)
            throw new StackOverflowError("No solution...");
        // Delay iteration
        Thread.sleep(delay);
        // Call method recursively until convergence
        return advance(newGen);
    }

    @Override
    public final AlgorithmParameterSpec getParameterSpec() {
        return null;
    }

}
