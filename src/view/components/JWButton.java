package src.view.components;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public final class JWButton extends JButton {

    private static final long serialVersionUID = 1L;

    /**
     * Enclose JButton Text, Dimension and ActionListener
     *
     * @param text          String
     * @param l             ActionListener
     * @param preferredSize Dimension
     */
    public JWButton(final String text, final ActionListener l, final Dimension preferredSize) {
        super(text);
        this.addActionListener(l);
        this.setPreferredSize(preferredSize);
    }

    /**
     * Enclose JButton Text and ActionListener
     *
     * @param text          String
     * @param l             ActionListener
     */
    public JWButton(final String text, final ActionListener l) {
        this(text, l, null);
    }

}
