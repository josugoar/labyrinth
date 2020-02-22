import utils.DataStructures.Element;
import utils.DataStructures.Elements.*;

public class Maze {

    private Element[][] grid;
    private int[] start, end;
    private int[][] obstacles = null;

    public static void main(String[] args) {
        Maze maze = new Maze(new int[] { 10, 10 }, new int[] { 0, 0 }, new int[] { 9, 9 });
        maze.generate(1);
        System.out.println(maze);
    }

    public Maze(int[] shape, int[] start, int[] end) {
        this.grid = new Element[shape[0]][shape[1]];
        setStart(start);
        setEnd(end);
    }

    @Override
    public String toString() {
        return String.format("Maze(start[%d, %d], end[%d, %d])", start[0], start[1], end[0], end[1]);
    }

    public void populate() {
        // if (start != null && end != null) {
        for (int[] obstacle : obstacles) {
            if (this.grid[obstacle[0]][obstacle[1]] == null) {
                this.grid[obstacle[0]][obstacle[1]] = new Obstacle();
            }
        }
        // }
    }

    public void generate(float density) {
        // TODO: random obstacle generation
    }

    public Element[][] getGrid() {
        return grid;
    }

    public void setGrid(Element[][] grid) {
        this.grid = grid;
    }

    public int[] getStart() {
        return start;
    }

    public void setStart(int[] start) {
        this.start = start;
    }

    public int[] getEnd() {
        return end;
    }

    public void setEnd(int[] end) {
        this.end = end;
    }

    public int[][] getObstacles() {
        return obstacles;
    }

    public void setObstacles(int[][] obstacles) {
        this.obstacles = obstacles;
    }

}
