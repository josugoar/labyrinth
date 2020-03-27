package algo;

import java.io.Serializable;
import java.security.spec.AlgorithmParameterSpec;

import javax.xml.crypto.AlgorithmMethod;

import utils.JWrapper;

/**
 * Generic algorithm abstract class wrapper, extending implementing
 * <code>javax.xml.crypto.AlgorithmMethod</code>,
 * <code>java.lang.Runnable</code> and <code>java.io.Serializable</code>.
 *
 * @see javax.xml.crypto.AlgorithmMethod AlgorithmMethod
 * @see java.lang.Runnable Runnable
 * @see java.io.Serializable Serializable
 */
public abstract class AbstractAlgorithm implements AlgorithmMethod, Runnable, Serializable {

    // TODO: Spec
    private class GeneratorSpec implements AlgorithmParameterSpec, Serializable {

        private static final long serialVersionUID = 1L;

    }

    private static final long serialVersionUID = 1L;

    /**
     * Lock <code>java.lang.Object</code> for <code>java.lang.Thread</code>
     * interruptions.
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
            try {
                this.setWaiting(false);
            } catch (final InterruptedException e) {
                JWrapper.dispatchException(e);
            }
        this.running = running;
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
     * @throws InterruptedException if (waiting && !running)
     */
    public synchronized void setWaiting(final boolean waiting) throws InterruptedException {
        if (waiting && !this.running)
            throw new InterruptedException("Algorithm must first be running...");
        if (!waiting)
            synchronized (lock) {
                this.lock.notifyAll();
            }
        this.waiting = waiting;
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
