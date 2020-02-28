package src;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import src.controller.JWGrid;
import src.view.JWrapper;
import src.view.components.JWButton;
import src.view.components.JWPanel;
import src.view.components.JWSplitPane;

/**
 * Runnable MazeApp JApplet
 *
 * @author JoshGoA
 */
public class MazeApp extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    /**
     * Cell Mode
     */
    public enum Mode {
        START, END, OBSTACLE, EMPTY
    }

    private Mode mode = Mode.OBSTACLE;
    private int size = 20, speed = 1, density = 1;

    /**
     * Initialize HTML-like Container tree
     *
     * @return New tree
     */
    private final Container initTree() {
        // Set custom JPanel wrapper for BoxLayout self-reference
        final JWrapper JWBox = (final Component panel) -> {
            ((JWPanel) panel).setLayout(new BoxLayout((JWPanel) panel, BoxLayout.Y_AXIS));
            return panel;
        };
        // Main JWSplitPane
        return new JWSplitPane(JWSplitPane.HORIZONTAL_SPLIT,
            // Left JWPanel
            JWBox.JWComponent(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                    new ArrayList<Component>() {
                        private static final long serialVersionUID = 1L;
                        {
                            // Left JWPanel JWButtons
                            this.add(Box.createVerticalGlue());
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                        }
                                    }
                            ));
                        }
                    },
                    new Dimension(200, 0)
            )),
            // Right JWSplitPane
            new JWSplitPane(JWSplitPane.VERTICAL_SPLIT,
                // Top Right JWGridLayout
                new JWGrid(20, 20, new Dimension(470, 500)),
                // Bottom Right JWPanel
                new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 12),
                    new ArrayList<Component>() {
                        private static final long serialVersionUID = 1L;
                        {
                            // Bottom Right JWPanel JWButtons
                            this.add(new JWButton("Start",
                                    new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            MazeApp.this.setMode(Mode.START);
                                        }
                                    },
                                    new Dimension(100, 20)
                            ));
                            this.add(new JWButton("End",
                                    new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            MazeApp.this.setMode(Mode.END);
                                        }
                                    },
                                    new Dimension(100, 20)
                            ));
                            this.add(new JWButton("Obstacle",
                                    new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            MazeApp.this.setMode(Mode.OBSTACLE);
                                        }
                                    },
                                    new Dimension(100, 20)
                            ));
                            this.add(new JWButton("Empty",
                                    new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            MazeApp.this.setMode(Mode.EMPTY);
                                        }
                                    },
                                    new Dimension(100, 20)
                            ));
                        }
                    },
                    new Dimension(470, 50)
                )
            )
        );
    }

    public final void start() {
        // Invoke runnable in system EventQueue dispatch thread
        SwingUtilities.invokeLater(new MazeApp());
    }

    @Override
    public final void run() {
        this.setContentPane(initTree());
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    @Override
    public final String toString() {
        return String.format("%s", this.getClass());
    }

    public final Mode getMode() {
        return this.mode;
    }

    protected final void setMode(final Mode mode) {
        this.mode = mode;
    }

}
