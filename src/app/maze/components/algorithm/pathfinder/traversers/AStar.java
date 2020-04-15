package app.maze.components.algorithm.pathfinder.traversers;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Set;

import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.algorithm.pathfinder.heuristics.Heuristic;
import app.maze.components.cell.composite.CellComposite;

public class AStar extends PathFinder {

    private static final long serialVersionUID = 1L;

    private final Heuristic heuristic = new Heuristic.EuclideanDistance();

    @Override
    protected CellComposite advance(Set<CellComposite> currGen) throws StackOverflowError, InterruptedException {
        return null;
    }

    @Override
    public AlgorithmParameterSpec getParameterSpec() {
        return this.heuristic;
    }

}
