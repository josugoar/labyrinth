package app.view.components;

import java.awt.Dimension;

import javax.swing.JComboBox;

/**
 * An extended <code>javax.swing.JComboBox</code> implementation.
 *
 * @see javax.swing.JComboBox JComboBox
 */
public final class JWComboBox<E> extends JComboBox<E> {

    private static final long serialVersionUID = 1L;

    public JWComboBox(final E[] data, final Dimension preferredSize) {
        super(data);
        this.setPreferredSize(preferredSize);
    }

}
