package app.maze.controller.components.process.manager;

import java.io.Serializable;
import java.util.Objects;

import javax.swing.tree.MutableTreeNode;
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
import app.maze.controller.components.panel.flyweight.PanelFlyweight;
import app.maze.model.MazeModel;
import utils.JWrapper;

public final class ProcessManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private PathFinder pathFinder;

    private Generator generator;

    private final PathFinderListener listener = new ManagerListener();

    {
        // Set default algorithms
        setAlgorithm(new Dijkstra());
        setAlgorithm(new BackTracker());
    }

    public ProcessManager(final MazeController mzController) {
        setController(mzController);
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
        mzController.collapse();
        // Set waiting state
        if (pathFinder.isRunning())
            pathFinder.setWaiting(!pathFinder.isWaiting());
        else if (generator.isRunning())
            generator.setWaiting(!generator.isWaiting());
    }

    public final void awake(final Class<? extends AlgorithmManager> algorithm) {
        try {
            // Assert running algorithm
            assertRunning();
            if (algorithm.equals(PathFinder.class)) {
                final MazeModel mzModel = mzController.getModel();
                // Clear node parent relationships
                mzController.clear();
                pathFinder.find((MutableTreeNode) mzModel.getRoot(), (MutableTreeNode) mzModel.getTarget());
            } else if (algorithm.equals(Generator.class)) {
                // Reset structure
                mzController.reset();
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
            pathFinder = (PathFinder) algorithm;
            pathFinder.addListener(listener);
        } else if (algorithm instanceof Generator)
            generator = (Generator) algorithm;
    }

    public final void setDelay(final int delay) {
        pathFinder.setDelay(delay);
        generator.setDelay(delay);
    }

    public final void setDensity(final int density) {
        generator.setDensity(density);
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    private final class ManagerListener implements PathFinderListener, Serializable {

        private static final long serialVersionUID = 1L;

        private final void update(final CellObserver node, final State state) {
            final MazeModel mzModel = mzController.getModel();
            // Ignore if root
            if (node.equals(mzModel.getRoot()))
                return;
            final PanelFlyweight flyweight = mzController.getFlyweight();
            // Node updated
            flyweight.request(node).setBackground(state.getColor());
        }

        @Override
        public void nodeGerminated(final TreeNode node) {
            update((CellObserver) node, State.GERMINATED);
        }

        @Override
        public void nodeVisited(final TreeNode node) {
            update((CellObserver) node, State.VISITED);
        }

        @Override
        public void nodeFound(final TreeNode node) {
            update((CellObserver) node, State.VISITED);
            mzController.expand();
        }

        @Override
        public void nodeTraversed(final TreeNode node) {
            update((CellObserver) node, State.PATH);
        }

    }

}
