package app.controller.components;

import java.awt.Point;
import java.io.Serializable;

import javax.xml.crypto.AlgorithmMethod;

import app.view.components.RangedSlider.BoundedRange;

/**
 * Abstract algorithm interface wrapper, implementing
 * <code>javax.xml.crypto.AlgorithmMethod</code> and
 * <code>java.io.Serializable</code>.
 *
 * @see javax.xml.crypto.AlgorithmMethod AlgorithmMethod
 * @see java.io.Serializable Serializable
 */
public abstract interface AbstractAlgorithm extends AlgorithmMethod, Serializable {

    /**
     * Awake algorithm on given euclidean space.
     *
     * @param <T>  AbstractCell<T>
     * @param grid T[][]
     */
    public abstract <T extends AbstractCell<T>> void awake(final T[][] grid, final Point start, final Point end);

    /**
     * Return current running state.
     *
     * @return boolean
     */
    public abstract boolean getIsRunning();

    /**
     * Set current running state.
     *
     * @param isRunning boolean
     */
    public abstract void setIsRunning(final boolean isRunning);

    /**
     * Return current delay <code>BoundedRange</code>.
     *
     * @return BoundedRange
     */
    public abstract BoundedRange getDelay();

    /**
     * Set current delay <code>BoundedRange</code> value.
     *
     * @param delay int
     */
    public abstract void setDelay(final int delay);

}
