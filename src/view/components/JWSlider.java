package src.view.components;

import java.awt.Dimension;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

/**
 * JWSlider.
 *
 * @see javax.swing.JSlider JSlider
 */
public class JWSlider extends JSlider {

    private static final long serialVersionUID = 1L;

    public JWSlider(final ChangeListener l, final Dimension preferredSize) {
        super(JSlider.HORIZONTAL, 0, 10, 5);
        // this.addChangeListener(e -> {
        //     JWSlider source = (JWSlider) e.getSource();
        //     if (!source.getValueIsAdjusting()) {
        //         int fps = (int) source.getValue();
        //         if (fps == 0) {
        //             if (!frozen) stopAnimation();
        //         } else {
        //             delay = 1000 / fps;
        //             timer.setDelay(delay);
        //             timer.setInitialDelay(delay * 10);
        //             if (frozen)
        //                 startAnimation();
        //         }
        //     }
        // });
        this.setPreferredSize(new Dimension(180, 20));
    }

    public JWSlider() {

    }
}
