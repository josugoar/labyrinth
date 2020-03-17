package app.view.components;

import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JSlider;

public class JWSlider extends JSlider {

    private static final long serialVersionUID = 1L;

    private static Dimension preferredSliderSize = new Dimension(100, 16);

    {
        this.setPreferredSize(JWSlider.preferredSliderSize);
    }

    public JWSlider(final Range range) {
        super(range.getMin(), range.getMax(), range.getValue());
    }

    public static final Dimension getPreferredSliderSize() {
        return JWSlider.preferredSliderSize;
    }

    public static final void setPreferredSliderSize(final Dimension preferredSliderSize) {
        JWSlider.preferredSliderSize = Objects.requireNonNull(preferredSliderSize, "'preferredSliderSize' must not be null");
    }

}
