package app.view.components;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * <code>javax.swing.JPopupMenu</code> that return focus to given
 * <code>java.awt.Component</code>.
 *
 * @see javax.swing.JPopupMenu JPopupMenu
 * @see javax.swing.event.PopupMenuListener PopupMenuListener
 */
public class FocusedPopup extends JPopupMenu {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new focused <code>java.awt.Component</code>.
     *
     * @param component Component
     */
    public FocusedPopup(final Component component) {
        super();
        this.addPropertyChangeListener("visible", e -> {
            // Set focus delay
            new Timer(1, l -> {
                component.requestFocusInWindow();
                ((Timer) l.getSource()).stop();
            }).start();
        });
    }

}
