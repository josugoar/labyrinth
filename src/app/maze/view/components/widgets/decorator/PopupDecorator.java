package app.maze.view.components.widgets.decorator;

import java.awt.Component;

import javax.swing.JPopupMenu;
import javax.swing.Timer;

public class PopupDecorator extends JPopupMenu {

    private static final long serialVersionUID = 1L;

    public PopupDecorator(final Component component) {
        super(null);
        // Return focus after state change
        this.addPropertyChangeListener("visible", e -> {
            new Timer(1, l -> {
                component.requestFocusInWindow();
                ((Timer) l.getSource()).stop();
            }).start();
        });
    }

}
