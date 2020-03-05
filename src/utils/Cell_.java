package src.utils;

/**
 * Cell.
 */
public interface Cell_ {

    public static enum State {
        VISITED, GERMINATED, PATH
    }

    State state = null;

    public State getState();

    public void setState(final State state);

}
