package algo.grd;

import java.io.Serializable;

/**
 * Colored state interface, implementing <code>java.io.Serializable</code>.
 *
 * @see java.io.Serializable Serializable
 */
public abstract interface State<T extends Serializable> extends Serializable {

    /**
     * Return current associated reference.
     *
     * @return T
     */
    public abstract T getReference();

    /**
     * Set current associated reference.
     *
     * @param T Object
     */
    public abstract void setReference(final T color);

}
