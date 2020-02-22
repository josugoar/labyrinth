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
        // TODO: Add CL methods HERE
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
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
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
                try {
                    PathFinder.awake(grid, new int[] { 0, 0 });
                } catch (StackOverflowError | InterruptedException e1) {
                    e1.printStackTrace();
                }
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
        // TODO: Elements inherit from Cell

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

            private final Color color = Color.BLUE;

            private Node parent = null;
            private Cell cell = null;
            private boolean path = false;

            public Node(final Node parent, final Cell cell, final int[] seed) {
                super(seed);
                this.setColor(Color.BLUE);
                this.parent = parent;
                this.cell = cell;
            }

            @Override
            public String toString() {
                if (parent == null) {
                    return String.format("Node(parent: null, cell: %s, seed: [%d, %d], path: %b)", this.getCell(),
                            this.getSeed()[0], this.getSeed()[1], this.getPath());
                } else {
                    return String.format("Node(parent: [%d, %d], cell: %s, seed: [%d, %d], path: %b)",
                            this.getParent().getSeed()[0], this.getParent().getSeed()[1], this.getCell(),
                            this.getSeed()[0], this.getSeed()[1], this.getPath());

                }

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

        private final static class EndPoint extends Cell {

            private static final long serialVersionUID = 3183976473041479125L;

            public EndPoint(final int[] seed) {
                super(seed);
            }

        }

        private final static class Obstacle extends Cell {

            private static final long serialVersionUID = 1L;

            public Obstacle(final int[] seed) {
                super(seed);
            }

        }

    }

    private static class CellListener extends MouseAdapter {

        private final Cell panel;

        public CellListener(final Cell panel) {
            this.panel = panel;
        }

        @Override
        public void mouseEntered(final MouseEvent event) {
            // Set BLACK if LeftMouseButton is pressed
            if (SwingUtilities.isLeftMouseButton(event)) {
                MazeApp.grid[getPanel().getSeed()[0]][getPanel().getSeed()[1]] = new Cell.Obstacle(
                        getPanel().getSeed());
                panel.setColor(Color.BLACK);
                panel.repaint();
                // Set WHITE if LeftMouseButton is pressed
            } else if (SwingUtilities.isRightMouseButton(event)) {
                MazeApp.grid[getPanel().getSeed()[0]][getPanel().getSeed()[1]] = null;
                panel.setColor(Color.WHITE);
                panel.repaint();
            }
        }

        public Cell getPanel() {
            return this.panel;
        }

    }

    private static class PathFinder {

        public static Cell.Node awake(final Cell[][] grid, final int[] start)
                throws StackOverflowError, InterruptedException {
            try {
                return find(grid, new ArrayList<Cell.Node>() {
                    private static final long serialVersionUID = 1L;
                    {
                        add(new Cell.Node(null, grid[start[0]][start[1]], start));
                    }
                });
            } catch (final StackOverflowError e1) {
                System.out.println(e1.getMessage());
                return null;
            }
        }

        private static Cell.Node find(final Cell[][] grid, final ArrayList<Cell.Node> curr_nodes)
                throws StackOverflowError, InterruptedException {
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
            Thread.sleep(500);
            // Call method recursively until convergence
            return find(grid, new_nodes);
        }

    }

}
