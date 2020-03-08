package app.view.components;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

/**
 * An extended <code>javax.swing.JSlider</code> implementation.
 *
 * @see javax.swing.JSlider JSlider
 */
public final class JWSlider extends JSlider {

    private static final long serialVersionUID = 1L;

    /**
     * Enclose minimum, maximum, n and ChangeListener.
     *
     * @param minimum int
     * @param maximum int
     * @param n       int
     * @param l       ChangeListener
     */
    public JWSlider(final int minimum, final int maximum, final int n, final ChangeListener l) {
        super(JSlider.HORIZONTAL, minimum, maximum, n);
        this.addChangeListener(l);
    }

}
