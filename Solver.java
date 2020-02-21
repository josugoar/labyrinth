import java.util.List;

import utils.Algorithms.RecursivePathfinder;
import utils.DataStructures.Node;
import utils.Parsers.StringSplitter;

// String[0]: shape
// String[1]: start
// String[2]: end
// String[3] (optional): obstacles
public class Solver {

    // TODO: Add element inheritance
    // Each element is a class which inherits from parent Element
    // Empty: empty cell (0)
    // Obstacle: obstacle cell (1)
    // Pickup: cell that must be accessed before reaching end
    // End: end cell

    public static void main(final String[] args) {
        // Parse command line argument into integer arrays
        final List<List<int[]>> points = StringSplitter.parse(args);

        // Get relevant points
        final int[] shape = points.get(0).get(0);
        final int[] end = points.get(2).get(0);

        // Initialize grid and fill it with values
        int[][] grid = new int[shape[0]][shape[1]];
        if (points.size() > 2) {
            grid = fill(grid, points.get(3), 1);
        }
        grid[end[0]][end[0]] = -1;

        // Call pathfinding algorithm from starting coordinates
        final Node last_child = RecursivePathfinder.find(grid, points.get(1).get(0));
        System.out.println(last_child);
    }

    /**
     * Fill grid with values
     *
     * @param grid   int[][]
     * @param points ArrayList<int[]>
     * @param cell   int
     * @return grid with inserted points
     */
    public static int[][] fill(int[][] grid, final List<int[]> points, final int cell) {
        for (final int[] point : points) {
            grid[point[0]][point[1]] = cell;
        }
        return grid;
    }

}
