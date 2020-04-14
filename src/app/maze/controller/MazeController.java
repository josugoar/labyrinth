package app.maze.controller;

import java.awt.Color;
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
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import app.maze.components.cell.State;
import app.maze.components.cell.observer.CellObserver;
import app.maze.components.cell.subject.CellSubject;
import app.maze.controller.components.panel.flyweight.PanelFlyweight;
import app.maze.controller.components.process.manager.ProcessManager;
import app.maze.model.MazeModel;
import app.maze.view.MazeView;
import utils.JWrapper;

public final class MazeController implements Serializable {

    private static final long serialVersionUID = 1L;

    private PanelFlyweight flyweight;

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
        flyweight.setDimension(dimension, dimension);
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
        // Validate change
        tree.revalidate();
        tree.repaint();
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
            CellSubject.select(null);
        } catch (final InterruptedException | NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    // TODO: Fix collapse path first
    public final void expand() {
        final JTree tree = mzView.getTree();
        tree.expandPath(new TreePath(new Object[] { mzModel.getRoot() }));
        List<Object> path = new ArrayList<Object>(0);
        for (TreeNode parent = (TreeNode) mzModel.getTarget(); parent != null; parent = ((TreeNode) parent).getParent()) {
            path.add(parent);
            }
        Collections.reverse(path);
        tree.expandPath(new TreePath(path.toArray()));
    }

    public final void dispatchKey(final KeyEvent e) {
        Objects.requireNonNull(e, "KeyEvent must not be null...");
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
        Objects.requireNonNull(renderer, "DefaultTreeCellRenderer must not be null...");
        // Assert node
        if (Objects.requireNonNull(node, "Object must not be null...") instanceof CellObserver) {
            final Color color = this.flyweight.request((CellObserver) node).getBackground();
            String file = "emptyIcon.gif";
            // Root node
            if (color == State.ROOT.getColor())
                file = "startIcon.gif";
            // Target node
            else if (color == State.TARGET.getColor())
                file = "endIcon.gif";
            // Empty node
            else if (color == State.GERMINATED.getColor())
                // TODO: Convert to .gif
                file = "germinatedIcon.png";
            else if (color == State.VISITED.getColor())
                file = "visitedIcon.gif";
            else if (color == State.PATH.getColor())
                file = "pathIcon.gif";
            renderer.setIcon(new ImageIcon(MazeView.class.getResource("assets/" + file)));
        }
    }

    public final void readMaze(final String path) {
        try {
            final FileInputStream file = new FileInputStream(MazeModel.class.getResource(path).getPath());
            final ObjectInputStream in = new ObjectInputStream(file);
            // Read serialized object
            final PanelFlyweight otherFlyweight = (PanelFlyweight) in.readObject();
            final CellObserver otherRoot = (CellObserver) in.readObject();
            final CellObserver otherTarget = (CellObserver) in.readObject();
            in.close();
            file.close();
            // Interrupt algorithm
            manager.interrupt();
            // Override panel
            flyweight.override(otherFlyweight);
            // Override endpoints
            mzModel.reset();
            mzModel.setRoot(otherRoot);
            mzModel.setTarget(otherTarget);
        } catch (final IOException | ClassNotFoundException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void writeMaze(final String path) {
        try {
            // Assert running algorithm
            manager.assertRunning();
            final FileOutputStream file = new FileOutputStream(MazeModel.class.getResource(path).getPath());
            final ObjectOutputStream out = new ObjectOutputStream(file);
            // Unselect cell
            CellSubject.select(null);
            // Remove node parent relationships
            mzModel.clear();
            // Serialize class
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
