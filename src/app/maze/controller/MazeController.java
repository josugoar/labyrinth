package app.maze.controller;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import app.maze.components.cell.State;
import app.maze.components.cell.composite.CellComposite;
import app.maze.components.cell.view.CellView;
import app.maze.controller.components.panel.flyweight.PanelFlyweight;
import app.maze.controller.components.process.manager.ProcessManager;
import app.maze.model.MazeModel;
import app.maze.view.MazeView;
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
        // Reset structure
        reset();
        // Resize panel
        flyweight.resetDimension(dimension, dimension);
    }

    public final void reset() {
        // Interrupt running algorithm
        manager.interrupt();
        // Reset endpoints
        mzModel.reset();
        // Reset panel
        flyweight.reset();
    }

    public final void collapse() {
        final JTree tree = mzView.getTree();
        final Object oldRoot = mzModel.getRoot();
        // Get expanded descendants
        final Enumeration<TreePath> expanded = tree.getExpandedDescendants(oldRoot == null
                ? null
                : new TreePath(oldRoot));
        // Delete model
        tree.setModel(null);
        // Reset model
        if (oldRoot == null)
            tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("No root node...")));
        else
            tree.setModel(mzModel);
        // Ignore if no expanded descendants
        if (expanded == null)
            return;
        // Expand previous expanded descendants
        for (final Enumeration<TreePath> e = expanded; e.hasMoreElements();)
            tree.expandPath(e.nextElement());
    }

    public final void clear() {
        try {
            // Assert running algorithm
            manager.assertRunning();
            // Remove node parent relationships
            mzModel.clear();
            // Unselect cell
            CellView.select(null);
        } catch (final InterruptedException | NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void expand() {
        final JTree tree = mzView.getTree();
        // Delete model
        tree.setModel(null);
        // Reset model
        tree.setModel(mzModel);
        // Initialize empty path
        List<Object> path = new ArrayList<Object>(0);
        // Range through parent
        for (TreeNode parent = ((TreeNode) mzModel.getTarget())
                .getParent(); parent != null; parent = ((TreeNode) parent).getParent())
            // Add parent to path
            path.add(parent);
        // Reverse path
        Collections.reverse(path);
        // Expand path
        tree.expandPath(new TreePath(path.toArray()));
    }

    public final void dispatchKey(final KeyEvent e) {
        if (e.isShiftDown())
            try {
                // Assert running algorithm
                manager.assertRunning();
                // Change cursor state
                mzView.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            // Play/Pause algorithm
            manager.await();
    }

    public final void dispatchCell(final DefaultTreeCellRenderer renderer, final Object node) {
        // Assert node
        if (node instanceof CellComposite) {
            final Color color = ((Component) flyweight.request(node)).getBackground();
            // Empty node
            String file = "emptyIcon.gif";
            // Root node
            if (color == State.ROOT.getColor())
                file = "startIcon.gif";
            // Target node
            else if (color == State.TARGET.getColor())
                file = "endIcon.gif";
            // Empty node
            else if (color == State.GERMINATED.getColor())
                file = "germinatedIcon.gif";
            else if (color == State.VISITED.getColor())
                file = "visitedIcon.gif";
            else if (color == State.PATH.getColor())
                file = "pathIcon.gif";
            renderer.setIcon(new ImageIcon(MazeView.class.getResource("assets/" + file)));
        }
    }

    // TODO: Update slider on dimension change

    public final void readMaze(final String path) {
        try {
            final FileInputStream file = new FileInputStream(MazeModel.class.getResource(path).getPath());
            final ObjectInputStream in = new ObjectInputStream(file);
            // Read serialized object
            final PanelFlyweight otherFlyweight = (PanelFlyweight) in.readObject();
            final TreeNode otherRoot = (TreeNode) in.readObject();
            final TreeNode otherTarget = (TreeNode) in.readObject();
            in.close();
            file.close();
            // Interrupt algorithm
            manager.interrupt();
            // Override panel
            flyweight.override(otherFlyweight);
            // Reset endpoints
            mzModel.reset();
            // Override endpoints
            mzModel.setRoot(otherRoot);
            mzModel.setTarget(otherTarget);
        } catch (final IOException | ClassNotFoundException e) {
            JWrapper.dispatchException(e);
        }
    }

    // TODO: Fix large serializiation

    public final void writeMaze(final String path) {
        try {
            // Assert running algorithm
            manager.assertRunning();
            final FileOutputStream file = new FileOutputStream(MazeModel.class.getResource(path).getPath());
            final ObjectOutputStream out = new ObjectOutputStream(file);
            // Unselect cell
            CellView.select(null);
            // Remove node parent relationships
            mzModel.clear();
            // Serialize object
            out.writeObject(flyweight);
            out.writeObject(mzModel.getRoot());
            out.writeObject(mzModel.getTarget());
            out.close();
            file.close();
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
