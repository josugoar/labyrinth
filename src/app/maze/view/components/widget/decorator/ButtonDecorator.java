package app.maze.view.components.widget.decorator;

import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import app.maze.view.components.widget.factory.WidgetFactory;

public class ButtonDecorator extends JButton {

    private static final long serialVersionUID = 1L;

    {
        setFocusable(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
    }

    public ButtonDecorator(final String toolTipText, final String fileName) {
        super("", WidgetFactory.createIcon(fileName));
        setToolTipText(toolTipText);
    }

}
