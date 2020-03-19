package app.model.components;

import java.awt.Color;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import app.controller.components.CellController;

public final class Node<T extends CellController> implements Serializable {

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

    private final Node<T> parent;
    private final T outer;

    private State state = State.GERMINATED;

    public Node(final Node<T> parent, final T outer) {
        this.parent = parent;
        this.outer = outer;
    }

    public Node(final T outer) {
        this(null, outer);
    }

    public Node<T> getParent() {
        return this.parent;
    }

    public T getOuter() {
        return this.outer;
    }

    public State getState() {
        return this.state;
    }

    public void setState(final State state) {
        this.state = Objects.requireNonNull(state, "'state' must not be null");
        this.outer.stateChange();
    }

}
