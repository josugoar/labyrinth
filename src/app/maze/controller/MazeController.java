package app.maze.controller;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import app.maze.components.cell.observer.CellObserver;
import app.maze.components.cell.subject.CellSubject;
import app.maze.controller.components.MazeProcess;
import app.maze.model.MazeModel;
import app.maze.model.components.flyweight.MazeFlyweight;
import app.maze.view.MazeView;
import utils.JWrapper;

public final class MazeController implements Serializable {

    private static final long serialVersionUID = 1L;

    public final MazeFlyweight mzFlyweight;

    public final MazeProcess mzProcess;

    {
        this.mzFlyweight = new MazeFlyweight(this);
        this.mzProcess = new MazeProcess();
    }

    public MazeController(final MazeModel mzModel, final MazeView mzView) {
        this.setModel(mzModel);
        this.setView(mzView);
    }

    public MazeController() {
        this(null, null);
    }

    public final void reset() {
        // Interrupt running algorithm
        this.mzProcess.interrupt();
        // Reset endpoints
        this.mzModel.reset();
        // Override panel
        this.mzFlyweight.override();
    }

    public final void clear() {
        try {
            // Assert running algorithm
            this.mzProcess.assertRunning();
            // Remove node parent relationships
            this.mzModel.clear();
        } catch (final InterruptedException | NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void collapse() {
        final JTree tree = this.mzView.getTree();
        // Get expanded rows
        final Enumeration<TreePath> expanded = tree.getExpandedDescendants(this.mzModel.getRoot() == null ? null : new TreePath(this.mzModel.getRoot()));
        // Get old root
        final CellObserver oldRoot = (CellObserver) this.mzModel.getRoot();
        // Delete model
        tree.setModel(null);
        // Reset model
        if (oldRoot == null)
            tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("No root node...")));
        else
            tree.setModel(this.mzModel);
        // Expand previous selected rows
        if (expanded == null)
            return;
        for (final Enumeration<TreePath> e = expanded; e.hasMoreElements();)
            tree.expandPath(e.nextElement());
    }

    public final void dispatchKey(final KeyEvent e) {
        Objects.requireNonNull(e, "KeyEvent must not be null...");
        if (e.isShiftDown())
            try {
                // Assert running algorithm
                this.mzProcess.assertRunning();
                // Change cursor state
                this.mzView.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            // Play/Pause algorithm
            this.mzProcess.setWaiting(!this.mzProcess.isWaiting());
    }

    public final void dispatchCell(final DefaultTreeCellRenderer renderer, final Object node) {
        Objects.requireNonNull(renderer, "DefaultTreeCellRenderer must not be null...");
        // Assert node
        if (Objects.requireNonNull(node, "Object must not be null...") instanceof CellObserver)
            // Root node
            if (((CellObserver) node).equals(this.mzModel.getRoot()))
                renderer.setIcon(new ImageIcon(MazeView.class.getResource("assets/startIcon.gif")));
            // Target node
            else if (((CellObserver) node).equals(this.mzModel.getTarget()))
                renderer.setIcon(new ImageIcon(MazeView.class.getResource("assets/endIcon.gif")));
            // Empty node
            else
                renderer.setIcon(new ImageIcon(MazeView.class.getResource("assets/emptyIcon.gif")));
    }

    public final void readMaze() {
        try {
            final FileInputStream file = new FileInputStream(MazeModel.class.getResource("components/ser/maze.ser").getPath());
            final ObjectInputStream in = new ObjectInputStream(file);
            // Read serialized file
            final MazeModel other = (MazeModel) in.readObject();
            in.close();
            file.close();
            // Interrupt algorithm
            this.mzProcess.interrupt();
            // Override serialized class
            this.mzModel = other;
            // Override panel
            this.mzFlyweight.override();
        } catch (final IOException | ClassNotFoundException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void writeMaze() {
        try {
            // Assert running algorithm
            this.mzProcess.assertRunning();
            final FileOutputStream file = new FileOutputStream(MazeModel.class.getResource("components/ser/maze.ser").getPath());
            final ObjectOutputStream out = new ObjectOutputStream(file);
            CellSubject.select(null);
            // Remove node parent relationships
            this.mzModel.clear();
            // Serialize class
            out.writeObject(this.mzModel);
            out.close();
            file.close();
        } catch (final InterruptedException | IOException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void setDimension(final int dimension) {
        // Reset endpoints
        this.mzModel.reset();
        this.mzFlyweight.setDimension(dimension, dimension);
    }

    public final MazeFlyweight getFlyweight() {
        return this.mzFlyweight;
    }

    public final MazeProcess getProcess() {
        return this.mzProcess;
    }

    public MazeModel mzModel;

    public final MazeModel getModel() {
        return this.mzModel;
    }

    public final void setModel(final MazeModel mzModel) {
        this.mzModel = mzModel;
    }

    public MazeView mzView;

    public final MazeView getView() {
        return this.mzView;
    }

    public final void setView(final MazeView mzView) {
        this.mzView = mzView;
    }

}
