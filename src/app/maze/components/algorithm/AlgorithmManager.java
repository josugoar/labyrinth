package app.maze.components.algorithm;

import java.io.Serializable;

import javax.xml.crypto.AlgorithmMethod;

import utils.JWrapper;

/**
 * Generic algorithm abstract class wrapper, implementing
 * <code>javax.xml.crypto.AlgorithmMethod</code>,
 * <code>java.lang.Runnable</code> and <code>java.io.Serializable</code>.
 *
 * @see javax.xml.crypto.AlgorithmMethod AlgorithmMethod
 * @see java.lang.Runnable Runnable
 * @see java.io.Serializable Serializable
 */
public abstract class AlgorithmManager implements AlgorithmMethod, Runnable, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Lock <code>java.lang.Object</code> for <code>java.lang.Thread</code>
     * lock synchronization.
     */
    protected Object lock = new Object();

    /**
     * Volatile flag for algorithm running state.
     */
    protected volatile boolean running = false;

    /**
     * Volatile flag for algorithm waiting state.
     */
    protected volatile boolean waiting = false;

    /**
     * Volatile delay reference.
     */
    protected volatile int delay = 100;

    /**
     * Awake algorithm.
     */
    protected abstract void awake();

    /**
     * Start <code>java.lang.Thread</code> execution.
     */
    protected final void start() {
        new Thread(this).start();
    }

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
     * @param running boolean
     */
    public synchronized void setRunning(final boolean running) {
        if (!running)
            this.setWaiting(false);
        this.running = running;
    }

    /**
     * Assert current running state.
     *
     * @throws InterruptedException if (running)
     */
    public final void assertRunning() throws InterruptedException {
        if (this.running)
            throw new InterruptedException("Invalid input while running...");
    }

    /**
     * Return current waiting state.
     *
     * @return boolean
     */
    public final boolean isWaiting() {
        return this.waiting;
    }

    /**
     * Set current waiting state.
     *
     * @param waiting boolean
     */
    public synchronized void setWaiting(final boolean waiting) {
        try {
            if (waiting && !this.running)
                throw new InterruptedException("Algorithm is not running...");
            if (!waiting)
                synchronized (lock) {
                    this.lock.notifyAll();
                }
            this.waiting = waiting;
        } catch (final InterruptedException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Assert current waiting state.
     */
    public final void assertWaiting() {
        if (this.waiting)
            synchronized (this.lock) {
                try {
                    this.lock.wait();
                } catch (final InterruptedException e) {
                    JWrapper.dispatchException(e);
                }
            }
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
    public final void run() {
        this.awake();
    }

    @Override
    public String toString() {
        return this.getAlgorithm();
    }
}
