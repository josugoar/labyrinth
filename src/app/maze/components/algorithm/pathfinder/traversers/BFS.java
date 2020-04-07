package app.maze.components.algorithm.pathfinder.traversers;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Set;

import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.cell.observer.CellObserver;

public class BFS extends PathFinder {

    private static final long serialVersionUID = 1L;

    @Override
    protected CellObserver advance(Set<CellObserver> currGen) throws StackOverflowError, InterruptedException {
        return null;
    }

    @Override
    public AlgorithmParameterSpec getParameterSpec() {
        return null;
    }

}
