package app.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import app.controller.Cell;

public final class Node implements Serializable {

    public static enum State {
        VISITED, GERMINATED, PATH;

        public static final Map<State, Color> COLOR = new EnumMap<State, Color>(State.class) {
            private static final long serialVersionUID = 1L;
            {
                this.put(State.VISITED, Color.CYAN);
                this.put(State.GERMINATED, Color.BLUE);
                this.put(State.PATH, Color.YELLOW);
            }
        };
    }

    private static final long serialVersionUID = 1L;

    private final Node parent;
    private final Cell outer;

    private State state = State.GERMINATED;

    public Node(final Node parent, final Cell outer) {
        this.parent = parent;
        this.outer = outer;
    }

    public Node(final Cell outer) {
        this(null, outer);
    }

    public Node getParent() {
        return this.parent;
    }

    public Cell getOuter() {
        return this.outer;
    }

    public State getState() {
        return this.state;
    }

    public void setState(final State state) {
        this.state = Objects.requireNonNull(state, "'state' must not be null");
        this.outer.repaint();
    }

}
