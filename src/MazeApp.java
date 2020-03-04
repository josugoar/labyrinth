package src;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import src.controller.JWGrid;
import src.model.PathFinder.Dijkstra;
import src.view.JWrapper;
import src.view.components.JWButton;
import src.view.components.JWPanel;
import src.view.components.JWSlider;
import src.view.components.JWSplitPane;

/**
 * A <code>java.lang.Runnable</code> extended version of
 * <code>java.awt.JFrame</code> that includes <code>src.MazeApp</code> solver,
 * generation, edition and interactive visualization features.
 *
 * @author JoshGoA
 * @version 0.1
 * @see java.lang.Runnable Runnable
 * @see javax.swing.JFrame JFrame
 */
public class MazeApp extends JFrame implements Runnable {

    /**
     * Enum of <code>src.controller.Cell</code> mode constants: <code>START</code>,
     * <code>END</code>, <code>OBSTACLE</code>, <code>EMPTY</code>.
     */
    public static enum Mode {
        START, END, OBSTACLE, EMPTY
    }

    private static final long serialVersionUID = 1L;

    /**
     * <code>src.controller.Cell</code> <code>src.MazeApp.Mode</code> selection.
     */
    private Mode mode = Mode.EMPTY;

    /**
     * <code>src.controller.JWGrid</code> algorithm modifier.
     */
    private int size = 50, speed = 1, density = 1;

    /**
     * A <code>src.controller.JWGrid</code> containing <code>src.controller.Cell</code>.
     *
     * @see src.controller.JWGrid JWGrid
     * @see src.controller.Cell Cell
     */
    private final JWGrid layout = new JWGrid(size, size, new Dimension(470, 500));

    /**
     * Invoke runnable in system EventQueue dispatch thread.
     *
     * @see javax.swing.SwingUtilities#invokeLater(Runnable doRun) invokeLater()
     */
    public final void start() {
        SwingUtilities.invokeLater(new MazeApp());
    }

    /**
     * Initialize HTML-like Container tree.
     *
     * @see src.view.JWrapper JWrapper
     * @see src.view.components.JWButton JWButton
     * @see src.view.components.JWPanel JWPanel
     * @see src.view.components.JWSlider JWSlider
     * @see src.view.components.JWSplitPane JWSplitPane
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
                            // Left JWPanel JWComponents
                            // Action JWButton
                            this.add(Box.createVerticalGlue());
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 25, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            // Reset JWGrid
                                            this.add(new JWButton("Clear",
                                                    e -> {
                                                        MazeApp.this.layout.setStart(null);
                                                        MazeApp.this.layout.setEnd(null);
                                                        MazeApp.this.layout.setGrid(MazeApp.this.size, MazeApp.this.size);
                                                    },
                                                    new Dimension(80, 30)
                                            ));
                                            // Awake PathFinder
                                            this.add(new JWButton("Awake",
                                                    e -> new Dijkstra().awake(MazeApp.this.layout.getGrid()),
                                                    new Dimension(80, 30)
                                            ));
                                        }
                                    }
                            ));
                            // Algorithm modifiers
                            this.add(new JWPanel(new FlowLayout(FlowLayout.LEFT, 10, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JLabel("  Size"));
                                            this.add(new JWSlider());
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.LEFT, 10, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JLabel("  Speed"));
                                            this.add(new JWSlider());
                                        }
                                    }
                            ));
                            this.add(new JWPanel(new FlowLayout(FlowLayout.LEFT, 10, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JLabel("  Density"));
                                            this.add(new JWSlider());
                                        }
                                    }
                            ));
                            // Algorithm selectors
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JComboBox());
                                            this.add(new JComboBox());
                                        }
                                    }
                            ));
                            // Action JWButton
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 25, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JWButton("Test",
                                                    e -> MazeApp.this.layout.setGrid(20, 20),
                                                    new Dimension(80, 30)
                                            ));
                                            this.add(new JWButton("Button", null, new Dimension(80, 30)));
                                        }
                                    }
                            ));
                        }
                    },
                    new Dimension(225, 0)
            )),
            // Right JWSplitPane
            new JWSplitPane(JWSplitPane.VERTICAL_SPLIT,
                // Top Right JWGridLayout
                layout,
                // Bottom Right JWPanel
                new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                    new ArrayList<Component>() {
                        private static final long serialVersionUID = 1L;
                        {
                            // Bottom Right JWPanel JWComponents
                            // MazeApp mode modifiers
                            this.add(new JWButton("Start",
                                    e -> MazeApp.this.mode = Mode.START,
                                    new Dimension(100, 20)
                            ));
                            this.add(new JWButton("End",
                                    e -> MazeApp.this.mode = Mode.END,
                                    new Dimension(100, 20)
                            ));
                            this.add(new JWButton("Obstacle",
                                    e -> MazeApp.this.mode = Mode.OBSTACLE,
                                    new Dimension(100, 20)
                            ));
                            this.add(new JWButton("Empty",
                                    e -> MazeApp.this.mode = Mode.EMPTY,
                                    new Dimension(100, 20)
                            ));
                        }
                    },
                    new Dimension(0, 30)
                ),
                true
            ),
            false
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

    public final Mode getMode() {
        return this.mode;
    }

}
