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
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MazeApp implements Runnable {

    private static JFrame frame;

    private static final int ROWS = 20;
    private static final int COLS = 20;

    private static Cell[][] grid = new Cell[ROWS][COLS];

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

    public void initJFrame() {
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

    private JPanel initJPanel() {
        final JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 0, 0));
        // Define individual Cell for each space in GridLayout
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final Cell cell = new Cell(new int[] { row, col });
                cell.addMouseListener(new CellListener(cell));
                panel.add(cell);
            }
        }
        return panel;
    }

    private JButton initJButton() {
        final JButton button = new JButton("Solve");
        // Set JButton parameters
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        // Awake Pathfinder when clicked
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                PathFinder.awake(grid);
            }
        });
        return button;
    }

    private static void draw() {
        for (final Cell[] row : grid) {
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

            // private static final Color color = Color.BLUE;

            private Node parent = null;
            private Cell cell = null;
            private boolean path = false;

            public Node(final Node parent, final Cell cell, final int[] seed) {
                super(seed);
                // this.setColor(color);
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

            // private final Color color = Color.YELLOW;

            public Start(final int[] seed) {
                super(seed);
                // this.setColor(color);
            }

            @Override
            public String toString() {
                return String.format("Cell.Start", 1);
            }

        }

        private final static class EndPoint extends Cell {

            private static final long serialVersionUID = 3183976473041479125L;

            // private final Color color = Color.GREEN;

            public EndPoint(final int[] seed) {
                super(seed);
                // this.setColor(color);
            }

            @Override
            public String toString() {
                return String.format("Cell.EndPoint", 1);
            }

        }

        private final static class Obstacle extends Cell {

            private static final long serialVersionUID = 1L;

            // private final Color color = Color.BLACK;

            public Obstacle(final int[] seed) {
                super(seed);
                // this.setColor(color);
            }

            @Override
            public String toString() {
                return String.format("Cell.Obstacle", 1);
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
                if (MazeApp.grid[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] instanceof Cell.Start) {
                    PathFinder.setStart(null);
                    reset();
                } else {
                    PathFinder.setStart(this.getPanel().getSeed());
                    MazeApp.grid[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] = new Cell.Start(
                            this.getPanel().getSeed());
                    paint(Color.YELLOW);
                }
                // Set GREEN if RightMouseButton is clicked
            } else if (SwingUtilities.isRightMouseButton(event)) {
                if (MazeApp.grid[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] instanceof Cell.EndPoint) {
                    reset();
                } else {
                    MazeApp.grid[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] = new Cell.EndPoint(
                            this.getPanel().getSeed());
                    paint(Color.GREEN);
                }
            }
        }

        @Override
        public void mouseEntered(final MouseEvent event) {
            // Set BLACK if LeftMouseButton is dragged
            if (SwingUtilities.isLeftMouseButton(event)) {
                MazeApp.grid[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] = new Cell.Obstacle(
                        this.getPanel().getSeed());
                paint(Color.BLACK);
                // Set WHITE if RightMouseButton is dragged
            } else if (SwingUtilities.isRightMouseButton(event)) {
                reset();
            }
        }

        private void paint(final Color color) {
            this.getPanel().setColor(color);
            this.getPanel().repaint();
        }

        private void reset() {
            MazeApp.grid[this.getPanel().getSeed()[0]][this.getPanel().getSeed()[1]] = null;
            paint(Color.WHITE);
        }

        public Cell getPanel() {
            return this.panel;
        }

        public void setPanel(final Cell panel) {
            this.panel = panel;
        }

    }

    private static class PathFinder {

        private static int[] start = null;

        public static Cell.Node awake(final Cell[][] grid) {
            try {
                return find(grid, new ArrayList<Cell.Node>() {
                    private static final long serialVersionUID = 1L;
                    {
                        add(new Cell.Node(null, grid[getStart()[0]][getStart()[1]], getStart()));
                    }
                });
            } catch (final StackOverflowError e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        private static Cell.Node find(final Cell[][] grid, final ArrayList<Cell.Node> curr_nodes)
                throws StackOverflowError {
            final ArrayList<Cell.Node> new_nodes = new ArrayList<Cell.Node>();
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
            // Display array
            MazeApp.draw();
            // Call method recursively until convergence
            return find(grid, new_nodes);
        }

        public static int[] getStart() {
            return start;
        }

        public static void setStart(final int[] new_start) {
            start = new_start;
        }

    }

}
