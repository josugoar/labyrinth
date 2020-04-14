package app.maze.components.cell.subject;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import app.maze.components.cell.State;
import app.maze.components.cell.observer.CellObserver;
import app.maze.controller.MazeController;
import app.maze.controller.components.process.manager.ProcessManager;
import app.maze.view.MazeView;
import utils.JWrapper;

// TODO: Change Observer pattern

public final class CellSubject extends JPanel {

    private static final long serialVersionUID = 1L;

    private transient static CellSubject selected = null;

    private transient static CellSubject focused = null;

    // TODO: Refactor
    @SuppressWarnings("unchecked")
    private final Consumer<Color> update = (Consumer<Color> & Serializable) color ->
            setBorder(BorderFactory.createLineBorder(color));

    {
        // Set default background
        update.accept(State.WALKABLE.getColor());
        addMouseListener(new SubjectListener());
    }

    public CellSubject(final MazeController mzController, final CellObserver clObserver) {
        setController(mzController);
        setObserver(clObserver);
    }

    public CellSubject() {
        this(null, null);
    }

    public final void walk(final boolean walk) {
        final Consumer<Color> update = this.update.andThen(this::setBackground);
        try {
            final ProcessManager manager = mzController.getManager();
            // Assert running algorithm
            manager.assertRunning();
            // Update walkable state
            clObserver.setWalkable(walk);
            // Update background
            if (walk)
                update.accept(State.WALKABLE.getColor());
            else
                update.accept(State.UNWALKABLE.getColor());
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    public synchronized static void select(final CellSubject selected) {
        // Focus cell
        focus(selected);
        // Update selected cell
        CellSubject.selected = selected;
    }

    public synchronized static final void focus(final CellSubject focused) {
        // Ignore if selected
        if (selected != null)
            return;
        // Unfocus cell
        if (CellSubject.focused != null)
            CellSubject.focused.update.accept(State.WALKABLE.getColor());
        // Focus cell
        if (focused != null)
            if (focused.getBackground() == State.WALKABLE.getColor())
                focused.update.accept(State.UNWALKABLE.getColor());
            else
                focused.update.accept(focused.getBackground());
        // Update focused state
        CellSubject.focused = focused;
    }

    public static CellSubject getSelected() {
        return selected;
    }

    public static final CellSubject getFocused() {
        return focused;
    }

    public transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    public CellObserver clObserver;

    public final CellObserver getObserver() {
        return clObserver;
    }

    public final void setObserver(final CellObserver clObserver) {
        this.clObserver = clObserver;
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
                    mzView.releasePopup(CellSubject.this).show(CellSubject.this, e.getX(), e.getY());
                }
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        }

        @Override
        public synchronized final void mouseEntered(final MouseEvent e) {
            // Focus cell
            focus(CellSubject.this);
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
