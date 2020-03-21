package app.model;

import app.model.components.CellPanel;

public abstract class Generator {

    public final void awake(final CellPanel[][] grid) {

    }

    public static final class BackTracker extends Generator {

        @Override
        public final String toString() {
            return "BackTracker";
        }

    }

}
