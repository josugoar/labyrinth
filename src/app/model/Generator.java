package app.model;

import java.security.spec.AlgorithmParameterSpec;

import app.controller.components.AbstractAlgorithm;
import app.controller.components.AbstractCell;
import app.model.components.CellPanel;

public abstract class Generator implements AbstractAlgorithm {

    private static final long serialVersionUID = 1L;

    protected boolean isRunning = false;

    protected final void awake(final CellPanel[][] grid) {
    }

    @Override
    public final String getAlgorithm() {
        return this.getClass().getSimpleName();
    }

    public static final class BackTracker extends Generator {

        private static final long serialVersionUID = 1L;

        @Override
        public <T extends AbstractCell<T>> void awake(final T[][] grid) {
        }

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
