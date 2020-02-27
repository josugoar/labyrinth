package Components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Components.JWrapper.JWButton;
import Components.JWrapper.JWGridLayout;
import Components.JWrapper.JWPanel;
import Components.JWrapper.JWSplitPane;

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
        START, END, EMPTY, OBSTACLE
    }

    private Mode mode = Mode.OBSTACLE;
    private int size = 20, speed = 1, density = 1;

    public static final void main(final String[] args) {
        // Invoke runnable in system EventQueue dispatch thread
        SwingUtilities.invokeLater(new MazeApp());
    }

    /**
     * Initialize HTML-like Container tree
     *
     * @return New tree
     */
    private final Container initTree() {
        // Set custom JPanel wrapper for BoxLayout self-reference
        final JWrapper wrapper = (final Component panel) -> {
            ((JWPanel) panel).setLayout(new BoxLayout((JWPanel) panel, BoxLayout.Y_AXIS));
            return panel;
        };
        // Main JWSplitPane
        return new JWSplitPane(JWSplitPane.HORIZONTAL_SPLIT,
            // Left JWPanel
            wrapper.JWComponent(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                    new ArrayList<Component>() {
                        private static final long serialVersionUID = 1L;
                        {
                            // Left JWPanel JWButtons
                            this.add(Box.createVerticalGlue());
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
                                            this.add(new JWButton("Button", new Dimension(80, 30), null));
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
                new JWGridLayout(20, 20,new Dimension(470, 500)),
                // Bottom Right JWPanel
                new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 12),
                    new ArrayList<Component>() {
                        private static final long serialVersionUID = 1L;
                        {
                            // Bottom Right JWPanel JWButtons
                            this.add(new JWButton("Button", new Dimension(100, 20), null));
                            this.add(new JWButton("Button", new Dimension(100, 20), null));
                            this.add(new JWButton("Button", new Dimension(100, 20), null));
                            this.add(new JWButton("Button", new Dimension(100, 20), null));
                        }
                    },
                    new Dimension(470, 50)
                )
            )
        );
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
