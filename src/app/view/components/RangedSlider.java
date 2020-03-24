package app.view.components;

import java.awt.Dimension;
import java.io.Serializable;
import java.security.InvalidParameterException;

import javax.swing.JSlider;

/**
 * <code>javax.swing.JSlider</code>
 * <code>app.view.components.RangedSlider.BoundedRange</code> wrapper.
 *
 * @see javax.swing.JSlider JSlider
 * @see app.view.components.RangedSlider.BoundedRange
 */
public class RangedSlider extends JSlider {

    // TODO: Add JSlider annotations

    private static final long serialVersionUID = 1L;

    {
        // Set custom size
        this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
    }

    /**
     * Create a new <code>javax.swing.JSlider</code>
     * <code>app.view.components.RangedSlider.BoundedRange</code> wrapper.
     *
     * @param range app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    public RangedSlider(final BoundedRange range) {
        super(range.getMin(), range.getMax(), range.getValue());
    }

    /**
     * <code>app.view.components.RangedSlider</code> range helper, implementing
     * <code>java.io.Serializable</code>.
     *
     * @see app.view.components.RangedSlider RangedSlider
     * @see java.io.Serializable Serializable
     */
    public static final class BoundedRange implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Inclusive minimum value stored.
         */
        private int min;

        /**
         * Inclusive maximum value stored.
         */
        private int max;

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
        public BoundedRange(final int min, final int max, final int val) throws InvalidParameterException {
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
         * Set current inclusive minimum value.
         *
         * @param min int
         * @throws InvalidParameterException
         */
        public final void setMin(final int min) throws InvalidParameterException {
            if (min > this.max || min > this.val)
                throw new InvalidParameterException();
            this.min = min;
        }

        /**
         * Return current inclusive maximum value.
         *
         * @return int
         */
        public final int getMax() {
            return this.max;
        }

        /**
         * Set current inclusive maximum value.
         *
         * @param max int
         * @throws InvalidParameterException
         */
        public final void setMax(final int max) throws InvalidParameterException {
            if (max < this.min || max < this.val)
                throw new InvalidParameterException();
            this.max = max;
        }

        /**
         * Return current value.
         *
         * @return int
         */
        public final int getValue() {
            return this.val;
        }

        /**
         * Set current value.
         *
         * @param val int
         * @throws InvalidParameterException if (val < min || val > max)
         */
        public final void setValue(final int val) throws InvalidParameterException {
            if (val < this.min || val > this.max)
                throw new InvalidParameterException();
            this.val = val;
        }

    }

}
