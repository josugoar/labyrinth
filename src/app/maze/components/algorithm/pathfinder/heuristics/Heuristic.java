package app.maze.components.algorithm.pathfinder.heuristics;

import java.io.Serializable;
import java.security.spec.AlgorithmParameterSpec;

public interface Heuristic extends AlgorithmParameterSpec, Serializable {

    public abstract int fit();

    public static final class EuclideanDistance implements Heuristic {

        private static final long serialVersionUID = 1L;

        @Override
        public final int fit() {
            return 0;
        }

    }

}
