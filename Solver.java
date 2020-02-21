import java.util.List;

import utils.Algorithms.RecursivePathfinder;
import utils.DataStructures.Element;
import utils.DataStructures.Elements.Empty;
import utils.DataStructures.Elements.End;
import utils.DataStructures.Elements.Node;
import utils.DataStructures.Elements.Obstacle;
import utils.Parsers.StringSplitter;

// String[0]:               shape
// String[1]:               start
// String[2]:               end
// String[3] (optional):    obstacles

public class Solver {

    // TODO: Add element inheritance
    // Each element is a class which inherits from parent Element
    // Empty: empty cell (0)
    // Obstacle: obstacle cell (1)
    // Pickup: cell that must be accessed before reaching end
    // End: end cell

    public static void main(final String[] args) {
        // Parse command line arguments into integer arrays
        final List<List<int[]>> points = StringSplitter.parse(args);

        // Initialize grid
        final Element[][] grid = initialize(points);

        // Call pathfinding algorithm from starting coordinates
        final Node last_child = RecursivePathfinder.find(grid, points.get(1).get(0));

        System.out.println(last_child);
    }

    /**
     * Initilize grid and fill it with objects
     *
     * @param grid   Element[][]
     * @param points List<int[]>
     * @param cls    Element
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
        for (final int[] point : points.get(3)) {
            grid[point[0]][point[1]] = new Obstacle();
        }
        // Include end objects
        final int[] end = points.get(2).get(0);
        grid[end[0]][end[0]] = new End();
        return grid;
    }

}
