import java.util.ArrayList;
import utils.Algorithms.Dijkstra;
// import utils.Algorithms.ReverseTraversal;
import utils.DataStructures.Node;
import utils.Parsers.StringSplitter;

public class Solver {

    public static void main(String[] args) {
        ArrayList<int[]> points = StringSplitter.parse(args, "(", ")", ", ");
        int[] start = points.get(0);

        int[][] grid = new int[10][10];
        for (int i = 0; i < points.size(); i++) {
            grid[points.get(i)[0]][points.get(i)[1]] = -(i + 1);
        }

        ArrayList<Node> curr_nodes = new ArrayList<Node>() {
            private static final long serialVersionUID = 1L;
            {
                add(new Node(grid[start[0]][start[1]], start));
            }
        };

        Dijkstra.set_grid(grid);
        Node last_child = Dijkstra.solve(curr_nodes);
        System.out.println(last_child);

        // int[] path = ReverseTraversal.traverse(last);
    }

}
