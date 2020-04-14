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

    public final Color getColor() {
        return color;
    }

}
