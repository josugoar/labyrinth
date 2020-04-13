package app.maze.view.components.widgets.decorator;

import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class ButtonDecorator extends JButton {

    private static final long serialVersionUID = 1L;

    {
        // Iconify JButton
        this.setFocusable(false);
        this.setContentAreaFilled(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
    }

    public ButtonDecorator(final String toolTipText, final Icon icon) {
        super("", icon);
        this.setToolTipText(toolTipText);
    }

}
