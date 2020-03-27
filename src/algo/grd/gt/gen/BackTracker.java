package algo.grd.gt.gen;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;

import algo.grd.dsa.AbstractCell;
import algo.grd.dsa.AbstractCell.CellState;
import algo.grd.gt.Generator;

public final class BackTracker<T extends AbstractCell<T>> extends Generator<T> {

    private static final long serialVersionUID = 1L;

    final ArrayList<T> walls = new ArrayList<T>();

    private static final <T extends AbstractCell<T>> T selectRandom(final T[][] grid) {
        int length = 0;
        for (T[] cells : grid) {
            length += cells.length;
        }
        while (true)
            try {
                return grid[(int) (Math.random() * length)][(int) (Math.random() * length)];
            } catch (final IndexOutOfBoundsException e) {
            }
    }

    @Override
    public final void generate() throws InterruptedException {
        for (T[] ts : grid) {
            for (T t : ts) {
                t.setState(CellState.OBSTACLE);
            }
        }
        T cell = BackTracker.selectRandom(this.grid);
        for (T neighbor : cell.getNeighbors())
            walls.add(neighbor);
        do {
            cell = walls.get((int) (Math.random() * walls.size()));
            int count = 0;
            for (T a : cell.getNeighbors())
                if (a.getState() == CellState.OBSTACLE)
                    count++;
            if (count > 2) {
                cell.setState(CellState.EMPTY);
                for (T i : cell.getNeighbors()) {
                    if (i.getState() == CellState.OBSTACLE)
                        walls.add(i);
                }
            }
            Thread.sleep(this.delay);
            walls.remove(cell);
        } while (walls.size() != 0);
    }

    @Override
    public AlgorithmParameterSpec getParameterSpec() {
        return null;
    }

}
