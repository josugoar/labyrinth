package app.maze.components.algorithm.generator.traversers;

import java.security.spec.AlgorithmParameterSpec;

import app.maze.components.algorithm.TraverserListener.TraverserEvent;
import app.maze.components.algorithm.generator.Generator;
import app.maze.components.cell.Walkable;

public class Randomizer extends Generator {

    private static final long serialVersionUID = 1L;

    @Override
    protected void advance(final Walkable node) throws InterruptedException {
        // Fire visited TraverserEvent
        fireNodeVisited(new TraverserEvent(this, node));
        // Range through TreeNode children
        for (int i = 0; i < node.getChildCount(); i++) {
            final Walkable child = (Walkable) node.getChildAt(i);
            // Ignore if Walkable visited
            if (visited.contains(child))
                continue;
            // Visit Walkable
            visited.add(child);
            // Advance Walkable generation
            advance(child);
            // Delay iteration
            Thread.sleep(delay);
            // Fire traversed TraverserEvent
            fireNodeTraversed(new TraverserEvent(this, node));
            // Randomize Walkable
            if (Math.random() < (double) density / 100)
                child.setWalkable(false);
            else
                child.setWalkable(true);
        }
        // Fire germinated TraverserEvent
        fireNodeGerminated(new TraverserEvent(this, node));
    }

    @Override
    public AlgorithmParameterSpec getParameterSpec() {
        return null;
    }

}
