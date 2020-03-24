package app.model;

import java.awt.Point;
import java.security.spec.AlgorithmParameterSpec;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;

public abstract class Generator implements AbstractAlgorithm {

    private static final long serialVersionUID = 1L;

    protected boolean isRunning = false;

    @Override
    public final <T extends AbstractCell<T>> void awake(final T[][] grid, final Point start, final Point end) {
    }

    @Override
    public final String getAlgorithm() {
        return this.getClass().getSimpleName();
    }

    public static final class BackTracker extends Generator {

        private static final long serialVersionUID = 1L;

        @Override
        public final boolean getIsRunning() {
            return this.isRunning;
        }

        @Override
        public final void setIsRunning(final boolean isRunning) {
            // TODO: Glass pane
            this.isRunning = isRunning;
        }

        @Override
        public AlgorithmParameterSpec getParameterSpec() {
            return null;
        }

    }

}
