package utils;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

/**
 * Grid system lattice manager, implementing <code>java.io.Serializable</code>.
 *
 * @param <T> GridManager.GridObject<T>
 * @see java.io.Serializable Serializable
 * @see utils.GridManager.GridObject GridObject
 */
public class GridManager<T extends GridManager.GridObject<T>> implements Serializable {

    /**
     * <code>utils.GridManager</code> vertex object, extending
     * <code>java.io.Serializable</code>.
     *
     * @param <T> GridObject<T>
     * @see java.io.Serializable Serializable
     * @see utils.GridManager GridManager
     */
    public abstract interface GridObject<T extends GridObject<T>> extends Serializable {

        /**
         * Return current <code>utils.GridManager</code>.
         *
         * @return GridManager<T>
         */
        public abstract GridManager<T> getManager();

        /**
         * Set current <code>utils.GridManager</code>.
         *
         * @param manager GridManager<T>
         */
        public abstract void setManager(final GridManager<T> manager);

        /**
         * Get current <code>GridManager<T></code> coordinates.
         *
         * @return Point
         */
        public abstract Point getPoint();

        /**
         * Set current <code>GridManager<T></code> coordinates.
         *
         * @param point Point
         */
        public abstract void setPoint(final Point point);

    }

    private static final long serialVersionUID = 1L;

    /**
     * Current <code>utils.GridManager.GridObject</code> array structure.
     */
    protected T[][] grid;

    /**
     * Current <code>java.awt.Dimension</code>.
     */
    protected Dimension dimension;

    /**
     * Current <code>utils.GridManager.GridObject</code> initializer.
     */
    protected BiFunction<GridManager<T>, Point, T> initializer;

    /**
     * Enclose Dimension and BiFunction.
     *
     * @param dimension   Dimension
     * @param initializer BiFunction<GridManager<T>, Point, T>
     * @throws NoSuchElementException if (dimension == null || dimension.width < 0
     *                                || dimension.height < 0)
     */
    public GridManager(final Dimension dimension, final BiFunction<GridManager<T>, Point, T> initializer)
            throws NoSuchElementException {
        this.setInitializer(initializer);
        this.resize(dimension);
    }

    /**
     * Enclose Dimension.
     *
     * @param dimension Dimension
     * @throws NoSuchElementException if (dimension == null || dimension.width < 0
     *                                || dimension.height < 0)
     */
    public GridManager(final Dimension dimension) throws NoSuchElementException {
        this(dimension, null);
    }

    /**
     * Set current <code>java.awt.Dimension</code>.
     *
     * @param dimension Dimension
     * @throws NoSuchElementException if (dimension == null || dimension.width < 0
     *                                || dimension.height < 0)
     */
    @SuppressWarnings("unchecked")
    public final void resize(final Dimension dimension) throws NoSuchElementException {
        assertDimension(dimension, true);
        this.dimension = dimension;
        grid = (T[][]) new GridManager.GridObject[dimension.width][dimension.height];
        for (int row = 0; row < dimension.width; row++)
            for (int col = 0; col < dimension.height; col++)
                if (initializer != null)
                    setGridObject(new Point(row, col), initializer.apply(this, new Point(row, col)));
    }

    /**
     * Flatten current <code>utils.GridManager.GridObject</code> array structure.
     *
     * @return Object[]
     */
    public final Object[] ravel() {
        return Arrays.stream(grid).flatMap(Arrays::stream).toArray();
    }

    /**
     * Return current <code>java.awt.Dimension</code>.
     *
     * @return Dimension
     */
    public final Dimension getDimension() {
        return new Dimension(dimension.width, dimension.height);
    }

    /**
     * Assert valid <code>java.awt.Dimension</code>.
     *
     * @param dimension      Dimension
     * @param throwException boolean
     * @return boolean
     * @throws NoSuchElementException if ((dimension == null || dimension.width < 0
     *                                || dimension.height < 0) && throwException)
     */
    public final boolean assertDimension(final Dimension dimension, final boolean throwException)
            throws NoSuchElementException {
        try {
            if (dimension == null)
                throw new NullPointerException("Dimension must not be null...");
            if (dimension.width < 0 || dimension.height < 0)
                throw new InvalidParameterException("Dimension must not be negative...");
        } catch (final NullPointerException | IndexOutOfBoundsException e) {
            if (throwException)
                throw new NoSuchElementException(e.toString());
        }
        return false;
    }

    /**
     * Return <code>utils.GridManager.GridObject</code> in <code>java.awt.Point</code>
     * coordinates.
     *
     * @param point Point
     * @return T
     */
    public final T getGridObject(final Point point) {
        if (assertPoint(point, false))
            return null;
        return grid[point.x][point.y];
    }

    /**
     * Set <code>utils.GridManager.GridObject</code> in <code>java.awt.Point</code>
     * coordinates.
     *
     * @param point      Point
     * @param gridObject T
     * @throws NoSuchElementException if (dimension == null || dimension.width < 0
     *                                || dimension.height < 0)
     */
    public void setGridObject(final Point point, final T gridObject) throws NoSuchElementException {
        assertPoint(point, true);
        grid[point.x][point.y] = gridObject;
    }

    /**
     * Assert valid <code>java.awt.Point</code>.
     *
     * @param point          Point
     * @param throwException boolean
     * @return boolean
     * @throws NoSuchElementException if (point == null || point.x < 0 || point.x >
     *                                dimension.width - 1 || point.y < 0 || point.y
     *                                > dimension.height - 1)
     */
    public final boolean assertPoint(final Point point, final boolean throwException) throws NoSuchElementException {
        try {
            if (point == null)
                throw new NullPointerException("Point must not be null...");
            if (point.x < 0 || point.x > dimension.width - 1 || point.y < 0 || point.y > dimension.height - 1)
                throw new IndexOutOfBoundsException("Point does not exist...");
        } catch (final NullPointerException | IndexOutOfBoundsException e) {
            if (throwException)
                throw new NoSuchElementException(e.toString());
        }
        return false;
    }

    /**
     * Return current <code>utils.GridManager.GridObject</code> initializer.
     *
     * @return BiFunction<GridManager<T>, Point, T>
     */
    public final BiFunction<GridManager<T>, Point, T> getInitializer() {
        return initializer;
    }

    /**
     * Return current <code>utils.GridManager.GridObject</code> initializer.
     *
     * @param initializer BiFunction<GridManager<T>, Point, T>
     */
    public final void setInitializer(final BiFunction<GridManager<T>, Point, T> initializer) {
        this.initializer = initializer;
    }

    @Override
    public String toString() {
        String out = "";
        for (final T[] row : grid) {
            for (final T gridObject : row)
                out += gridObject.toString() + "\t";
            out += "\n";
        }
        return out;
    }

}
