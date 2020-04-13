package app.maze.controller.components;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;

import javax.swing.tree.TreeNode;

import app.maze.components.algorithm.AlgorithmManager;
import app.maze.components.algorithm.generator.Generator;
import app.maze.components.algorithm.generator.traversers.BackTracker;
import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.algorithm.pathfinder.PathFinderListener;
import app.maze.components.algorithm.pathfinder.traversers.Dijkstra;
import app.maze.components.cell.observer.CellObserver;
import app.maze.controller.MazeController;

public final class MazeProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PathFinderListener listener = new PathFinderListener() {
        @Override
        public void nodeVisited(final TreeNode node) {
            if (node.equals(MazeProcess.this.mzController.getModel().getRoot()))
                return;
            MazeProcess.this.mzController.getFlyweight().request((CellObserver) node).setBackground(Color.BLUE);
        }
        @Override
        public void nodeGerminated(final TreeNode node) {
            if (node.equals(MazeProcess.this.mzController.getModel().getRoot()))
                return;
            MazeProcess.this.mzController.getFlyweight().request((CellObserver) node).setBackground(Color.CYAN);
        }
        @Override
        public void nodeFound(final TreeNode node) {
            MazeProcess.this.mzController.getFlyweight().request((CellObserver) node).setBackground(Color.CYAN);
            MazeProcess.this.mzController.collapse();
        }
        @Override
        public void nodeTraversed(final TreeNode node) {
            if (node.equals(MazeProcess.this.mzController.getModel().getRoot()))
                return;
            MazeProcess.this.mzController.getFlyweight().request((CellObserver) node).setBackground(Color.YELLOW);
        }
    };

    private PathFinder pathFinder = new Dijkstra() {
        private static final long serialVersionUID = 1L;
        {
            this.addListener(MazeProcess.this.listener);
        }
    };

    private Generator generator = new BackTracker();

    public MazeProcess(final MazeController mzController) {
        this.setController(mzController);
    }

    public MazeProcess() {
        this(null);
    }

    public final void interrupt() {
        if (pathFinder.isRunning())
            pathFinder.setRunning(false);
        else if (generator.isRunning())
            generator.setRunning(false);
    }

    public final void await() {
        if (pathFinder.isRunning())
            pathFinder.setWaiting(!pathFinder.isWaiting());
        else if (generator.isRunning())
            generator.setWaiting(!generator.isWaiting());
    }

    public final void awake(final Class<? extends AlgorithmManager> algorithm) {
        if (pathFinder.isRunning() || generator.isRunning())
            return;
        this.mzController.clear();
        if (algorithm.equals(PathFinder.class)) {
            pathFinder.find((CellObserver) this.mzController.getModel().getRoot(), (CellObserver) this.mzController.getModel().getTarget());
        } else if (algorithm.equals(Generator.class))
            // TODO: Fix
            generator.awake(null);
    }

    public final void assertRunning() throws InterruptedException {
        pathFinder.assertRunning();
        generator.assertRunning();
    }

    public final void setAlgorithm(final AlgorithmManager algorithm) {
        Objects.requireNonNull(algorithm, "AlgorithmManager must not be null...");
        if (algorithm instanceof PathFinder) {
            this.pathFinder = (PathFinder) algorithm;
            this.pathFinder.addListener(this.listener);
        } else if (algorithm instanceof Generator)
            this.generator = (Generator) algorithm;
    }

    public final void setDelay(final int delay) {
        this.pathFinder.setDelay(delay);
        this.generator.setDelay(delay);
    }

    public final void setDensity(final int density) {
        this.generator.setDensity(density);
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return this.mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

}
