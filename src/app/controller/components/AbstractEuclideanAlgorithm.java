package app.controller.components;

import java.awt.Point;
import java.io.Serializable;

import javax.xml.crypto.AlgorithmMethod;

/**
 * Abstract algorithm abstract class wrapper, implementing
 * <code>javax.xml.crypto.AlgorithmMethod</code> and
 * <code>java.io.Serializable</code>.
 *
 * @see javax.xml.crypto.AlgorithmMethod AlgorithmMethod
 * @see java.io.Serializable Serializable
 */
public abstract class AbstractEuclideanAlgorithm implements AlgorithmMethod, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Volatile flag for algorithm running state.
     */
    protected volatile boolean running = false;

    /**
     * Delay reference for visualizing running process.
     */
    protected int delay = 100;

    /**
     * Awake algorithm on given euclidean space.
     *
     * @param <T>   AbstractCell<T>
     * @param grid  T[][]
     * @param start Point
     * @param end   Point
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
     * Set current running state synchronously.
     *
     * @param isRunning boolean
     */
    public synchronized void setRunning(final boolean running) {
        this.running = running;
    }

    /**
     * Return current delay reference.
     *
     * @return int
     */
    public final int getDelay() {
        return this.delay;
    }

    /**
     * Set current delay reference.
     *
     * @param delay int
     */
    public synchronized void setDelay(final int delay) {
        this.delay = delay;
    }

    @Override
    public final String getAlgorithm() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return this.getAlgorithm();
    }

}
