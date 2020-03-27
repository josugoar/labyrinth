package algo.grd.gt.gen;

import java.security.spec.AlgorithmParameterSpec;

import algo.grd.dsa.AbstractCell;
import algo.grd.dsa.AbstractCell.CellState;
import algo.grd.gt.Generator;

public final class Randomizer<T extends AbstractCell<T>> extends Generator<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public final void generate() throws InterruptedException {
        for (final T[] cells : grid)
            for (final T cell : cells) {
                this.assertWaiting();
                if (!this.running)
                    throw new InterruptedException("Invokation interrupted...");
                if (Math.random() < (float) super.density / 100)
                    cell.setState(CellState.OBSTACLE);
                Thread.sleep(super.delay);
            }
    }

    @Override
    public AlgorithmParameterSpec getParameterSpec() {
        return null;
    }

}
