import java.util.List;

import utils.Algorithms.RecursivePathfinder;
import utils.DataStructures.Element;
import utils.DataStructures.Elements.*;
import utils.Parsers.StringSplitter;

// String[0]:               shape
// String[1]:               start
// String[2]:               end
// String[3] (optional):    obstacles

public class Solver {

    public static void main(final String[] args) {
        // Parse command line arguments into integer arrays
        final List<List<int[]>> points = StringSplitter.parse(args);
        // Initialize grid
        final Element[][] grid = initialize(points);
        // Call pathfinding algorithm from starting coordinates
        final Node last_child = RecursivePathfinder.awake(grid, points.get(1).get(0));

        System.out.println(last_child);
    }

    /**
     * Initilize grid and fill it with objects
     *
     * @param points List<List<int[]>>
     * @return grid with inserted objects
     */
    public static Element[][] initialize(final List<List<int[]>> points) {
        // Include empty objects
        final int[] shape = points.get(0).get(0);
        final Element[][] grid = new Element[shape[0]][shape[1]];
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                grid[i][j] = new Empty();
            }
        }
        // Include obstacle objects
        try {
            for (final int[] obstacle : points.get(3)) {
                grid[obstacle[0]][obstacle[1]] = new Obstacle();
            }
        } catch (final Exception e) {
            System.out.println("No obstacles initialized");
        }
        // Include end objects
        for (final int[] end : points.get(2)) {
            grid[end[0]][end[0]] = new End();
        }
        // Include start objects
        final int[] start = points.get(1).get(0);
        grid[start[0]][start[1]] = new Start();
        return grid;
    }

}
