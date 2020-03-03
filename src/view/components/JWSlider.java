package src.view.components;

import java.awt.Dimension;

import javax.swing.JSlider;

/**
 * JWSlider.
 *
 * @see javax.swing.JSlider JSlider
 */
public class JWSlider extends JSlider {

    private static final long serialVersionUID = 1L;

    public JWSlider() {
        super(JSlider.HORIZONTAL, 0, 10, 5);
        this.setPreferredSize(new Dimension(180, 20));
    }
}
