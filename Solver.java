import java.util.List;

import utils.Algorithms.RecursivePathfinder;
import utils.DataStructures.Element;
import utils.DataStructures.Elements.Empty;
import utils.DataStructures.Elements.End;
import utils.DataStructures.Elements.Node;
import utils.DataStructures.Elements.Obstacle;
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
        Element[][] grid = new Element[shape[0]][shape[1]];
        grid = fill1(grid, null, Empty.class);
        if (points.size() > 2) {
            grid = fill2(grid, points.get(3), Obstacle.class);
        }
        grid[end[0]][end[0]] = new End();

        // Call pathfinding algorithm from starting coordinates
        final Node last_child = RecursivePathfinder.find(grid, points.get(1).get(0));
        System.out.println(last_child);
    }

    /**
     * Fill grid with values
     *
     * @param grid   int[][]
     * @param points ArrayList<int[]>
     * @param empty  int
     * @return grid with inserted points
     */
    public static Element[][] fill1(Element[][] grid, final List<int[]> points, final Class<Empty> cls) {
        if (points != null) {
            for (final int[] point : points) {
                try {
                    grid[point[0]][point[1]] = cls.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[0].length; j++) {
                        grid[i][j] = cls.newInstance();
                    }
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return grid;
    }

    public static Element[][] fill2(Element[][] grid, final List<int[]> points, final Class<Obstacle> cls) {
        if (points != null) {
            for (final int[] point : points) {
                try {
                    grid[point[0]][point[1]] = cls.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Element[] elements : grid) {
                for (Element element : elements) {
                    try {
                        element = cls.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return grid;
    }

}
