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

    // TODO: Change ArrayList to Component[]

    private static final long serialVersionUID = 1L;

    public static final void main(final String[] args) {
        EventQueue.invokeLater(new MazeApp());
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
                        new Dimension(250, 525), new ArrayList<Component>(2) {
                            private static final long serialVersionUID = 1L;
                            {
                                // Left JWPanel JWButtons
                                add(Box.createVerticalGlue());
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new Dimension(100, 50), new ArrayList<Component>(2) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }));
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new Dimension(100, 50), new ArrayList<Component>(2) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }));
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new Dimension(100, 50), new ArrayList<Component>(2) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }));
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new Dimension(100, 50), new ArrayList<Component>(2) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }));
                                add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                                        new Dimension(100, 50), new ArrayList<Component>(2) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                                add(new JWButton("Button", new Dimension(80, 30), null));
                                            }
                                        }));
                            }
                        })),
                // Right JWSplitPane
                new JWSplitPane(JWSplitPane.VERTICAL_SPLIT,
                    // Top Right JWGridLayout
                    new JWGridLayout(20, 20, new Dimension(500, 500)),
                    // Bottom Right JWPanel
                    new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 12), new Dimension(500, 50),
                        new ArrayList<Component>(1) {
                            private static final long serialVersionUID = 1L;
                            {
                                // Bottom Right JWPanel JWButtons
                                add(new JWButton("Button", new Dimension(100, 20), null));
                                add(new JWButton("Button", new Dimension(100, 20), null));
                                add(new JWButton("Button", new Dimension(100, 20), null));
                            }
                        }
                    )
                )
        ));
    }

}
