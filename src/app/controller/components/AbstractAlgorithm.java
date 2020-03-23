package app.controller.components;

import javax.xml.crypto.AlgorithmMethod;

/**
 * Abstract algorithm interface wrapper, extending
 * <code>javax.xml.crypto.AlgorithmMethod</code>.
 *
 * @see javax.xml.crypto.AlgorithmMethod AlgorithmMethod
 */
public abstract interface AbstractAlgorithm extends AlgorithmMethod {

    /**
     * Awake algorithm on given euclidean space.
     *
     * @param <T>  AbstractCell<T>
     * @param grid T[][]
     */
    public abstract <T extends AbstractCell<T>> void awake(final T[][] grid);

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

}
