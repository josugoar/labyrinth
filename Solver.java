import java.util.ArrayList;
import utils.Algorithms.RecursiveDijkstra;
import utils.DataStructures.Node;
import utils.Parsers.StringSplitter;

public class Solver {

    public static void main(final String[] args) {
        final ArrayList<int[]> points = StringSplitter.parse(args, "(", ")", ", ");
        final int[][] grid = put_points(new int[10][10], points);

        final int[] start = points.get(0);
        final ArrayList<Node> curr_nodes = new ArrayList<Node>() {
            private static final long serialVersionUID = 1L;
            {
                add(new Node(null, grid[start[0]][start[1]], start));
            }
        };

        RecursiveDijkstra.set_grid(grid);
        final Node last_child = RecursiveDijkstra.solve(curr_nodes);

        System.out.println(last_child);
    }

    public static int[][] put_points(int[][] grid, ArrayList<int[]> points) {
        for (int i = 0; i < points.size(); i++) {
            final int[] point = points.get(i);
            if (i < 2) {
                grid[point[0]][point[1]] = -(i + 1);
            } else {
                grid[point[0]][point[1]] = 1;
            }
        }
        return grid;
    }

}
