package app.maze.controller.components.process.manager;

import java.io.Serializable;
import java.util.Objects;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import app.maze.components.algorithm.AlgorithmManager;
import app.maze.components.algorithm.generator.Generator;
import app.maze.components.algorithm.generator.traversers.Randomizer;
import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.algorithm.pathfinder.PathFinderListener;
import app.maze.components.algorithm.pathfinder.traversers.Dijkstra;
import app.maze.components.cell.State;
import app.maze.components.cell.Walkable;
import app.maze.components.cell.composite.CellComposite;
import app.maze.components.cell.view.CellView;
import app.maze.controller.MazeController;
import app.maze.model.MazeModel;
import utils.JWrapper;

public final class ProcessManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private PathFinder pathFinder;

    private Generator generator;

    private final PathFinderListener listener = new ManagerListener();

    {
        // Set default AlgorithmManager
        setAlgorithm(new Dijkstra());
        setAlgorithm(new Randomizer());
    }

    public ProcessManager(final MazeController mzController) {
        setController(mzController);
    }

    public ProcessManager() {
        this(null);
    }

    public final void interrupt() {
        // Interrupt AlgorithmManager running state
        if (pathFinder.isRunning())
            pathFinder.setRunning(false);
        else if (generator.isRunning())
            generator.setRunning(false);
    }

    public final void await() {
        // Set AlgorithmManager waiting state
        if (pathFinder.isRunning()) {
            pathFinder.setWaiting(!pathFinder.isWaiting());
            // Collapse JTree
            mzController.collapse();
        } else if (generator.isRunning()) {
            generator.setWaiting(!generator.isWaiting());
            // Collapse JTree
            mzController.collapse();
        }
    }

    public final void awake(final Class<? extends AlgorithmManager> algorithm) {
        try {
            Objects.requireNonNull(algorithm, "AlgorithmManager must not be null...");
            // Assert running AlgorithmManager
            assertRunning();
            final MazeModel mzModel = mzController.getModel();
            if (algorithm.equals(PathFinder.class)) {
                // Clear node parent relationships
                mzController.clear();
                // Fire PathFinder
                pathFinder.find((MutableTreeNode) mzModel.getRoot(), (MutableTreeNode) mzModel.getTarget());
            } else if (algorithm.equals(Generator.class)) {
                // TODO: Refactor (add button to set all nodes (un)walkable)
                final CellComposite[] reference = mzController.getFlyweight().getReferences();
                final Walkable oldRoot = (Walkable) mzModel.getRoot();
                // // Reset structure
                // mzController.reset();
                // for (final CellComposite o : reference) {
                //     o.setWalkable(false);
                //     ((JComponent) mzController.getFlyweight().request(o)).setBackground(Color.BLACK);
                // }
                // Fire Generator
                generator.generate(oldRoot == null
                        ? reference[(int) (Math.random() * reference.length)]
                        : oldRoot);
                mzModel.setRoot(null);
            }
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void assertRunning() throws InterruptedException {
        // Assert running AlgorithmManager
        pathFinder.assertRunning();
        generator.assertRunning();
    }

    public final void setAlgorithm(final AlgorithmManager algorithm) {
        Objects.requireNonNull(algorithm, "AlgorithmManager must not be null...");
        // Update AlgorithmManager
        if (algorithm instanceof PathFinder) {
            pathFinder = (PathFinder) algorithm;
            // Add default PathFinderListener to PathFinder
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

        private final void update(final CellComposite node, final State state) {
            final MazeModel mzModel = mzController.getModel();
            // Ignore if TreeModel root
            if (node.equals(mzModel.getRoot()))
                return;
            final CellView cell = (CellView) mzController.getFlyweight().request(node);
            // Update CellView background
            cell.setBackground(state);
            // Ignore if unfocused CellView
            if (CellView.getFocused() == null || !CellView.getFocused().equals(cell))
                return;
            // Update Border color
            cell.update.accept(state);
        }

        private final void dispatchPathFinder(final PathFinderEvent e, final State state) {
            final TreeNode[] gen = e.getGeneration();
            // Update single TreeNode if no generation
            if (gen == null)
                update((CellComposite) e.getNode(), state);
            // Update entire TreeNode generation
            else
                for (final TreeNode node : gen)
                    update((CellComposite) node, state);
        }

        @Override
        public void nodeGerminated(final PathFinderEvent e) {
            dispatchPathFinder(e, State.GERMINATED);
        }

        @Override
        public void nodeVisited(final PathFinderEvent e) {
            dispatchPathFinder(e, State.VISITED);
        }

        @Override
        public void nodeFound(final PathFinderEvent e) {
            dispatchPathFinder(e, State.VISITED);
        }

        @Override
        public void nodeTraversed(final PathFinderEvent e) {
            dispatchPathFinder(e, State.PATH);
            // Expand JTree target path
            mzController.expand();
        }

    }

}
