package app.model;

import app.model.components.Cell;

public abstract class Generator {

    public final void awake(final Cell[][] grid) {

    }

    public static final class BackTracker extends Generator {

        @Override
        public final String toString() {
            return "BackTracker";
        }

    }

}
