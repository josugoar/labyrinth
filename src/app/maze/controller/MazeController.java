package app.maze.controller;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import app.maze.components.cell.State;
import app.maze.components.cell.composite.CellComposite;
import app.maze.components.cell.view.CellView;
import app.maze.controller.components.panel.flyweight.PanelFlyweight;
import app.maze.controller.components.process.manager.ProcessManager;
import app.maze.model.MazeModel;
import app.maze.model.components.tree.factory.TreeFactory;
import app.maze.view.MazeView;
import app.maze.view.components.widget.factory.WidgetFactory;
import utils.JWrapper;

/**
 * Maze MVC Controller representation, implementing
 * <code>java.io.Serializable</code>.
 *
 * @see java.io.Serializable Serializable
 */
public final class MazeController implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <code>app.maze.controller.components.panel.flyweight.PanelFlyweight</code>
     * relationship.
     */
    private final PanelFlyweight flyweight;

    /**
     * <code>app.maze.controller.components.process.manager.ProcessManager</code>
     * relationship.
     */
    private final ProcessManager manager;

    {
        flyweight = new PanelFlyweight(this);
        manager = new ProcessManager(this);
    }

    /**
     * Enclose MazeModel and MazeView.
     *
     * @param mzModel MazeModel
     * @param mzView  MazeView
     */
    public MazeController(final MazeModel mzModel, final MazeView mzView) {
        setModel(mzModel);
        setView(mzView);
    }

    /**
     * Create empty Controller.
     */
    public MazeController() {
        this(null, null);
    }

    /**
     * Resize
     * <code>app.maze.controller.components.panel.flyweight.PanelFlyweight</code>
     * overriding previous size.
     *
     * @param dimension int
     */
    public final void resize(final int dimension) {
        reset();
        flyweight.resetDimension(dimension, dimension);
    }

    /**
     * Reset Controller relationships and processes.
     */
    public final void reset() {
        manager.interrupt();
        mzModel.reset();
        flyweight.reset();
    }

    /**
     * Expand <code>javax.swing.JTree</code> node view.
     */
    public final void expand() {
        final JTree tree = mzView.getTree();
        // IMPORTANT: Delete MazeModel reference and update it in order to collapse JTree
        TreeFactory.putTreeModel(tree, mzModel);
        final List<TreeNode> path = new ArrayList<TreeNode>(0);
        for (TreeNode parent = ((TreeNode) mzModel.getTarget()).getParent(); parent != null; parent = parent.getParent())
            path.add(parent);
        Collections.reverse(path);
        tree.expandPath(new TreePath(path.toArray()));
    }

    /**
     * Collapse <code>javax.swing.JTree</code> node view.
     */
    public final void collapse() {
        final JTree tree = mzView.getTree();
        final Object oldRoot = mzModel.getRoot();
        final Enumeration<TreePath> expanded = tree.getExpandedDescendants(oldRoot == null ? null : new TreePath(oldRoot));
        // IMPORTANT: Delete MazeModel reference and update it in order to collapse JTree
        TreeFactory.putTreeModel(tree, oldRoot == null ? TreeFactory.createTreeModel() : mzModel);
        if (expanded == null)
            return;
        for (final Enumeration<TreePath> e = expanded; e.hasMoreElements();)
            tree.expandPath(e.nextElement());
    }

    /**
     * Clear Controller state.
     */
    public final void clear() {
        try {
            manager.assertRunning();
            mzModel.clear();
            CellView.select(null);
        } catch (final InterruptedException | NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Dispatch <code>java.awt.event.KeyEvent</code>.
     *
     * @param e KeyEvent
     */
    public final void dispatchKey(final KeyEvent e) {
        if (e.isShiftDown())
            try {
                manager.assertRunning();
                mzView.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            manager.await();
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            reset();
            for (final CellComposite node : flyweight.getReferences())
                node.setWalkable(false);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            reset();
    }

    /**
     * Dispatch <code>javax.swing.tree.DefaultTreeCellRenderer</code> and assign
     * icon.
     *
     * @param renderer DefaultTreeCellRenderer
     * @param node     Object
     */
    public final void dispatchCell(final DefaultTreeCellRenderer renderer, final Object node) {
        if (!(node instanceof CellComposite))
            return;
        String fileName = null;
        switch (State.getState(((CellComposite) node).getView().getBackground())) {
            case WALKABLE:
                fileName = "walkableIcon.gif";
                break;
            case ROOT:
                fileName = "rootIcon.gif";
                break;
            case TARGET:
                fileName = "targetIcon.gif";
                break;
            case VISITED:
                fileName = "visitedIcon.gif";
                break;
            case GERMINATED:
                fileName = "germinatedIcon.gif";
                break;
            case PATH:
                fileName = "pathIcon.gif";
                break;
            default:
        }
        if (fileName == null)
            return;
        renderer.setIcon(WidgetFactory.createIcon(fileName));
    }

    // TODO: Update slider on dimension change

    /**
     * Read Maze representation from path and restore its state and processes.
     *
     * @param path String
     */
    public final void readMaze(final String path) {
        try {
            final FileInputStream file = new FileInputStream(path);
            final ObjectInputStream in = new ObjectInputStream(file);
            final PanelFlyweight otherFlyweight = (PanelFlyweight) in.readObject();
            final TreeNode otherRoot = (TreeNode) in.readObject();
            final TreeNode otherTarget = (TreeNode) in.readObject();
            in.close();
            file.close();
            manager.interrupt();
            flyweight.override(otherFlyweight);
            mzModel.reset();
            mzModel.setRoot(otherRoot);
            mzModel.setTarget(otherTarget);
        } catch (final IOException | ClassNotFoundException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Write current maze representation to path.
     *
     * @param path String
     */
    public final void writeMaze(final String path) {
        try {
            manager.assertRunning();
            final FileOutputStream file = new FileOutputStream(path);
            final ObjectOutputStream out = new ObjectOutputStream(file);
            final CellComposite root = (CellComposite) mzModel.getRoot();
            CellView.select(null);
            mzModel.clear();
            if (root != null)
                root.override();
            out.writeObject(flyweight);
            out.writeObject(root);
            out.writeObject(mzModel.getTarget());
            out.close();
            file.close();
            if (root != null)
                mzModel.initNeighbors(root);
        } catch (final InterruptedException | IOException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Return current
     * <code>app.maze.controller.components.panel.flyweight.PanelFlyweight</code>
     * relationship.
     *
     * @return PanelFlyweight
     */
    public final PanelFlyweight getFlyweight() {
        return flyweight;
    }

    /**
     * Return current
     * <code>app.maze.controller.components.process.manager.ProcessManager</code>
     * relationship.
     *
     * @return ProcessManager
     */
    public final ProcessManager getManager() {
        return manager;
    }

    /**
     * <code>app.maze.model.MazeModel</code> relationship.
     */
    private MazeModel mzModel;

    /**
     * Return current <code>app.maze.model.MazeModel</code> relationship.
     *
     * @return MazeModel
     */
    public final MazeModel getModel() {
        return mzModel;
    }

    /**
     * Set current <code>app.maze.model.MazeModel</code> relationship.
     *
     * @return MazeModel
     */
    public final void setModel(final MazeModel mzModel) {
        this.mzModel = mzModel;
    }

    /**
     * <code>app.maze.view.MazeView</code> relationship.
     */
    private MazeView mzView;

    /**
     * Return current
     * <code>app.maze.view.MazeView</code>
     * relationship.
     *
     * @return MazeView
     */
    public final MazeView getView() {
        return mzView;
    }

    /**
     * Set current
     * <code>app.maze.view.MazeView</code>
     * relationship.
     *
     * @return MazeView
     */
    public final void setView(final MazeView mzView) {
        this.mzView = mzView;
    }

}
