package app.controller.components;

/**
 * Abstract algorithm interface wrapper.
 */
public abstract interface AbstractAlgorithm {

    // TODO: extends AlgorithmMethod

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
