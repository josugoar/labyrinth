package app.maze.components.cell.view;

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
    public final Consumer<State> update = (Consumer<State> & Serializable) state ->
            setBorder(BorderFactory.createLineBorder(state.getColor()));

    {
        // Set default State
        update.accept(State.WALKABLE);
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
        final Consumer<State> update = this.update.andThen(this::setBackground);
        try {
            final ProcessManager manager = mzController.getManager();
            // Assert running AlgorithmManager
            manager.assertRunning();
            // Update Walkable
            clComposite.setWalkable(walk);
            // Update CellView
            if (walk)
                update.accept(State.WALKABLE);
            else
                update.accept(State.UNWALKABLE);
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    public synchronized static void select(final CellView selected) {
        // Focus CellView
        focus(selected);
        // Update selected CellView
        CellView.selected = selected;
    }

    public synchronized static final void focus(final CellView focused) {
        // Ignore if selected CellView
        if (selected != null)
            return;
        // Unfocus CellView
        if (CellView.focused != null)
            CellView.focused.update.accept(State.WALKABLE);
        // Focus CellView
        if (focused != null) {
            final State state = State.getState(focused.getBackground());
            if (state.equals(State.WALKABLE))
                focused.update.accept(State.UNWALKABLE);
            else
                focused.update.accept(state);
        }
        // Update focused CellView
        CellView.focused = focused;
    }

    public static CellView getSelected() {
        return selected;
    }

    public static final CellView getFocused() {
        return focused;
    }

    public final void setBackground(final State state) {
        // Delegate JComponent background
        setBackground(state.getColor());
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

        private final void dispatchButton(final MouseEvent e) {
            // Ignore if not Shift down
            if (!e.isShiftDown())
                return;
            // Set node Walkable state
            if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                walk(false);
            else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                walk(true);
        }

        @Override
        public final void mousePressed(final MouseEvent e) {
            // Clear node parent relationships
            mzController.clear();
            try {
                final ProcessManager manager = mzController.getManager();
                if (e.isShiftDown()) {
                    // Assert running AlgorithmManager
                    manager.assertRunning();
                    // Dispatch MouseEvent
                    dispatchButton(e);
                } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                    final MazeView mzView = mzController.getView();
                    // Assert running AlgorithmManager
                    manager.assertRunning();
                    // Release endpoint JPupupMenu
                    mzView.releasePopup(CellView.this).show(CellView.this, e.getX(), e.getY());
                }
            } catch (final InterruptedException l) {
                JWrapper.dispatchException(l);
            }
        }

        @Override
        public final void mouseEntered(final MouseEvent e) {
            // Focus CellView
            focus(CellView.this);
            // Dispatch MouseEvent
            dispatchButton(e);
        }

        @Override
        public final void mouseExited(final MouseEvent e) {
            // Unfocus CellView
            focus(null);
            // Ignore if selected CellView
            if (selected != null)
                return;
            // Reset Border
            update.accept(State.WALKABLE);
        }

    };

}
