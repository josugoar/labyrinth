package app.view.components;

import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * Iconified <code>javax.swing.JButton</code>.
 *
 * @see javax.swing.JButton JButton
 */
public class IconifiedButton extends JButton {

    private static final long serialVersionUID = 1L;

    {
        // Iconify JButton
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
    }

    /**
     * Create a new iconified <code>javax.swing.JButton</code>.
     *
     * @param toolTipText String
     * @param icon        Icon
     */
    public IconifiedButton(final String toolTipText, final Icon icon) {
        super("", icon);
        this.setToolTipText(toolTipText);
    }

}
