package app.maze.components.cell.view;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import app.maze.components.cell.State;
import app.maze.components.cell.composite.CellComposite;
import app.maze.controller.MazeController;
import app.maze.controller.components.process.manager.ProcessManager;
import app.maze.view.MazeView;
import utils.JWrapper;

public final class CellView extends JPanel {

    private static final long serialVersionUID = 1L;

    private transient static CellView selected = null;

    private transient static CellView focused = null;

    @SuppressWarnings("unchecked")
    public final Consumer<Color> update = (Consumer<Color> & Serializable) color ->
            setBorder(BorderFactory.createLineBorder(color));

    {
        // Set default background color
        update.accept(State.WALKABLE.getColor());
        addMouseListener(new SubjectListener());
    }

    public CellView(final MazeController mzController, final CellComposite clComposite) {
        setController(mzController);
        setComposite(clComposite);
    }

    public CellView() {
        this(null, null);
    }

    public final void walk(final boolean walk) {
        final Consumer<Color> update = this.update.andThen(this::setBackground);
        try {
            final ProcessManager manager = mzController.getManager();
            // Assert running algorithm
            manager.assertRunning();
            // Update walkable state
            clComposite.setWalkable(walk);
            // Update background color
            if (walk)
                update.accept(State.WALKABLE.getColor());
            else
                update.accept(State.UNWALKABLE.getColor());
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    public synchronized static void select(final CellView selected) {
        // Focus cell
        focus(selected);
        // Update selected cell
        CellView.selected = selected;
    }

    public synchronized static final void focus(final CellView focused) {
        // Ignore if selected
        if (selected != null)
            return;
        // Unfocus cell
        if (CellView.focused != null)
            CellView.focused.update.accept(State.WALKABLE.getColor());
        // Focus cell
        if (focused != null)
            if (focused.getBackground() == State.WALKABLE.getColor())
                focused.update.accept(State.UNWALKABLE.getColor());
            else
                focused.update.accept(focused.getBackground());
        // Update focused state
        CellView.focused = focused;
    }

    public static CellView getSelected() {
        return selected;
    }

    public static final CellView getFocused() {
        return focused;
    }

    public transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    public CellComposite clComposite;

    public final CellComposite getComposite() {
        return clComposite;
    }

    public final void setComposite(final CellComposite clComposite) {
        this.clComposite = clComposite;
    }

    private final class SubjectListener extends MouseAdapter implements Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public final void mousePressed(final MouseEvent e) {
            // Clear node parent relationships
            mzController.clear();
            try {
                final ProcessManager manager = mzController.getManager();
                if (e.isShiftDown()) {
                    // Assert running algorithm
                    manager.assertRunning();
                    // Set node walkable state
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                        walk(false);
                    // Set node unwalkable state
                    else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                        walk(true);
                } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                    final MazeView mzView = mzController.getView();
                    // Assert running algorithm
                    manager.assertRunning();
                    // Release endpoint popup
                    mzView.releasePopup(CellView.this).show(CellView.this, e.getX(), e.getY());
                }
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        }

        @Override
        public synchronized final void mouseEntered(final MouseEvent e) {
            // Focus cell
            focus(CellView.this);
            if (e.isShiftDown())
                // Set node walkable state
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                    walk(false);
                // Set node unwalkable state
                else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                    walk(true);
        }

        @Override
        public synchronized final void mouseExited(final MouseEvent e) {
            // Unfocus cell
            focus(null);
            // Ignore if selected
            if (selected != null)
                return;
            // Reset border
            update.accept(State.WALKABLE.getColor());
        }

    };

}
