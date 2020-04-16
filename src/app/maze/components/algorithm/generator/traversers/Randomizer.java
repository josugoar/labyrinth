package app.maze.components.algorithm.generator.traversers;

import java.security.spec.AlgorithmParameterSpec;

import app.maze.components.algorithm.generator.Generator;
import app.maze.components.cell.Walkable;

public class Randomizer extends Generator {

    private static final long serialVersionUID = 1L;

    @Override
    protected void advance(final Walkable node) throws InterruptedException {
        for (int i = 0; i < node.getChildCount(); i++) {
            final Walkable child = (Walkable) node.getChildAt(i);
            if (!visited.contains(child)) {
                visited.add(child);
                advance(child);
                if (Math.random() < 0.5)
                    child.setWalkable(false);
                else
                    child.setWalkable(true);
            }
        }
    }

    @Override
    public AlgorithmParameterSpec getParameterSpec() {
        return null;
    }

}
