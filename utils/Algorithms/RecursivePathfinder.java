package utils.Algorithms;

import java.util.ArrayList;
import utils.DataStructures.Node;

public class RecursivePathfinder {

    private static int[][] grid;
    private static int empty = 0;
    private static int end = -1;

    /**
     * Wrapper for solve method
     *
     * @param new_grid int[][]
     * @param start    int[]
     * @return last node pointing to ending coordinates
     */
    public static Node find(final int[][] new_grid, final int[] start) {
        grid = new_grid;
        return solve(new ArrayList<Node>() {
            private static final long serialVersionUID = 1L;
            {
                add(new Node(null, grid[start[0]][start[1]], start));
            }
        });
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
                    new_coords = new int[] { seed[0]++, seed[1] };
                    break;
                case 1:
                    new_coords = new int[] { seed[0], seed[1]++ };
                    break;
                case 2:
                    new_coords = new int[] { seed[0]--, seed[1] };
                    break;
                default:
                    new_coords = new int[] { seed[0], seed[1]-- };
                }
                // Check for out of bounds coordinates
                if ((Math.abs(new_coords[0]) <= grid.length - 1) && (Math.abs(new_coords[1]) <= grid[0].length - 1)) {
                    int new_val = grid[new_coords[0]][new_coords[1]];
                    final Node new_node = new Node(node, new_val, new_coords);
                    // Check for valid grid values
                    if (new_val == empty) {
                        // Modify empty value
                        new_val = 2;
                        next_nodes.add(new_node);
                    } else if (new_val == end) {
                        return new_node;
                    }
                }
            }
        }
        // Call method recursively until convergence
        return solve(next_nodes);
    }

    public static void set_empty(final int new_empty) {
        empty = new_empty;
    }

    public static void set_end(final int new_end) {
        end = new_end;
    }

}
