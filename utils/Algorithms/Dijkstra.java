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
            int[] coords;
            int[] seed = node.get_seed();
            for (int i = 0; i < 4; i++) {
                switch (i) {
                case 0:
                    coords = new int[] { seed[0]++, seed[1] };
                    break;
                case 1:
                    coords = new int[] { seed[0], seed[1]++ };
                    break;
                case 2:
                    coords = new int[] { seed[0]--, seed[1] };
                    break;
                default:
                    coords = new int[] { seed[0], seed[1]-- };
                }
                if ((Math.abs(coords[0]) <= grid.length) && (Math.abs(coords[1]) <= grid[0].length)) {
                    final Node new_node = new Node(node, grid[coords[0]][coords[1]], coords);
                    if (grid[coords[0]][coords[1]] == empty) {
                        grid[coords[0]][coords[1]] = 2;
                        next_nodes.add(new_node);
                    } else if (grid[coords[0]][coords[1]] == end) {
                        return new_node;
                    }
                }
            }
        }
        return solve(next_nodes);
    }

    public static void set_grid(int[][] new_grid) {
        grid = new_grid;
    }

    public static int[][] get_grid() {
        return grid;
    }

    public static void set_empty(int new_empty) {
        empty = new_empty;
    }

    public static int get_empty() {
        return empty;
    }

    public static void set_end(int new_end) {
        end = new_end;
    }

    public static int get_end() {
        return end;
    }

}
