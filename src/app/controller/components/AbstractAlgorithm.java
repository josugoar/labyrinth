package app.controller.components;

/**
 * Abstract algorithm class wrapper.
 */
public abstract class AbstractAlgorithm {

    /**
     * Awake algorithm on given euclidean space.
     *
     * @param <T>  AbstractCell<T>
     * @param grid T[][]
     */
    public abstract <T extends AbstractCell<T>> void awake(final T[][] grid);

    @Override
    public final String toString() {
        return this.getClass().getSimpleName();
    }

}
