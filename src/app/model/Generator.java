package app.model;

import java.awt.Point;
import java.util.Map;

import app.controller.Cell;
import app.controller.Cell.State;

public class Generator {

    public final void generate(final Map<Point, Cell> grid) {
        int i = 0;
        while (i < grid.size()) {
            final Cell cell = grid.get(new Point((int) (Math.random() * Math.sqrt(grid.size())), (int) (Math.random() * Math.sqrt(grid.size()))));
            cell.setState(State.OBSTACLE);
            i++;
        }
    }

}
