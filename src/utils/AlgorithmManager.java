package utils;

import java.io.Serializable;

import javax.xml.crypto.AlgorithmMethod;

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
     * interruptions.
     */
    protected Object lock = new Object();

    /**
     * Volatile flag for algorithm running state.
     */
    // TODO: AtomicBoolean
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
     * Return current running state.
     *
     * @return boolean
     */
    protected final boolean isRunning() {
        return this.running;
    }

    /**
     * Set current running state.
     *
     * @param running boolean
     */
    protected synchronized void setRunning(final boolean running) {
        if (!running)
            this.setWaiting(false);
        this.running = running;
    }

    /**
     * Assert current running state.
     *
     * @throws InterruptedException if (running)
     */
    protected final void assertRunning() throws InterruptedException {
        if (this.running)
            throw new InterruptedException("Invalid input while running...");
    }

    /**
     * Return current waiting state.
     *
     * @return boolean
     */
    protected final boolean isWaiting() {
        return this.waiting;
    }

    /**
     * Set current waiting state.
     *
     * @param waiting boolean
     */
    protected synchronized void setWaiting(final boolean waiting) {
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
    protected final void assertWaiting() {
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
    protected final int getDelay() {
        return this.delay;
    }

    /**
     * Set current delay reference.
     *
     * @param delay int
     */
    protected synchronized void setDelay(final int delay) {
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
