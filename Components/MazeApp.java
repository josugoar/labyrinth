package Components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

import Components.JWrapper.JWButton;
import Components.JWrapper.JWGridLayout;
import Components.JWrapper.JWPanel;
import Components.JWrapper.JWSplitPane;

/**
 * Runnable MazeApp JApplet
 */
public class MazeApp extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    private enum Mode {
        START, END, EMPTY, OBSTACLE
    }

    public static final void main(final String[] args) {
        EventQueue.invokeLater(new MazeApp());
    }

    private final void setComponents() {
        // Set custom JPanel wrapper for BoxLayout self-reference
        final JWrapper wrapper = (final Component panel) -> {
            ((JWPanel) panel).setLayout(new BoxLayout((JWPanel) panel, BoxLayout.Y_AXIS));
            return panel;
        };
        // Main JWSplitPane
        this.add(new JWSplitPane(JWSplitPane.HORIZONTAL_SPLIT,
                // Left JWPanel
                wrapper.JWComponent(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                        new ArrayList<Component>() {
                            private static final long serialVersionUID = 1L;
                            {
                                // Left JWPanel JWButtons
                                add(Box.createVerticalGlue());
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new ArrayList<Component>() {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }
                                ));
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new ArrayList<Component>() {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }
                                ));
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new ArrayList<Component>() {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }
                                ));
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new ArrayList<Component>() {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }
                                ));
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new ArrayList<Component>() {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
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
                    new JWGridLayout(20, 20,new Dimension(500, 500)),
                    // Bottom Right JWPanel
                    new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 12),
                        new ArrayList<Component>() {
                            private static final long serialVersionUID = 1L;
                            {
                                // Bottom Right JWPanel JWButtons
                                add(new JWButton("Button", new Dimension(100, 20), null));
                                add(new JWButton("Button", new Dimension(100, 20), null));
                                add(new JWButton("Button", new Dimension(100, 20), null));
                            }
                        },
                        new Dimension(500, 50)
                    )
                )
        ));
    }

    @Override
    public final void run() {
        this.setComponents();
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

}
