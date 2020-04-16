package app.maze.components.cell;

import java.awt.Color;

import javax.swing.UIManager;

public enum State {

    WALKABLE(UIManager.getColor("Panel.background")),

    UNWALKABLE(Color.BLACK),

    ROOT(Color.RED),

    TARGET(Color.GREEN),

    VISITED(Color.CYAN),

    GERMINATED(Color.BLUE),

    PATH(Color.YELLOW);

    private final Color color;

    private State(final Color color) {
        this.color = color;
    }

    public static final State getState(final Color color) {
        // Range through State
        for (final State state : values())
            // Return State if Color match
            if (state.getColor().equals(color))
                return state;
        return null;
    }

    public final Color getColor() {
        return color;
    }

}
