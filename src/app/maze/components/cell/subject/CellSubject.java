package app.maze.components.cell.subject;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;

import app.maze.components.cell.observer.CellObserver;
import app.maze.controller.MazeController;
import utils.JWrapper;

public final class CellSubject extends JPanel {

    private static final long serialVersionUID = 1L;

    public static enum State {

        WALKABLE(UIManager.getColor("Panel.background")), UNWALKABLE(Color.BLACK), ROOT(Color.RED), TARGET(Color.GREEN);

        private final Color color;

        private State(final Color color) {
            this.color = color;
        }

        public final Color getColor() {
            return this.color;
        }

    }

    private static CellSubject selected = null;

    private static CellSubject focused = null;

    {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public synchronized final void mousePressed(final MouseEvent e) {
                final CellSubject clSubject = (CellSubject) e.getSource();
                // Clear node parent relationships
                clSubject.mzController.clear();
                try {
                    if (e.isShiftDown()) {
                        // Assert running algorithm
                        clSubject.mzController.getProcess().assertRunning();
                        // Set node walkable state
                        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                            clSubject.walk(false);
                        // Set node unwalkable state
                        else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                            clSubject.walk(true);
                    } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
                        // Release endpoint popup
                        clSubject.mzController.getProcess().assertRunning();
                        clSubject.mzController.getView().releasePopup(clSubject).show(clSubject, e.getX(), e.getY());
                    }
                } catch (final InterruptedException l) {
                    JWrapper.dispatchException(l);
                }
            }

            @Override
            public synchronized final void mouseEntered(final MouseEvent e) {
                final CellSubject clSubject = (CellSubject) e.getSource();
                // Focus cell
                CellSubject.focus(clSubject);
                if (e.isShiftDown())
                    // Set node walkable state
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
                        clSubject.walk(false);
                    // Set node unwalkable state
                    else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
                        clSubject.walk(true);
            }

            @Override
            public synchronized final void mouseExited(final MouseEvent e) {
                // Unfocus cell
                CellSubject.focus(null);
            }
        });
    }

    public CellSubject(final MazeController mzController, final CellObserver clObserver) {
        this.setController(mzController);
        this.setObserver(clObserver);
    }

    public CellSubject() {
        this(null, null);
    }

    public final void walk(final boolean walk) {
        try {
            // Assert running algorithm
            this.mzController.getProcess().assertRunning();
            this.clObserver.setWalkable(walk);
            // Update background
            if (walk) {
                this.setBorder(BorderFactory.createLineBorder(CellSubject.State.WALKABLE.getColor()));
                this.setBackground(CellSubject.State.WALKABLE.getColor());
            } else {
                this.setBorder(BorderFactory.createLineBorder(CellSubject.State.UNWALKABLE.getColor()));
                this.setBackground(CellSubject.State.UNWALKABLE.getColor());
            }
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    public synchronized static void select(final CellSubject selected) {
        // Focus cell
        CellSubject.focus(selected);
        // Update selected cell
        CellSubject.selected = selected;
    }

    public synchronized static final void focus(final CellSubject focused) {
        // Ignore if selected
        if (CellSubject.selected != null)
            return;
        // Unfocus cell
        if (CellSubject.focused != null)
            CellSubject.focused.setBorder(BorderFactory.createLineBorder(CellSubject.State.WALKABLE.getColor()));
        // Focus cell
        if (focused != null)
            if (focused.getBackground() == CellSubject.State.WALKABLE.getColor())
                focused.setBorder(BorderFactory.createLineBorder(CellSubject.State.UNWALKABLE.getColor()));
            else
                focused.setBorder(BorderFactory.createLineBorder(focused.getBackground()));
        // Update focused state
        CellSubject.focused = focused;
    }

    public static CellSubject getSelected() {
        return CellSubject.selected;
    }

    public static final CellSubject getFocused() {
        return CellSubject.focused;
    }

    public MazeController mzController;

    public final MazeController getController() {
        return this.mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

    public CellObserver clObserver;

    public final CellObserver getObserver() {
        return this.clObserver;
    }

    public final void setObserver(final CellObserver clObserver) {
        this.clObserver = clObserver;
    }

}
