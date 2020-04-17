package app.maze.controller.components.process.manager;

import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import app.maze.components.algorithm.AlgorithmManager;
import app.maze.components.algorithm.Listenable;
import app.maze.components.algorithm.TraverserListener;
import app.maze.components.algorithm.generator.Generator;
import app.maze.components.algorithm.generator.traversers.Randomizer;
import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.algorithm.pathfinder.traversers.Dijkstra;
import app.maze.components.cell.State;
import app.maze.components.cell.composite.CellComposite;
import app.maze.components.cell.view.CellView;
import app.maze.controller.MazeController;
import app.maze.controller.components.panel.flyweight.PanelFlyweight;
import app.maze.model.MazeModel;
import utils.JWrapper;

public final class ProcessManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Set<AlgorithmManager> algorithms = new HashSet<AlgorithmManager>(0);

    {
        // Set default AlgorithmManager
        setAlgorithm(new Dijkstra(), PathFinder.class);
        setAlgorithm(new Randomizer(), Generator.class);
    }

    public ProcessManager(final MazeController mzController) {
        setController(mzController);
    }

    public ProcessManager() {
        this(null);
    }

    public final void interrupt() {
        for (final AlgorithmManager a : algorithms)
            // Interrupt AlgorithmManager running state
            a.setRunning(false);
    }

    public final void await() {
        for (final AlgorithmManager a : algorithms) {
            // Ignore if AlgorithmManager not running
            if (!a.isRunning())
                continue;
            // Set AlgorithmManager waiting state
            a.setWaiting(!a.isWaiting());
            // Collapse JTree
            mzController.collapse();
        }
    }

    public final void awake(final Class<? extends AlgorithmManager> clazz) {
        try {
            Objects.requireNonNull(clazz, "AlgorithmManager must not be null...");
            // Assert running AlgorithmManager
            assertRunning();
            final MazeModel mzModel = mzController.getModel();
            for (final AlgorithmManager a : algorithms) {
                // Ignore if AlgorithmManager not assignable from Class
                if (!clazz.isAssignableFrom(a.getClass()))
                    continue;
                // PathFinder AlgorithmManager
                if (clazz.equals(PathFinder.class)) {
                    // Clear node parent relationships
                    mzController.clear();
                    // Fire PathFinder
                    ((PathFinder) a).find((MutableTreeNode) mzModel.getRoot(), (MutableTreeNode) mzModel.getTarget());
                // Generator AlgorithmManager
                } else if (clazz.equals(Generator.class)) {
                    final PanelFlyweight flyweight = mzController.getFlyweight();
                    // Reset MazeController structure
                    mzController.reset();
                    final CellComposite[] reference = flyweight.getReferences();
                    // Select random CellComposite
                    final CellComposite root = reference[(int) (Math.random() * reference.length)];
                    // Set CellComposite parent relationships
                    mzModel.initNeighbors(root);
                    // Fire Generator
                    ((Generator) a).generate(root);
                    // Reset MazeModel endpoints
                    mzModel.reset();
                }
            }
        } catch (final NullPointerException | InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void assertRunning() throws InterruptedException {
        for (final AlgorithmManager a : algorithms)
            // Assert running AlgorithmManager
            a.assertRunning();
    }

    public final void setAlgorithm(final AlgorithmManager algorithm, final Class<? extends AlgorithmManager> clazz) {
        try {
            Objects.requireNonNull(algorithm, "AlgorithmManager must not be null...");
            Objects.requireNonNull(clazz, "Class must not be null...");
            if (!(clazz.equals(PathFinder.class)) && !(clazz.equals(Generator.class)))
                throw new InvalidAlgorithmParameterException("Class must be PathFinder or Generator...");
            if (!clazz.isAssignableFrom(algorithm.getClass()))
                throw new InvalidAlgorithmParameterException("AlgorithmManager must extend Class...");
            // Add ManagerListener if Listenable
            if (algorithm instanceof Listenable)
                ((Listenable) algorithm).addListener(new ManagerListener());
            // Remove repeated AlgorithmManager
            algorithms.removeIf(a -> clazz.isAssignableFrom(a.getClass()));
            // Add new AlgorithmManager
            algorithms.add(algorithm);
        } catch (final NullPointerException | InvalidAlgorithmParameterException e) {
                JWrapper.dispatchException(e);
        }
    }

    public final void setDelay(final int delay) {
        for (final AlgorithmManager a : algorithms)
            // Set AlgorithmManager delay
            a.setDelay(delay);
    }

    public final void setDensity(final int density) {
        for (final AlgorithmManager a : algorithms) {
            // Ignore if AlgorithmManager  not assignable from Generator
            if (!Generator.class.isAssignableFrom(a.getClass()))
                continue;
            ((Generator) a).setDensity(density);
        }
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    private final class ManagerListener implements TraverserListener {

        private static final long serialVersionUID = 1L;

        private final void restate(final CellComposite node, final State state) {
            final MazeModel mzModel = mzController.getModel();
            // Ignore if TreeModel root
            if (node.equals(mzModel.getRoot()))
                return;
            final CellView cell = node.getView();
            // Update CellView background
            cell.setState(state);
            // Ignore if unfocused CellView
            if (CellView.getFocused() == null || !CellView.getFocused().equals(cell))
                return;
            // Update Border color
            cell.recolor.accept(state);
        }

        private final void dispatchTraverser(final TraverserEvent e, final State state) {
            final TreeNode[] gen = e.getGeneration();
            // Update single TreeNode if no generation
            if (gen == null)
                restate((CellComposite) e.getNode(), state);
            // Update entire TreeNode generation
            else
                for (final TreeNode node : gen)
                    restate((CellComposite) node, state);
        }

        @Override
        public final void nodeGerminated(final TraverserEvent e) {
            dispatchTraverser(e, State.GERMINATED);
        }

        @Override
        public final void nodeVisited(final TraverserEvent e) {
            dispatchTraverser(e, State.VISITED);
        }

        @Override
        public final void nodeReached(final TraverserEvent e) {
            if (e.getGeneration() == null && e.getNode() == null) {
                final PanelFlyweight flyweight = mzController.getFlyweight();
                for (final CellView clView : flyweight.getComponents())
                    clView.setState(clView.getComposite().isWalkable() ? State.WALKABLE : State.UNWALKABLE);
            } else
                dispatchTraverser(e, State.VISITED);
        }

        @Override
        public final void nodeTraversed(final TraverserEvent e) {
            dispatchTraverser(e, State.PATH);
            // Ignore if TraverserEvent generation
            if (e.getGeneration() == null)
                return;
            // Expand JTree target path
            mzController.expand();
        }

    }

}
