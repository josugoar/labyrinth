package utils.Algorithms;

import java.util.ArrayList;
import utils.DataStructures.Node;

public class Dijkstra {

    private static int[][] grid;
    private static int empty = 0;
    private static int end = -2;

    public static Node solve(final ArrayList<Node> curr_nodes) {
        final ArrayList<Node> next_nodes = new ArrayList<Node>();
        for (final Node node : curr_nodes) {
            int[] new_coords;
            final int[] seed = node.get_seed();
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
                if ((Math.abs(new_coords[0]) <= grid.length - 1) && (Math.abs(new_coords[1]) <= grid[0].length - 1)) {
                    int new_val = grid[new_coords[0]][new_coords[1]];
                    final Node new_node = new Node(node, new_val, new_coords);
                    if (new_val == empty) {
                        new_val = 2;
                        next_nodes.add(new_node);
                    } else if (new_val == end) {
                        return new_node;
                    }
                }
            }
        }
        return solve(next_nodes);
    }

    public static void set_grid(final int[][] new_grid) {
        grid = new_grid;
    }

    public static int[][] get_grid() {
        return grid;
    }

    public static void set_empty(final int new_empty) {
        empty = new_empty;
    }

    public static void set_end(final int new_end) {
        end = new_end;
    }

}
