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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
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

    private PanelFlyweight mzFlyweight;

    private final ProcessManager mzProcess;

    {
        this.mzFlyweight = new PanelFlyweight(this);
        this.mzProcess = new ProcessManager(this);
    }

    public MazeController(final MazeModel mzModel, final MazeView mzView) {
        this.setModel(mzModel);
        this.setView(mzView);
    }

    public MazeController() {
        this(null, null);
    }

    public final void resize(final int dimension) {
        // Reset structure
        this.reset();
        // Resize panel
        this.mzFlyweight.setDimension(dimension, dimension);
    }

    public final void reset() {
        // Interrupt running algorithm
        this.mzProcess.interrupt();
        // Reset endpoints
        this.mzModel.reset();
        // Reset panel
        this.mzFlyweight.reset();
    }

    public final void collapse() {
        final JTree tree = this.mzView.getTree();
        final Object oldRoot = this.mzModel.getRoot();
        // get expanded descendants
        final Enumeration<TreePath> expanded = tree.getExpandedDescendants(oldRoot == null ? null : new TreePath(oldRoot));
        // Delete model
        tree.setModel(null);
        // Reset model
        if (oldRoot == null)
            tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("No root node...")));
        else
            tree.setModel(this.mzModel);
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
            this.mzProcess.assertRunning();
            // Remove node parent relationships
            this.mzModel.clear();
            // Unselect cell
            CellSubject.select(null);
        } catch (final InterruptedException | NullPointerException e) {
            JWrapper.dispatchException(e);
        }
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
            this.mzProcess.await();
    }

    public final void dispatchCell(final DefaultTreeCellRenderer renderer, final Object node) {
        Objects.requireNonNull(renderer, "DefaultTreeCellRenderer must not be null...");
        // Assert node
        if (Objects.requireNonNull(node, "Object must not be null...") instanceof CellObserver) {
            final Color color = this.mzFlyweight.request((CellObserver) node).getBackground();
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
            renderer.setIcon(new ImageIcon(MazeView.class.getResource(String.format("assets/%s", file))));
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
            this.mzProcess.interrupt();
            // Override components and references
            this.mzFlyweight.reset();
            // TODO: MazeFlyweight.override(MazeFlyweight) Delete setReferences, not needed with override ???
            this.mzFlyweight.setReferences(Arrays.asList(otherFlyweight.getReferences()));
            this.mzFlyweight.removeAll();
            for (final Component component : otherFlyweight.getComponents()) {
                ((CellSubject) component).setController(this);
                ((CellSubject) component).getObserver().setController(this);
                this.mzFlyweight.add(component);
            }
            // Override endpoints
            this.mzModel.reset();
            this.mzModel.setRoot(otherRoot);
            this.mzModel.setTarget(otherTarget);
        } catch (final IOException | ClassNotFoundException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final void writeMaze(final String path) {
        try {
            // Assert running algorithm
            this.mzProcess.assertRunning();
            final FileOutputStream file = new FileOutputStream(MazeModel.class.getResource(path).getPath());
            final ObjectOutputStream out = new ObjectOutputStream(file);
            // Unselect cell
            CellSubject.select(null);
            // Remove node parent relationships
            this.mzModel.clear();
            // Serialize class
            out.writeObject(this.mzFlyweight);
            out.writeObject((CellObserver) this.mzModel.getRoot());
            out.writeObject((CellObserver) this.mzModel.getTarget());
            out.close();
            file.close();
        } catch (final InterruptedException | IOException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final PanelFlyweight getFlyweight() {
        return this.mzFlyweight;
    }

    public final ProcessManager getProcess() {
        return this.mzProcess;
    }

    private MazeModel mzModel;

    public final MazeModel getModel() {
        return this.mzModel;
    }

    public final void setModel(final MazeModel mzModel) {
        this.mzModel = mzModel;
    }

    private MazeView mzView;

    public final MazeView getView() {
        return this.mzView;
    }

    public final void setView(final MazeView mzView) {
        this.mzView = mzView;
    }

}
