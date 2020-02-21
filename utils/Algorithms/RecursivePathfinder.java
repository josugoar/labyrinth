package utils.Algorithms;

import java.util.ArrayList;

import utils.DataStructures.Element;
import utils.DataStructures.Elements.Empty;
import utils.DataStructures.Elements.End;
import utils.DataStructures.Elements.Node;
import utils.DataStructures.Elements.Start;
import utils.Parsers.ArrayDisplayer;

public class RecursivePathfinder {

    private static Element[][] grid;

    /**
     * Wrapper for solve method
     *
     * @param new_grid int[][]
     * @param start    int[]
     * @return last node pointing to ending coordinates
     */
    public static Node find(final Element[][] new_grid, final int[] start) {
        new_grid[start[0]][start[1]] = new Start();
        grid = new_grid;
        final long start_time = System.currentTimeMillis();
        final Node last_child = solve(new ArrayList<Node>() {
            private static final long serialVersionUID = 1L;
            {
                add(new Node(null, grid[start[0]][start[1]], start));
            }
        });
        final long end_time = System.currentTimeMillis();
        System.out.println("Elapsed time in miliseconds: " + (end_time - start_time));
        return last_child;
    }

    /**
     * Finds last node following shortest path
     *
     * @param curr_nodes ArrayList<Node>
     * @return last node pointing to ending coordinates
     */
    public static Node solve(final ArrayList<Node> curr_nodes) {
        final ArrayList<Node> next_nodes = new ArrayList<Node>();
        for (final Node node : curr_nodes) {
            int[] new_coords;
            final int[] seed = node.get_seed();
            // Generate 4 children from parent node seed
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case 0:
                        new_coords = new int[] { seed[0] + 1, seed[1] };
                        break;
                    case 1:
                        new_coords = new int[] { seed[0], seed[1] + 1 };
                        break;
                    case 2:
                        new_coords = new int[] { seed[0] - 1, seed[1] };
                        break;
                    default:
                        new_coords = new int[] { seed[0], seed[1] - 1 };
                }
                // Check for out of bounds coordinates
                if ((Math.abs(new_coords[0]) <= grid.length - 1) && (Math.abs(new_coords[1]) <= grid[0].length - 1)
                        && (new_coords[0] >= 0) && (new_coords[1] >= 0)) {
                    final Element new_val = grid[new_coords[0]][new_coords[1]];
                    final Node new_node = new Node(node, new_val, new_coords);
                    // Check for valid grid values
                    if (new_val instanceof Empty) {
                        // Modify empty value
                        grid[new_coords[0]][new_coords[1]] = new_node;
                        next_nodes.add(new_node);
                    } else if (new_val instanceof End) {
                        return new_node;
                    }
                }
            }
        }
        // Display array
        ArrayDisplayer.plot(grid);
        // Call method recursively until convergence
        return solve(next_nodes);
    }

}
