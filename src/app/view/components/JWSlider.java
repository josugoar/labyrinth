package app.view.components;

import java.awt.Dimension;
import java.io.Serializable;
import java.security.InvalidParameterException;

import javax.swing.JSlider;

/**
 * <code>javax.swing.JSlider</code>
 * <code>app.view.components.JWSlider.JWRange</code> wrapper.
 *
 * @see javax.swing.JSlider JSlider
 * @see app.view.components.JWSlider.JWRange
 */
public class JWSlider extends JSlider {

    private static final long serialVersionUID = 1L;

    {
        // Set custom size
        this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
    }

    /**
     * Create a new <code>javax.swing.JSlider</code> wrapper with given
     * <code>app.view.components.JWSlider.JWRange</code>.
     *
     * @param range app.view.components.JWSlider.JWRange JWRange
     */
    public JWSlider(final JWRange range) {
        super(range.getMin(), range.getMax(), range.getValue());
    }

    /**
     * <code>app.view.components.JWSlider</code> range helper, implementing
     * <code>java.io.Serializable</code>.
     *
     * @see app.view.components.JWSlider JWSlider
     * @see java.io.Serializable Serializable
     */
    public static final class JWRange implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Inclusive minimum value stored.
         */
        private final int min;

        /**
         * Inclusive maximum value stored.
         */
        private final int max;

        /**
         * Current value stored.
         */
        private int val;

        /**
         * Create a new range.
         *
         * @param min int
         * @param max int
         * @param val int
         * @throws InvalidParameterException if (min > max || val < min || val > max)
         */
        public JWRange(final int min, final int max, final int val) throws InvalidParameterException {
            if (min > max)
                throw new InvalidParameterException();
            this.min = min;
            this.max = max;
            this.setValue(val);
        }

        /**
         * Return current inclusive minimum value.
         *
         * @return int
         */
        public int getMin() {
            return this.min;
        }

        /**
         * Return current inclusive maximum value.
         *
         * @return int
         */
        public int getMax() {
            return this.max;
        }

        /**
         * Return current value.
         *
         * @return int
         */
        public int getValue() {
            return this.val;
        }

        /**
         * Set current value.
         *
         * @param val int
         * @throws InvalidParameterException if (val < min || val > max)
         */
        public void setValue(final int val) throws InvalidParameterException {
            if (val < this.min || val > this.max)
                throw new InvalidParameterException();
            this.val = val;
        }

    }

}
