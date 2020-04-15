package app.maze.components.algorithm.pathfinder.traversers;

import java.security.spec.AlgorithmParameterSpec;
import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.MutableTreeNode;

import app.maze.components.algorithm.pathfinder.PathFinder;

public class Dijkstra extends PathFinder {

    private static final long serialVersionUID = 1L;

    @Override
    protected final MutableTreeNode advance(final Set<MutableTreeNode> currGen) throws StackOverflowError, InterruptedException {
        // Check for waiting state
        this.assertWaiting();
        // Check for running state
        if (!this.running)
            throw new InterruptedException("Invokation interrupted...");
        // Germinate generation
        for (MutableTreeNode node : currGen)
            fireNodeVisited(node);
        // Initialize new empty generation
        final Set<MutableTreeNode> newGen = new HashSet<MutableTreeNode>(0);
        // Range through current generaton nodes cell neighbors
        for (final MutableTreeNode node : currGen)
            for (int i = 0; i < node.getChildCount(); i++) {
                final MutableTreeNode child = (MutableTreeNode) node.getChildAt(i);
                // Check endpoint
                if (child.equals(this.target)) {
                    child.setParent(node);
                    // Visit generation
                    // TODO: Fix
                    // for (MutableTreeNode leaf : newGen)
                        fireNodeFound(node);
                    return child;
                }
                // Check visited
                if (!this.visited.contains(child)) {
                    newGen.add(child);
                    child.setParent(node);
                    this.visited.add(child);
                    // Visit generation
                    fireNodeGerminated(child);
                }
                if (child.equals(this.start))
                    child.setParent(null);
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
