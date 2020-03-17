package app.view.components;

import java.awt.Cursor;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class JWButton extends JButton {

    private static final long serialVersionUID = 1L;

    {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentAreaFilled(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setFocusPainted(false);
    }

    public JWButton(final Icon icon, final String toolTipText, final ActionListener l) {
        super("", icon);
        this.setToolTipText(toolTipText);
        this.addActionListener(l);
    }

}
