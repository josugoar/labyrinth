package app.controller.components;

import java.awt.Point;
import java.io.Serializable;

import javax.xml.crypto.AlgorithmMethod;

import app.view.components.RangedSlider.BoundedRange;

/**
 * Abstract algorithm abstract class wrapper, implementing
 * <code>javax.xml.crypto.AlgorithmMethod</code> and
 * <code>java.io.Serializable</code>.
 *
 * @see javax.xml.crypto.AlgorithmMethod AlgorithmMethod
 * @see java.io.Serializable Serializable
 */
public abstract class AbstractAlgorithm implements AlgorithmMethod, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Flag for algorithm running state.
     */
    protected volatile boolean running = false;

    /**
     * Delay <code>app.view.components.RangedSlider.BoundedRange</code> between draw
     * cycles.
     *
     * @see app.view.components.RangedSlider.BoundedRange BoundedRange
     */
    protected final BoundedRange delay = new BoundedRange(0, 250, 100);

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
    public final boolean isRunning() {
        return this.running;
    }

    /**
     * Set current running state.
     *
     * @param isRunning boolean
     */
    public final void setRunning(final boolean running) {
        this.running = running;
    }

    /**
     * Return current delay <code>BoundedRange</code>.
     *
     * @return BoundedRange
     */
    public final BoundedRange getDelay() {
        return this.delay;
    }

    /**
     * Set current delay <code>BoundedRange</code> value.
     *
     * @param delay int
     */
    public final void setDelay(final int delay) {
        this.delay.setValue(delay);
    }

    @Override
    public final String getAlgorithm() {
        return this.getClass().getSimpleName();
    }

    @Override
    public final String toString() {
        return this.getAlgorithm();
    }

}
