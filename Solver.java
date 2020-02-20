import java.util.ArrayList;
import utils.Algorithms.Dijkstra;
import utils.DataStructures.Node;
import utils.Parsers.StringSplitter;

public class Solver {

    // TODO: Improve Node toString method (show seed values)
    // TODO: Generalize search by number of dimensions

    public static void main(final String[] args) {
        final ArrayList<int[]> points = StringSplitter.parse(args, "(", ")", ", ");

        final int[][] grid = new int[10][10];
        for (int i = 0; i < points.size(); i++) {
            final int[] point = points.get(i);
            if (i < 2) {
                grid[point[0]][point[1]] = -(i + 1);
            } else {
                grid[point[0]][point[1]] = 1;
            }
        }

        final int[] start = points.get(0);
        final ArrayList<Node> curr_nodes = new ArrayList<Node>() {
            private static final long serialVersionUID = 1L;
            {
                add(new Node(null, grid[start[0]][start[1]], start));
            }
        };

        Dijkstra.set_grid(grid);
        final Node last_child = Dijkstra.solve(curr_nodes);

        System.out.println(last_child);
    }

}
