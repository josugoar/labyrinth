import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MazeApp implements Runnable {

    private static JFrame frame;

    private static final int ROWS = 20;
    private static final int COLS = 20;

    private static int[][] grid = new int[ROWS][COLS];

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
        frame.add(initJPanel());
        // Set JFrame parameters
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        // TEST
        // final Component[] cells = ((JPanel)
        // frame.getContentPane().getComponent(0)).getComponents();
    }

    private JPanel initJPanel() {
        final JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 0, 0));
        // Define individual Cell for each space in GridLayout
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final Cell cell = new Cell(new int[] { row, col });
                cell.addMouseListener(new elementListener(cell));
                panel.add(cell);
            }
        }
        return panel;
    }

    @Override
    public String toString() {
        return String.format("MazeApp(shape: [%d, %d])", ROWS, COLS);
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static int[][] getGrid() {
        return grid;
    }

    private static class Cell extends JPanel {

        private static final long serialVersionUID = 8465814529701152253L;

        private static final int CELL_SIZE = 20;
        private Color color = Color.WHITE;

        private final int[] seed;

        public Cell(final int[] seed) {
            this.seed = seed;
            // Set JPanel parameters
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        }

        @Override
        protected void paintComponent(final Graphics g) {
            // Paint JPanel with given color
            g.setColor(getColor());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        @Override
        public String toString() {
            return String.format("Cell(seed: [%d, %d])", this.seed[0], this.seed[1]);
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

    }

    private static class elementListener extends MouseAdapter {

        private final Cell panel;

        public elementListener(final Cell panel) {
            this.panel = panel;
        }

        @Override
        public void mouseEntered(final MouseEvent event) {
            // Set BLACK if LeftMouseButton is pressed
            if (SwingUtilities.isLeftMouseButton(event)) {
                MazeApp.grid[getPanel().getSeed()[0]][getPanel().getSeed()[1]] = 1;
                // TEST (Remember to delete java.util.Arrays import)
                for (final int[] row : MazeApp.grid) {
                    System.out.println(Arrays.toString(row));
                }
                System.out.println("");
                //
                panel.setColor(Color.BLACK);
                panel.repaint();
                // Set WHITE if LeftMouseButton is pressed
            } else if (SwingUtilities.isRightMouseButton(event)) {
                MazeApp.grid[getPanel().getSeed()[0]][getPanel().getSeed()[1]] = 0;
                // TEST (Remember to delete java.util.Arrays import)
                for (final int[] row : MazeApp.grid) {
                    System.out.println(Arrays.toString(row));
                }
                System.out.println("");
                //
                panel.setColor(Color.WHITE);
                panel.repaint();
            }
        }

        public Cell getPanel() {
            return this.panel;
        }

    }

}
