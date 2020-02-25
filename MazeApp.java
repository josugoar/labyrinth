import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MazeApp implements Runnable {

    // TODO: Add Javadocs
    // TODO: Change ROWS and COLS to FRAME_SIZE (N*N)
    // TODO: Store coordinates in java.awt.Point, not int[]
    // TODO: Create Grid object
        // TODO: Grid has layout (ListedHashMap/TreeMap<Point, Cell>), start (ListedHashMap/TreeMap<String, Cell> of algorithm type) and end (ArrayList<Cell>)
    // TODO: Convert PathFinder into abstract class and add Dijkstra and AStar inherintance
        // TODO: PathFinder has grid and start atributes, when grid is modified start is reseted
        // TODO: Strore grid layout and relevant points (start, end ...) in Grid object
    // TODO: Convert Cell into superclass
        // TODO: Include multiple PathFinderListener for each Cell.Star
        // TODO: Convert CELL_SIZE to inline constant

    private static final int ROWS = 10;
    private static final int COLS = 10;

    protected static JFrame frame;
    protected static Cell[][] grid = new Cell[10][10];
    protected static LinkedHashMap<String, Cell> cellMap = new LinkedHashMap<String, Cell>();

    public static void main(final String[] args) {
        // Invoke MazeApp asynchronously in event dispatch thread
        final MazeApp maze = new MazeApp();
        EventQueue.invokeLater(maze);
        System.out.println("Running...");
    }

    @Override
    public void run() {
        initJFrame();
    }

    private static void initJFrame() {
        frame = new JFrame("MazeApp");
        // Add JFrame components
        frame.add(initJPanel(), BorderLayout.NORTH);
        frame.add(initJButton(), BorderLayout.SOUTH);
        // Set JFrame parameters
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel initJPanel() {
        final JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 0, 0));
        // Define individual Cell with personal CellListener and cellMap pointer for
        // each space in GridLayout
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final Cell cell = new Cell(new int[] { row, col });
                cell.addMouseListener(new CellListener(cell));
                panel.add(cell);
                getCellMap().put(Arrays.toString(cell.getSeed()), cell);
            }
        }
        return panel;
    }

    private static JButton initJButton() {
        final JButton button = new JButton("Solve");
        // Set JButton parameters
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        // Awake Pathfinder when clicked
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                PathFinder.awake();
            }
        });
        return button;
    }

    private static void paintPanel(final Cell panel, final Color color) {
        panel.setColor(color);
        panel.repaint();
    }

    private static void resetPanel(final Cell panel) {
        getGrid()[panel.getSeed()[0]][panel.getSeed()[1]] = null;
        paintPanel(panel, Color.WHITE);
    }

    private static void drawNode(final List<Cell.Node> gen, final int delay) {
        for (final Cell.Node node : gen) {
            new Timer(delay, e -> {
                paintPanel(getCellMap().get(Arrays.toString(node.getSeed())), Color.BLUE);
                ((Timer) e.getSource()).stop();
            }).start();
        }
    }

    private static void drawTerminal() {
        // Draw grid representation in terminal
        for (final Cell[] row : getGrid()) {
            for (final Cell cell : row) {
                if (cell == null) {
                    System.out.printf("-");
                } else if (cell instanceof Cell.Node) {
                    if (((Cell.Node) cell).getPath()) {
                        System.out.printf("^");
                    } else {
                        System.out.printf("*");
                    }
                } else if (cell instanceof Cell.Obstacle) {
                    System.out.printf("#");
                } else {
                    System.out.printf("0");
                }
                System.out.printf(" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    @Override
    public String toString() {
        return String.format("MazeApp(shape: [%d, %d])", ROWS, COLS);
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static Cell[][] getGrid() {
        return grid;
    }

    public static LinkedHashMap<String, Cell> getCellMap() {
        return cellMap;
    }

    private static class Cell extends JPanel {

        private static final long serialVersionUID = 3179763942246403693L;

        protected static final int CELL_SIZE = 20;

        protected Color color = Color.WHITE;
        protected final int[] seed;

        public Cell(final int[] seed) {
            this.seed = seed;
            // Set JPanel parameters
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        }

        @Override
        protected void paintComponent(final Graphics g) {
            // Paint JPanel with given color
            g.setColor(this.getColor());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        @Override
        public String toString() {
            return String.format("Cell(seed: [%d, %d])", this.getSeed()[0], this.getSeed()[1]);
        }

        public Color getColor() {
            return this.color;
        }

        public void setColor(final Color color) {
            this.color = color;
        }

        public int[] getSeed() {
            return this.seed;
        }

        private final static class Node extends Cell {

            private static final long serialVersionUID = -2199315976522190756L;

            private Node parent = null;
            private Cell cell = null;
            private boolean path = false;

            public Node(final Node parent, final Cell cell, final int[] seed) {
                super(seed);
                this.parent = parent;
                this.cell = cell;
            }

            @Override
            public String toString() {
                return String.format("Cell.Node(seed: [%d, %d], cell: %s,  path: %b)", this.getSeed()[0],
                        this.getSeed()[1], this.getCell(), this.getPath());
            }

            public Node getParent() {
                return this.parent;
            }

            public Cell getCell() {
                return this.cell;
            }

            public int[] getSeed() {
                return this.seed;
            }

            public boolean getPath() {
                return this.path;
            }

            public void setPath(final boolean path) {
                this.path = path;
            }

        }

        private final static class Start extends Cell {

            static final long serialVersionUID = 1L;

            public Start(final int[] seed) {
                super(seed);
            }

            @Override
            public String toString() {
                return String.format("Cell.Start", 0);
            }

        }

        private final static class EndPoint extends Cell {

            private static final long serialVersionUID = 3183976473041479125L;

            public EndPoint(final int[] seed) {
                super(seed);
            }

            @Override
            public String toString() {
                return String.format("Cell.EndPoint", 0);
            }

        }

        private final static class Obstacle extends Cell {

            private static final long serialVersionUID = 1L;

            public Obstacle(final int[] seed) {
                super(seed);
            }

            @Override
            public String toString() {
                return String.format("Cell.Obstacle", 0);
            }

        }

    }

    private static class CellListener extends MouseAdapter {

        private Cell panel;

        public CellListener(final Cell panel) {
            this.setPanel(panel);
        }

        @Override
        public void mouseClicked(final MouseEvent event) {
            // Set YELLOW if LeftMouseButton is clicked
            if (SwingUtilities.isLeftMouseButton(event)) {
                // Delete Start
                if (MazeApp.getGrid()[this.getPanel().getSeed()[0]][this.getPanel()
                        .getSeed()[1]] instanceof Cell.Start) {
                    PathFinder.setStart(null);
                    resetPanel(this.getPanel());
                } else {
                    // Override Start
                    if (PathFinder.getStart() != null) {
                        if (MazeApp.getGrid()[PathFinder.getStart()[0]][PathFinder
                                .getStart()[1]] instanceof Cell.Start) {
                            resetPanel(MazeApp.getCellMap().get(Arrays.toString(PathFinder.getStart())));
                        }
                    }
                    // Set new Start
                    PathFinder.setStart(this.getPanel().getSeed());
                    MazeApp.getGrid()[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] = new Cell.Start(
                            this.getPanel().getSeed());
                    paintPanel(this.getPanel(), Color.YELLOW);
                }
                // Set GREEN if RightMouseButton is clicked
            } else if (SwingUtilities.isRightMouseButton(event)) {
                // Delete EndPoint
                if (MazeApp.getGrid()[this.getPanel().getSeed()[0]][this.getPanel()
                        .getSeed()[1]] instanceof Cell.EndPoint) {
                    resetPanel(this.getPanel());
                    // Set new EndPoint
                } else {
                    MazeApp.getGrid()[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] = new Cell.EndPoint(
                            this.getPanel().getSeed());
                    paintPanel(this.getPanel(), Color.GREEN);
                }
            }
        }

        @Override
        public void mouseEntered(final MouseEvent event) {
            // Set BLACK if LeftMouseButton is dragged
            if (SwingUtilities.isLeftMouseButton(event)) {
                MazeApp.getGrid()[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] = new Cell.Obstacle(
                        this.getPanel().getSeed());
                paintPanel(this.getPanel(), Color.BLACK);
                // Set WHITE if RightMouseButton is dragged
            } else if (SwingUtilities.isRightMouseButton(event)) {
                resetPanel(this.getPanel());
            }
        }

        public Cell getPanel() {
            return this.panel;
        }

        public void setPanel(final Cell panel) {
            this.panel = panel;
        }

    }

    private static class PathFinder {

        private static int DELAY_N = 200;

        private static int delay_n = 0;
        private static int[] start = null;

        public static Cell.Node awake() {
            try {
                // Find EndPoint from Start Node
                final Cell.Node lastChild = find(MazeApp.getGrid(), new ArrayList<Cell.Node>() {
                    private static final long serialVersionUID = 1L;
                    {
                        add(new Cell.Node(null, MazeApp.getGrid()[getStart()[0]][getStart()[1]], getStart()));
                    }
                });
                traverse(MazeApp.getGrid(), lastChild.getParent());
                MazeApp.drawTerminal();
                return lastChild;
                // Catch no solution grid error
            } catch (final StackOverflowError e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        private static Cell.Node find(final Cell[][] grid, final List<Cell.Node> curr_nodes) throws StackOverflowError {
            final List<Cell.Node> new_nodes = new ArrayList<Cell.Node>();
            for (final Cell.Node node : curr_nodes) {
                // Generate neighbor children from parent node seed
                final int[] seed = node.getSeed();
                for (int row = seed[0] - 1; row < seed[0] + 2; row++) {
                    for (int col = seed[1] - 1; col < seed[1] + 2; col++) {
                        final int[] new_coords = { row, col };
                        // Check for out of bounds coordinates
                        if ((new_coords[0] < grid.length) && (new_coords[0] >= 0) && (new_coords[1] < grid[0].length)
                                && (new_coords[1] >= 0)) {
                            final Cell new_val = grid[new_coords[0]][new_coords[1]];
                            final Cell.Node new_node = new Cell.Node(node, new_val, new_coords);
                            // Check for valid grid values
                            if (new_val == null) {
                                // Modify empty value
                                grid[new_coords[0]][new_coords[1]] = new_node;
                                new_nodes.add(new_node);
                            } else if (new_val instanceof Cell.EndPoint) {
                                return new_node;
                            }
                        }
                    }
                }
            }
            // Handle no solution grid
            if (curr_nodes.equals(new_nodes)) {
                throw new StackOverflowError("No solution...");
            }
            // Display generation
            MazeApp.drawNode(new_nodes, delay_n++ * DELAY_N);
            // Call method recursively until convergence
            return find(grid, new_nodes);
        }

        private static Cell.Node traverse(final Cell[][] grid, final Cell.Node child) {
            // Inversely traverse path
            if (child.getParent() != null) {
                new Timer(delay_n++ * DELAY_N, e -> {
                    MazeApp.paintPanel(MazeApp.getCellMap().get(Arrays.toString(child.getSeed())), Color.CYAN);
                    ((Timer) e.getSource()).stop();
                }).start();
                child.setPath(true);
                return traverse(grid, child.getParent());
            } else {
                return child;
            }
        }

        public static int[] getStart() {
            return start;
        }

        public static void setStart(final int[] new_start) {
            start = new_start;
        }

    }

}
