package app.maze.view.components.widgets.decorator;

import java.awt.Dimension;
import java.io.Serializable;
import java.security.InvalidParameterException;

import javax.swing.JSlider;

public class SliderDecorator extends JSlider {

    // TODO: Add JSlider annotations

    private static final long serialVersionUID = 1L;

    {
        // Set default size
        this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
    }

    public SliderDecorator(final int min, final int max, final int val) {
        super(min, max, val);
    }

    public SliderDecorator(final BoundedRange range) {
        this(range.getMin(), range.getMax(), range.getValue());
    }

    public static class BoundedRange implements Serializable {

        private static final long serialVersionUID = 1L;

        private int min;

        private int max;

        private int val;

        public BoundedRange(final int min, final int max, final int val) throws InvalidParameterException {
            if (min > max)
                throw new InvalidParameterException();
            this.min = min;
            this.max = max;
            this.setValue(val);
        }

        public int getMin() {
            return this.min;
        }

        public final void setMin(final int min) throws InvalidParameterException {
            if (min > this.max || min > this.val)
                throw new InvalidParameterException();
            this.min = min;
        }

        public final int getMax() {
            return this.max;
        }

        public final void setMax(final int max) throws InvalidParameterException {
            if (max < this.min || max < this.val)
                throw new InvalidParameterException();
            this.max = max;
        }

        public final int getValue() {
            return this.val;
        }

        public final void setValue(final int val) throws InvalidParameterException {
            if (val < this.min || val > this.max)
                throw new InvalidParameterException();
            this.val = val;
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.max;
            result = prime * result + this.min;
            result = prime * result + this.val;
            return result;
        }

        @Override
        public final boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (this.getClass() != obj.getClass())
                return false;
            final BoundedRange other = (BoundedRange) obj;
            if (this.max != other.max)
                return false;
            if (this.min != other.min)
                return false;
            if (this.val != other.val)
                return false;
            return true;
        }

        @Override
        public final String toString() {
            return String.format("BoundedRange [min: %d, max: %d, val: %d]", this.min, this.max, this.val);
        }

    }

}
