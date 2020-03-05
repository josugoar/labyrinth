package src;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import src.controller.Cell;
import src.controller.Cell.State;
import src.controller.JWGrid;
import src.model.PathFinder;
import src.model.PathFinder.Dijkstra;
import src.view.JWrapper;
import src.view.components.JWButton;
import src.view.components.JWPanel;
import src.view.components.JWSlider;
import src.view.components.JWSplitPane;

/**
 * A <code>java.lang.Runnable</code> extended version of
 * <code>java.awt.JFrame</code> implementation that includes
 * <code>src.MazeApp</code> solving, generation, edition and interactive
 * visualization features.
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
     *
     * @see src.MazeApp.Mode Mode
     */
    private Mode mode = Mode.EMPTY;

    /**
     * <code>src.controller.JWGrid</code> feature modifier: layout size.
     */
    private int dimension = 50;

    /**
     * <code>src.controller.JWGrid</code> feature modifier: draw delay between
     * recursive iterations.
     */
    private int speed = 50;

    /**
     * <code>src.controller.JWGrid</code> feature modifier: generator obstacle
     * density.
     */
    private int density = 50;

    /**
     * <code>src.controller.JWGrid</code> feature modifier:
     * <code>src.model.PathFinder</code> algorithm selector.
     *
     * @see src.model.PathFinder PathFinder
     * @see src.model.PathFinder.Dijkstra Dijkstra
     */
    private PathFinder pathfinder = new Dijkstra();

    /**
     * A <code>src.controller.JWGrid</code> containing
     * <code>src.controller.Cell</code>.
     *
     * @see src.controller.JWGrid JWGrid
     * @see src.controller.Cell Cell
     */
    private final JWGrid layout = new JWGrid(dimension, dimension, new Dimension(500, 500));

    /**
     * Invoke Runnable in system EventQueue dispatch Thread.
     *
     * @see javax.swing.SwingUtilities#invokeLater(Runnable doRun) invokeLater()
     */
    public final void start() {
        SwingUtilities.invokeLater(this);
    }

    /**
     * Initialize Container tree.
     *
     * @see src.view.JWrapper JWrapper
     * @see src.view.components.JWButton JWButton
     * @see src.view.components.JWPanel JWPanel
     * @see src.view.components.JWSlider JWSlider
     * @see src.view.components.JWSplitPane JWSplitPane
     * @return New tree
     */
    private final Container initTree() {
        // Set custom JWPanel wrapper for BoxLayout self-reference
        final JWrapper JWBox = (final Component panel) -> {
            ((JWPanel) panel).setLayout(new BoxLayout((JWPanel) panel, BoxLayout.Y_AXIS));
            return panel;
        };
        // Main JWSplitPane
        return new JWSplitPane(JWSplitPane.HORIZONTAL_SPLIT,
            // Left JWPanel
            JWBox.JWComponent(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                    // Left JWPanel Component
                    new ArrayList<Component>() {
                        private static final long serialVersionUID = 1L;
                        {
                            // Add Box VerticalGlue to evenly space Component
                            this.add(Box.createVerticalGlue());
                            // Algorithm awakers
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 20, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            // Generate random obstacle pattern
                                            this.add(new JWButton("Generate", null, new Dimension(87, 30)));
                                            // Awake PathFinder
                                            this.add(new JWButton("Awake",
                                                    e -> MazeApp.this.pathfinder.awake(MazeApp.this.layout.getGrid()),
                                                    new Dimension(87, 30)
                                            ));
                                        }
                                    }
                            ));
                            // Feature modifiers
                            // Dimension modifier
                            this.add(new JWPanel(new FlowLayout(FlowLayout.LEFT, 10, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JLabel("  Dimension"));
                                            this.add(new JWSlider(2, 100, 50,
                                                e -> {
                                                    final JWSlider source = (JWSlider) e.getSource();
                                                    ((MazeApp) SwingUtilities.getWindowAncestor(source)).setDimension(source.getValue());
                                                }
                                            ));
                                        }
                                    }
                            ));
                            // Speed modifier
                            this.add(new JWPanel(new FlowLayout(FlowLayout.LEFT, 10, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JLabel("  Speed"));
                                            this.add(new JWSlider(1, 100, 50,
                                                e -> {
                                                    final JWSlider source = (JWSlider) e.getSource();
                                                    ((MazeApp) SwingUtilities.getWindowAncestor(source)).setSpeed(source.getValue());
                                                }
                                            ));
                                        }
                                    }
                            ));
                            // Density modifier
                            this.add(new JWPanel(new FlowLayout(FlowLayout.LEFT, 10, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.add(new JLabel("  Density"));
                                            this.add(new JWSlider(1, 100, 50,
                                                e -> {
                                                    final JWSlider source = (JWSlider) e.getSource();
                                                    ((MazeApp) SwingUtilities.getWindowAncestor(source)).setDensity(source.getValue());
                                                }
                                            ));
                                        }
                                    }
                            ));
                            // Algorithm selectors
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            // PathFinder selector
                                            this.add(new JComboBox());
                                            // MazeGenerator selector
                                            this.add(new JComboBox());
                                        }
                                    }
                            ));
                            // Serialization activators
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 20, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            // Load JWGrid
                                            this.add(new JWButton("Load", null, new Dimension(87, 30)));
                                            // Save JWGrid
                                            this.add(new JWButton("Save", null, new Dimension(87, 30)));
                                        }
                                    }
                            ));
                            // Cell State resetters
                            this.add(new JWPanel(new FlowLayout(FlowLayout.CENTER, 20, 0),
                                    new ArrayList<Component>() {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            // Reset entire JWGrid
                                            this.add(new JWButton("Reset",
                                                    e -> {
                                                        MazeApp.this.layout.setStart(null);
                                                        MazeApp.this.layout.setEnd(null);
                                                        MazeApp.this.layout.setGrid(MazeApp.this.dimension, MazeApp.this.dimension);
                                                    },
                                                    new Dimension(87, 30)
                                            ));
                                            // Reset JWGrid
                                            this.add(new JWButton("Clear",
                                                    e -> {
                                                        for (final Cell cell : MazeApp.this.layout.getGrid().values()) {
                                                            final State state = cell.getState();
                                                            if (state == State.VISITED || state == State.GERMINATED || state == State.PATH)
                                                                cell.setState(State.EMPTY);
                                                        }
                                                    },
                                                    new Dimension(87, 30)
                                            ));
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
                this.layout,
                // Bottom Right JWPanel
                new JWPanel(new FlowLayout(FlowLayout.CENTER, 10, 10),
                    // Bottom Right JWPanel Component
                    new ArrayList<Component>() {
                        private static final long serialVersionUID = 1L;
                        {
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
        this.setTitle("MazeApp");
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    @Override
    public final String toString() {
        return String.format("MazeApp(mode: , size: , speed: , density: )", this.mode, this.dimension, this.speed,
                this.density);
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + density;
        result = prime * result + ((layout == null) ? 0 : layout.hashCode());
        result = prime * result + ((mode == null) ? 0 : mode.hashCode());
        result = prime * result + dimension;
        result = prime * result + speed;
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MazeApp other = (MazeApp) obj;
        if (density != other.density)
            return false;
        if (layout == null) {
            if (other.layout != null)
                return false;
        } else if (!layout.equals(other.layout))
            return false;
        if (mode != other.mode)
            return false;
        if (dimension != other.dimension)
            return false;
        if (speed != other.speed)
            return false;
        return true;
    }

    public final Mode getMode() {
        return this.mode;
    }

    public final int getDimension() {
        return this.dimension;
    }

    public final void setDimension(final int dimension) {
        this.dimension = dimension;
        MazeApp.this.layout.setGrid(this.dimension, this.dimension);
    }

    public final int getSpeed() {
        return this.speed;
    }

    public final void setSpeed(final int speed) {
        this.speed = Math.abs(speed - 100);
    }

    public final int getDensity() {
        return this.density;
    }

    public void setDensity(final int density) {
        this.density = density;
    }

    public final PathFinder getPathFinder() {
        return this.pathfinder;
    }

    public final void setPathFinder(final PathFinder pathfinder) {
        this.pathfinder = Objects.requireNonNull(pathfinder, "'pathfinder' must not be null");
    }

}
