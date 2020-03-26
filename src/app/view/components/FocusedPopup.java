package app.view.components;

import java.awt.Component;

import javax.swing.JPopupMenu;
import javax.swing.Timer;

/**
 * <code>javax.swing.JPopupMenu</code> that return focus to given
 * <code>java.awt.Component</code>.
 *
 * @see javax.swing.JPopupMenu JPopupMenu
 * @see java.beans.PropertyChangeListener PropertyChangeListener
 */
public class FocusedPopup extends JPopupMenu {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new focused <code>java.awt.Component</code>.
     *
     * @param component Component
     */
    public FocusedPopup(final Component component) {
        super(null);
        this.addPropertyChangeListener("visible", e -> {
            // Set focus delay after property change
            new Timer(1, l -> {
                component.requestFocusInWindow();
                ((Timer) l.getSource()).stop();
            }).start();
        });
    }

}
