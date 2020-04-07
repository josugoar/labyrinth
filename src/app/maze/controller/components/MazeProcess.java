package app.maze.controller.components;

import java.io.Serializable;
import java.util.Objects;

import app.maze.components.algorithm.generator.Generator;
import app.maze.components.algorithm.generator.traversers.BackTracker;
import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.algorithm.pathfinder.traversers.Dijkstra;
import utils.AlgorithmManager;

public class MazeProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    private PathFinder pathFinder = new Dijkstra();

    private Generator generator = new BackTracker();

    public final void interrupt() {
        return;
    }

    public final void awake(final Class<? extends AlgorithmManager> algorithm) {
        if (algorithm.isInstance(PathFinder.class))
            return;
        else if (algorithm.isInstance(Generator.class))
            return;
    }

    public final boolean assertRunning() throws InterruptedException {
        return false;
    }

    public final void setAlgorithm(final AlgorithmManager algorithm) {
        Objects.requireNonNull(pathFinder, "AlgorithmManager must not be null...");
        if (algorithm instanceof PathFinder)
            this.pathFinder = (PathFinder) algorithm;
        else if (algorithm instanceof Generator)
            this.generator = (Generator) algorithm;
    }

    public final boolean isWaiting() {
        return false;
    }

    public final void setWaiting(final boolean waiting) {

    }

    public final void setDelay(final int delay) {
        this.pathFinder.setDelay(delay);
        this.generator.setDelay(delay);
    }

    public final void setDensity(final int density) {
        this.generator.setDensity(density);
    }

}
