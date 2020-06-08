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

/**
 * Algorithmic process manager, implementing <code>java.io.Serializable</code>.
 *
 * @see java.io.Serializable Serializable
 */
public final class ProcessManager implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Process selection.
     */
    private final Set<AlgorithmManager> algorithms = new HashSet<AlgorithmManager>(0);

    {
        setAlgorithm(new Dijkstra(), PathFinder.class);
        setAlgorithm(new Randomizer(), Generator.class);
    }

    /**
     * Enclose MazeController.
     *
     * @param mzController MazeController
     */
    public ProcessManager(final MazeController mzController) {
        setController(mzController);
    }

    /**
     * Create new algorithmic process manager.
     */
    public ProcessManager() {
        this(null);
    }

    /**
     * Force interrupt running processes.
     */
    public final void interrupt() {
        for (final AlgorithmManager a : algorithms)
            a.setRunning(false);
    }

    /**
     * Force await running processes.
     */
    public final void await() {
        for (final AlgorithmManager a : algorithms) {
            if (!a.isRunning())
                continue;
            a.setWaiting(!a.isWaiting());
            mzController.collapse();
        }
    }

    /**
     * Run given process if no processes are already running and clear MazeController.
     *
     * @param clazz Class<? extends AlgorithmManager>
     */
    public final void awake(final Class<? extends AlgorithmManager> clazz) {
        try {
            Objects.requireNonNull(clazz, "AlgorithmManager must not be null...");
            assertRunning();
            final MazeModel mzModel = mzController.getModel();
            for (final AlgorithmManager a : algorithms) {
                if (!clazz.isAssignableFrom(a.getClass()))
                    continue;
                if (clazz.equals(PathFinder.class)) {
                    mzController.clear();
                    ((PathFinder) a).find((MutableTreeNode) mzModel.getRoot(), (MutableTreeNode) mzModel.getTarget());
                } else if (clazz.equals(Generator.class)) {
                    final PanelFlyweight flyweight = mzController.getFlyweight();
                    mzController.reset();
                    final CellComposite[] reference = flyweight.getReferences();
                    final CellComposite root = reference[(int) (Math.random() * reference.length)];
                    mzModel.initNeighbors(root);
                    ((Generator) a).generate(root);
                    mzModel.reset();
                }
            }
        } catch (final NullPointerException | InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Assert if process is currently running.
     *
     * @throws InterruptedException if (running)
     */
    public final void assertRunning() throws InterruptedException {
        for (final AlgorithmManager a : algorithms)
            a.assertRunning();
    }

    /**
     * Add process to process selection and overrite previous matching one.
     *
     * @param algorithm AlgorithmManager
     * @param clazz     Class<? extends AlgorithmManager>
     */
    public final void setAlgorithm(final AlgorithmManager algorithm, final Class<? extends AlgorithmManager> clazz) {
        try {
            Objects.requireNonNull(algorithm, "AlgorithmManager must not be null...");
            Objects.requireNonNull(clazz, "Class must not be null...");
            if (!(clazz.equals(PathFinder.class)) && !(clazz.equals(Generator.class)))
                throw new InvalidAlgorithmParameterException("Class must be PathFinder or Generator...");
            if (!clazz.isAssignableFrom(algorithm.getClass()))
                throw new InvalidAlgorithmParameterException("AlgorithmManager must extend Class...");
            if (algorithm instanceof Listenable)
                ((Listenable) algorithm).addListener(new ManagerListener());
            algorithms.removeIf(a -> clazz.isAssignableFrom(a.getClass()));
            algorithms.add(algorithm);
        } catch (final NullPointerException | InvalidAlgorithmParameterException e) {
                JWrapper.dispatchException(e);
        }
    }

    /**
     * Set process iteration delay.
     *
     * @param delay int
     */
    public final void setDelay(final int delay) {
        for (final AlgorithmManager a : algorithms)
            a.setDelay(delay);
    }

    /**
     * Set <code>app.maze.components.algorithm.generator.Generator</code> process density.
     *
     * @param density int
     */
    public final void setDensity(final int density) {
        for (final AlgorithmManager a : algorithms) {
            if (!Generator.class.isAssignableFrom(a.getClass()))
                continue;
            ((Generator) a).setDensity(density);
        }
    }

    /**
     * <code>app.maze.controller.MazeController</code> relationship.
     */
    private transient MazeController mzController;

    /**
     * Return current <code>app.maze.controller.MazeController</code> relationship.
     */
    public final MazeController getController() {
        return mzController;
    }

    /**
     * Set current <code>app.maze.controller.MazeController</code> relationship.
     */
    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    /**
     * Manager process listener, implementing
     * <code>app.maze.components.algorithm.TraverserListener</code>.
     *
     * @see app.maze.components.algorithm.TraverserListener TraverserListener
     */
    private final class ManagerListener implements TraverserListener {

        private static final long serialVersionUID = 1L;

        /**
         * Update <code>app.maze.components.cell.view.CellView</code> state depending on
         * its <code>app.maze.components.cell.composite.CellComposite</code>.
         *
         * @param node  CellComposite
         * @param state State
         */
        private final void restate(final CellComposite node, final State state) {
            final MazeModel mzModel = mzController.getModel();
            if (node.equals(mzModel.getRoot()))
                return;
            final CellView cell = node.getView();
            cell.setState(state);
            if (CellView.getFocused() == null || !CellView.getFocused().equals(cell))
                return;
            cell.recolor.accept(state);
        }

        /**
         * Update
         * <code>app.maze.components.algorithm.TraverserListener.TraverserEvent</code>
         * generation or individual state.
         *
         * @param e     TraverserEvent
         * @param state State
         */
        private final void dispatchTraverser(final TraverserEvent e, final State state) {
            final TreeNode[] gen = e.getGeneration();
            if (gen == null)
                restate((CellComposite) e.getNode(), state);
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
            if (e.getGeneration() == null)
                return;
            mzController.expand();
        }

    }

}
