package app.view.components;

import java.awt.Dimension;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Objects;

import javax.swing.JSlider;

public class JWSlider extends JSlider {

    private static final long serialVersionUID = 1L;

    {
        this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
    }

    public JWSlider(final JWRange range) {
        super(range.getMin(), range.getMax(), range.getValue());
    }

    public static final class JWRange implements Serializable {

        private static final long serialVersionUID = 1L;

        private final int min, max;
        private int val;

        public JWRange(final int min, final int max, final int val) throws InvalidParameterException {
            if (min > max || val < min || val > max)
                throw new InvalidParameterException();
            this.min = min;
            this.max = max;
            this.val = val;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public int getValue() {
            return this.val;
        }

        public void setValue(final int val) throws InvalidParameterException {
            if (val < this.min || val > this.max)
                throw new InvalidParameterException();
            this.val = val;
        }

    }

}
