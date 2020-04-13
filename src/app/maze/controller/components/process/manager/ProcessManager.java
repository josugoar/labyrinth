package app.maze.controller.components.process.manager;

import java.io.Serializable;
import java.util.Objects;

import javax.swing.tree.TreeNode;

import app.maze.components.algorithm.AlgorithmManager;
import app.maze.components.algorithm.generator.Generator;
import app.maze.components.algorithm.generator.traversers.BackTracker;
import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.algorithm.pathfinder.PathFinderListener;
import app.maze.components.algorithm.pathfinder.traversers.Dijkstra;
import app.maze.components.cell.State;
import app.maze.components.cell.observer.CellObserver;
import app.maze.controller.MazeController;
import utils.JWrapper;

public final class ProcessManager implements Serializable {

    // TODO: Expand path when nodeFound

    private static final long serialVersionUID = 1L;

    private PathFinder pathFinder;

    private Generator generator;

    private final PathFinderListener listener = this.new ManagerListener();

    {
        // Set default algorithms
        this.setAlgorithm(new Dijkstra());
        this.setAlgorithm(new BackTracker());
    }

    public ProcessManager(final MazeController mzController) {
        this.setController(mzController);
    }

    public ProcessManager() {
        this(null);
    }

    public final void interrupt() {
        // Interrupt running state
        if (pathFinder.isRunning())
            pathFinder.setRunning(false);
        else if (generator.isRunning())
            generator.setRunning(false);
    }

    public final void await() {
        // Collapse tree
        this.mzController.collapse();
        // Set waiting state
        if (pathFinder.isRunning())
            pathFinder.setWaiting(!pathFinder.isWaiting());
        else if (generator.isRunning())
            generator.setWaiting(!generator.isWaiting());
    }

    public final void awake(final Class<? extends AlgorithmManager> algorithm) {
        try {
            this.assertRunning();
            if (algorithm.equals(PathFinder.class)) {
                // Clear node parent relationships
                this.mzController.clear();
                pathFinder.find((CellObserver) this.mzController.getModel().getRoot(),
                        (CellObserver) this.mzController.getModel().getTarget());
            } else if (algorithm.equals(Generator.class)) {
                // Reset structure
                this.mzController.reset();
                generator.awake(null);
            }
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
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

    private final class ManagerListener implements PathFinderListener, Serializable {

        private static final long serialVersionUID = 1L;

        private final void update(final CellObserver node, final State state) {
            // Ignore if root
            if (node.equals(ProcessManager.this.mzController.getModel().getRoot()))
                return;
            // Node updated
            ProcessManager.this.mzController.getFlyweight().request(node).setBackground(state.getColor());
        }

        @Override
        public void nodeGerminated(final TreeNode node) {
            this.update((CellObserver) node, State.GERMINATED);
        }

        @Override
        public void nodeVisited(final TreeNode node) {
            this.update((CellObserver) node, State.VISITED);
        }

        @Override
        public void nodeFound(final TreeNode node) {
            this.update((CellObserver) node, State.VISITED);
            ProcessManager.this.mzController.collapse();
        }

        @Override
        public void nodeTraversed(final TreeNode node) {
            this.update((CellObserver) node, State.PATH);
        }

    }

}
