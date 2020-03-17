package app.view.components;

import java.security.InvalidParameterException;

public final class Range {

    private final int min, max;
    private int val;

    public Range(final int min, final int max, final int val) throws InvalidParameterException {
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
