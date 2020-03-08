package app.model;

import java.awt.Point;
import java.util.Map;

import javax.swing.SwingUtilities;

import app.MazeApp;
import app.controller.Cell;
import app.controller.Cell.State;

public class Generator {

    public final void generate(final Map<Point, Cell> grid) {
        int i = 0;
        while (i < (((MazeApp) SwingUtilities.getWindowAncestor(grid.values().iterator().next())).getDensity()) * 10) {
            final Cell cell = grid.get(new Point((int) (Math.random() * Math.sqrt(grid.size())), (int) (Math.random() * Math.sqrt(grid.size()))));
            cell.setState(State.OBSTACLE);
            i++;
        }
    }

}
