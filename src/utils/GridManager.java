package utils;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class GridManager<T extends GridManager.GridObject<T>> implements Serializable {

    public abstract interface GridObject<T extends GridObject<T>> extends Serializable {

        public abstract GridManager<T> getManager();

        public abstract void setManager(GridManager<T> manager);

        public abstract Point getPoint();

        public abstract void setPoint(Point point);

    }

    private static final long serialVersionUID = 1L;

    protected T[][] grid;

    protected Dimension dimension;

    protected BiFunction<GridManager<T>, Point, T> initializer;

    public GridManager(final Dimension dimension, final BiFunction<GridManager<T>, Point, T> initializer)
            throws NoSuchElementException {
        this.setInitializer(initializer);
        this.resize(dimension);
    }

    public GridManager(final Dimension dimension) {
        this(dimension, null);
    }

    @SuppressWarnings("unchecked")
    public final void resize(final Dimension dimension) throws NoSuchElementException {
        this.assertDimension(dimension, true);
        this.dimension = dimension;
        this.grid = (T[][]) new GridManager.GridObject[this.dimension.width][this.dimension.height];
        for (int row = 0; row < this.dimension.width; row++)
            for (int col = 0; col < this.dimension.height; col++)
                if (this.initializer != null)
                    this.setGridObject(new Point(row, col), this.initializer.apply(this, new Point(row, col)));
    }

    public final Object[] ravel() {
        return Arrays.stream(this.grid).flatMap(Arrays::stream).toArray();
    }

    public final Dimension getDimension() {
        return new Dimension(this.dimension.width, this.dimension.height);
    }

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

    public final T getGridObject(final Point point) {
        if (this.assertPoint(point, false))
            return null;
        return this.grid[point.x][point.y];
    }

    public void setGridObject(final Point point, final T gridObject) throws NoSuchElementException {
        this.assertPoint(point, true);
        this.grid[point.x][point.y] = gridObject;
    }

    public final boolean assertPoint(final Point point, final boolean throwException) throws NoSuchElementException {
        try {
            if (point == null)
                throw new NullPointerException("Point must not be null...");
            if (point.x < 0 || point.x > this.dimension.width - 1 || point.y < 0 || point.y > this.dimension.height - 1)
                throw new IndexOutOfBoundsException("Point does not exist...");
        } catch (final NullPointerException | IndexOutOfBoundsException e) {
            if (throwException)
                throw new NoSuchElementException(e.toString());
        }
        return false;
    }

    public final BiFunction<GridManager<T>, Point, T> getInitializer() {
        return this.initializer;
    }

    public final void setInitializer(final BiFunction<GridManager<T>, Point, T> initializer) {
        this.initializer = initializer;
    }

    @Override
    public String toString() {
        String out = "";
        for (final T[] row : this.grid) {
            for (final T gridObject : row)
                out += gridObject.toString() + "\t";
            out += "\n";
        }
        return out;
    }

}
