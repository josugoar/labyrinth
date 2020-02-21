package utils.Algorithms;

import java.util.ArrayList;

import utils.DataStructures.Element;
import utils.DataStructures.Elements.*;
import utils.Parsers.ArrayDisplayer;

public class RecursivePathfinder {

    /**
     * Wrapper for solve method
     *
     * @param grid  Element[][]
     * @param start int[]
     * @return last child node pointing to endpoint
     */
    public static Node awake(final Element[][] grid, final int[] start) {
        // Time method call
        final long start_time = System.currentTimeMillis();
        final Node last_child = find(grid, new ArrayList<Node>() {
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
     * Find endpoint following shortest path
     *
     * @param grid       Element[][]
     * @param curr_nodes ArrayList<Node>
     * @return last child node pointing to endpoint
     */
    private static Node find(final Element[][] grid, final ArrayList<Node> curr_nodes) {
        final ArrayList<Node> next_nodes = new ArrayList<Node>();
        for (final Node node : curr_nodes) {
            // Generate neighbor children from parent node seed
            final int[] seed = node.get_seed();
            for (int row = seed[0] - 1; row < seed[0] + 2; row++) {
                for (int col = seed[1] - 1; col < seed[1] + 2; col++) {
                    final int[] new_coords = { row, col };
                    // Check for out of bounds coordinates
                    if ((new_coords[0] < grid.length) && (new_coords[0] >= 0) && (new_coords[1] < grid[0].length)
                            && (new_coords[1] >= 0)) {
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
        }
        // Display array
        ArrayDisplayer.plot(grid);
        // Call method recursively until convergence
        return find(grid, next_nodes);
    }
}
