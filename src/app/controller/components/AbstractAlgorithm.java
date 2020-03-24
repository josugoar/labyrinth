package app.controller.components;

import java.io.Serializable;

import javax.xml.crypto.AlgorithmMethod;

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
