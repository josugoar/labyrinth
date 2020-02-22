import java.awt.Color;
import java.awt.Component;
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

    private static final int ROWS = 20;
    private static final int COLS = 20;
    private static int[][] grid = new int[ROWS][COLS];

    public static void main(final String[] args) {
        // Invoke MazeApp asynchronously in event dispatch thread
        final MazeApp maze = new MazeApp();
        EventQueue.invokeLater(maze);
        System.out.println("Running...");
    }

    @Override
    public String toString() {
        return String.format("MazeApp(shape: [%d, %d])", ROWS, COLS);
    }

    @Override
    public void run() {
        initJFrame();
    }

    public void initJFrame() {
        final JFrame frame = new JFrame("MazeApp");
        frame.add(initJPanel());
        // Set JFrame parameters
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        final Component[] cells = ((JPanel) frame.getContentPane().getComponent(0)).getComponents();
    }

    private JPanel initJPanel() {
        final JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 0, 0));
        // Define individual Cell for each space in GridLayout
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final Cell cell = new Cell();
                cell.addMouseListener(new elementListener(cell, new int[] { row, col }));
                panel.add(cell);
            }
        }
        return panel;
    }

    private static class Cell extends JPanel {

        private static final long serialVersionUID = 8465814529701152253L;
        private static final int CELL_SIZE = 20;
        private Color color = Color.WHITE;

        public Cell() {
            // Set JPanel parameters
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        }

        public Color getColor() {
            return color;
        }

        public void setColor(final Color color) {
            this.color = color;
        }

        @Override
        protected void paintComponent(final Graphics g) {
            // Paint JPanel with given color
            g.setColor(getColor());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private static class elementListener extends MouseAdapter {

        private final Cell panel;
        private final int[] seed;

        public elementListener(final Cell panel, final int[] seed) {
            this.panel = panel;
            this.seed = seed;
        }

        @Override
        public void mouseEntered(final MouseEvent event) {
            // Set BLACK if LeftMouseButton is pressed
            if (SwingUtilities.isLeftMouseButton(event)) {
                MazeApp.grid[this.seed[0]][this.seed[1]] = 1;
                // TEST (Remember to delete java.util.Arrays import)
                for (int[] row : MazeApp.grid) {
                    System.out.println(Arrays.toString(row));
                }
                //
                panel.setColor(Color.BLACK);
                panel.repaint();
                // Set WHITE if LeftMouseButton is pressed
            } else if (SwingUtilities.isRightMouseButton(event)) {
                MazeApp.grid[this.seed[0]][this.seed[1]] = 0;
                // TEST (Remember to delete java.util.Arrays import)
                for (int[] row : MazeApp.grid) {
                    System.out.println(Arrays.toString(row));
                }
                //
                panel.setColor(Color.WHITE);
                panel.repaint();
            }
        }
    }

}
