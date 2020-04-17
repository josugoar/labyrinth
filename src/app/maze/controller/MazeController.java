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

public final class MazeController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PanelFlyweight flyweight;

    private final ProcessManager manager;

    {
        flyweight = new PanelFlyweight(this);
        manager = new ProcessManager(this);
    }

    public MazeController(final MazeModel mzModel, final MazeView mzView) {
        setModel(mzModel);
        setView(mzView);
    }

    public MazeController() {
        this(null, null);
    }

    public final void resize(final int dimension) {
        // Reset MazeController structure
        reset();
        // Resize PanelFlyweight
        flyweight.resetDimension(dimension, dimension);
    }

    public final void reset() {
        // Interrupt running AlgorithmManager
        manager.interrupt();
        // Hard reset MazeModel endpoints
        mzModel.reset();
        // Reset PanelFlyweights
        flyweight.reset();
    }

    public final void expand() {
        final JTree tree = mzView.getTree();
        // Put JTree TreeModel
        TreeFactory.putTreeModel(tree, mzModel);
        // Initialize empty TreeNode path
        List<TreeNode> path = new ArrayList<TreeNode>(0);
        // Range through TreeNode parent
        for (TreeNode parent = ((TreeNode) mzModel.getTarget()).getParent(); parent != null; parent = parent.getParent())
            // Add parent to TreeNode path
            path.add(parent);
        // Reverse TreeNode path
        Collections.reverse(path);
        // Expand TreePath
        tree.expandPath(new TreePath(path.toArray()));
    }

    public final void collapse() {
        final JTree tree = mzView.getTree();
        final Object oldRoot = mzModel.getRoot();
        // Get expanded JTree descendants
        final Enumeration<TreePath> expanded = tree.getExpandedDescendants(oldRoot == null ? null : new TreePath(oldRoot));
        // Put JTree TreeModel
        TreeFactory.putTreeModel(tree, oldRoot == null ? TreeFactory.createTreeModel() : mzModel);
        // Ignore if no expanded JTree descendants
        if (expanded == null)
            return;
        // Expand previous expanded JTree descendants
        for (final Enumeration<TreePath> e = expanded; e.hasMoreElements();)
            tree.expandPath(e.nextElement());
    }

    public final void clear() {
        try {
            // Assert running AlgorithmManager
            manager.assertRunning();
            // Remove CellComposite parent relationships
            mzModel.clear();
            // Unselect CellView
            CellView.select(null);
        } catch (final InterruptedException | NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void dispatchKey(final KeyEvent e) {
        if (e.isShiftDown())
            try {
                // Assert running AlgorithmManager
                manager.assertRunning();
                // Change Cursor state
                mzView.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            // Set AlgorithmManager waiting state
            manager.await();
        // Fill Walkable
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            reset();
            for (final CellComposite node : flyweight.getReferences())
                node.setWalkable(false);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            reset();
    }

    public final void dispatchCell(final DefaultTreeCellRenderer renderer, final Object node) {
        // Ignore if no CellComposite
        if (!(node instanceof CellComposite))
            return;
        // Initialize empty file name
        String fileName = null;
        // Switch on State
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
        // Ignore if no WidgetFactory file name
        if (fileName == null)
            return;
        renderer.setIcon(WidgetFactory.createIcon(fileName));
    }

    // TODO: Update slider on dimension change

    public final void readMaze(final String path) {
        try {
            // Open Stream
            final FileInputStream file = new FileInputStream(path);
            final ObjectInputStream in = new ObjectInputStream(file);
            // Read serialized object
            final PanelFlyweight otherFlyweight = (PanelFlyweight) in.readObject();
            final TreeNode otherRoot = (TreeNode) in.readObject();
            final TreeNode otherTarget = (TreeNode) in.readObject();
            // Close Stream
            in.close();
            file.close();
            // Interrupt AlgorithmManager
            manager.interrupt();
            // Override PanelFlyweight
            flyweight.override(otherFlyweight);
            // Hard reset MazeModel endpoints to prevent overlapping
            mzModel.reset();
            // Override MazeModel endpoints
            mzModel.setRoot(otherRoot);
            mzModel.setTarget(otherTarget);
        } catch (final IOException | ClassNotFoundException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void writeMaze(final String path) {
        try {
            // Assert running AlgorithmManager
            manager.assertRunning();
            // Open Stream
            final FileOutputStream file = new FileOutputStream(path);
            final ObjectOutputStream out = new ObjectOutputStream(file);
            final CellComposite root = (CellComposite) mzModel.getRoot();
            // Unselect cell
            CellView.select(null);
            // Remove node parent relationships
            mzModel.clear();
            // Remove MazeModel endpoint relationships to enable graph serialization
            if (root != null)
                root.override();
            // Serialize object
            out.writeObject(flyweight);
            out.writeObject(root);
            out.writeObject(mzModel.getTarget());
            // Close Stream
            out.close();
            file.close();
            // Reset CellComposite relationships
            if (root != null)
                mzModel.initNeighbors(root);
        } catch (final InterruptedException | IOException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final PanelFlyweight getFlyweight() {
        return flyweight;
    }

    public final ProcessManager getManager() {
        return manager;
    }

    private MazeModel mzModel;

    public final MazeModel getModel() {
        return mzModel;
    }

    public final void setModel(final MazeModel mzModel) {
        this.mzModel = mzModel;
    }

    private MazeView mzView;

    public final MazeView getView() {
        return mzView;
    }

    public final void setView(final MazeView mzView) {
        this.mzView = mzView;
    }

}
