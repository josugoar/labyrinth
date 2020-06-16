package app.maze.components.cell;

import java.awt.Color;

import javax.swing.UIManager;

/**
 * Node state collection.
 */
public enum State {

    WALKABLE(UIManager.getColor("Panel.background")),

    UNWALKABLE(Color.BLACK),

    ROOT(Color.RED),

    TARGET(Color.GREEN),

    VISITED(Color.CYAN),

    GERMINATED(Color.BLUE),

    PATH(Color.YELLOW);

    /**
     * State <code>java.awt.Color</code> reference.
     */
    private final Color color;

    /**
     * Enclose <code>java.awt.Color</code> reference.
     *
     * @param color Color
     */
    private State(final Color color) {
        this.color = color;
    }

    /**
     * Return <code>java.awt.Color</code> state reference.
     *
     * @param color Color
     * @return State
     */
    public static final State getState(final Color color) {
        for (final State state : values())
            if (state.getColor().equals(color))
                return state;
        return null;
    }

    /**
     * Return <code>java.awt.Color</code> reference.
     *
     * @return Color
     */
    public final Color getColor() {
        return color;
    }

}
