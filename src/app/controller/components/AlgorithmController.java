package app.controller.components;

/**
 * Abstract algorithm class wrapper.
 */
public abstract class AlgorithmController {

    /**
     * Awake algorithm on given euclidean space.
     *
     * @param <T>  CellController<T>
     * @param grid T[][]
     */
    public abstract <T extends CellController<T>> void awake(final T[][] grid);

    /**
     * Return algorithm simple class name.
     */
    @Override
    public final String toString() {
        return this.getClass().getSimpleName();
    }

}
