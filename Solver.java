import java.util.List;

import utils.Algorithms.RecursivePathfinder;
import utils.DataStructures.Element;
import utils.DataStructures.Elements.*;
import utils.Parsers.StringSplitter;

// String[0]:               shape
// String[1]:               start
// String[2]:               end
// String[3] (optional):    obstacles

final class Solver {

    public static void main(final String[] args) {
        // Parse arguments into integer arrays
        final List<List<int[]>> points = StringSplitter.parse(args);
        // Initialize grid with elements in points coordinates
        final Element[][] grid = initialize(points);
        // Call pathfinding algorithm from starting coordinates
        final Node last_child = RecursivePathfinder.awake(grid, points.get(1).get(0), true);

        // Print last child
        if (last_child != null) {
            System.out.println(last_child);
        }
    }

    /**
     * Initilizes grid and fills it with elements
     *
     * @param points List<List<int[]>>
     * @return grid with inserted elements
     */
    public static Element[][] initialize(final List<List<int[]>> points) {
        // Initialize grid with given shape
        final int[] shape = points.get(0).get(0);
        final Element[][] grid = new Element[shape[0]][shape[1]];
        // Include empty elements
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                grid[i][j] = new Empty();
            }
        }
        // Include obstacle elements
        try {
            for (final int[] obstacle : points.get(3)) {
                grid[obstacle[0]][obstacle[1]] = new Obstacle();
            }
        } catch (final Exception e) {
            System.out.println("No obstacles initialized");
        }
        // Include end elements
        for (final int[] end : points.get(2)) {
            grid[end[0]][end[0]] = new End();
        }
        // Include start elements
        final int[] start = points.get(1).get(0);
        grid[start[0]][start[1]] = new Start();
        return grid;
    }

}
