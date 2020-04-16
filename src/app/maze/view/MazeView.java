package app.maze.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.InvalidParameterException;
import java.util.Enumeration;
import java.util.Objects;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import app.maze.components.algorithm.generator.Generator;
import app.maze.components.algorithm.generator.traversers.BackTracker;
import app.maze.components.algorithm.generator.traversers.DFS;
import app.maze.components.algorithm.generator.traversers.Prim;
import app.maze.components.algorithm.generator.traversers.Randomizer;
import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.algorithm.pathfinder.traversers.AStar;
import app.maze.components.algorithm.pathfinder.traversers.BFS;
import app.maze.components.algorithm.pathfinder.traversers.Dijkstra;
import app.maze.components.cell.composite.CellComposite;
import app.maze.components.cell.view.CellView;
import app.maze.controller.MazeController;
import app.maze.controller.components.panel.flyweight.PanelFlyweight;
import app.maze.controller.components.process.manager.ProcessManager;
import app.maze.model.MazeModel;
import app.maze.view.components.widget.decorator.ButtonDecorator;
import app.maze.view.components.widget.decorator.MenuDecorator;
import app.maze.view.components.widget.decorator.MenuItemDecorator;
import utils.JWrapper;

public final class MazeView extends JFrame {

    private static final long serialVersionUID = 1L;

    public JTree tree;

    public JLabel label;

    static {
        // Set cross-platform look-and-feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (final Exception e) {
            System.err.println("Unsupported look and feel...");
        }
        // Do not consume JPopupMenu event on close
        UIManager.put("PopupMenu.consumeEventOnClose", false);
    }

    {
        // Change Cursor state depending on user input key KeyEvent
        addKeyListener(new KeyAdapter() {
            @Override
            public final void keyPressed(final KeyEvent e) {
                // Dispatch KeyEvent
                mzController.dispatchKey(e);
            }
            @Override
            public final void keyReleased(final KeyEvent e) {
                // Reset Cursor state
                setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    public MazeView(final MazeController mzController) {
        super("MazeApp");
        setController(mzController);
    }

    public MazeView() {
        this(null);
    }

    public final void display() {
        try {
            initComponent();
            initFrame();
            // Set JFrame visible
            setVisible(true);
        } catch (final NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    public final JPopupMenu releasePopup(final CellView cell) throws InvalidParameterException {
        Objects.requireNonNull(cell, "CellSubject must not be null...");
        return new JPopupMenu() {
            private static final long serialVersionUID = 1L;
            {
                final MazeModel mzModel = mzController.getModel();
                final TreeNode node = cell.getComposite();
                addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public final void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                        // Select CellView
                        CellView.select(cell);
                    }
                    @Override
                    public final void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
                        // Unselect CellView
                        CellView.select(null);
                    }
                    @Override
                    public final void popupMenuCanceled(final PopupMenuEvent e) {
                    }
                });
                add(new MenuItemDecorator("Start", "rootIcon.gif") {
                    private static final long serialVersionUID = 1L;
                    {
                        // Set MazeMode root
                        addActionListener(e -> mzModel.setRoot(node));
                    }
                });
                add(new MenuItemDecorator("End", "targetIcon.gif") {
                    private static final long serialVersionUID = 1L;
                    {
                        // Set MazeModel target
                        addActionListener(e -> mzModel.setTarget(node));
                    }
                });
            }
        };
    }

    private final void initFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 525));
        setLocationRelativeTo(null);
        setFocusable(true);
        pack();
    }

    private JSplitPane split;

    private final void initComponent() throws NullPointerException {
        Objects.requireNonNull(mzController, "MazeController must not be null...");
        final PanelFlyweight flyweight = mzController.getFlyweight();
        final ProcessManager manager = mzController.getManager();
        add(new JPanel(new BorderLayout(0, 0)) {
            private static final long serialVersionUID = 1L;
            {
                add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    new JTabbedPane(SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT) {
                        private static final long serialVersionUID = 1L;
                        {
                            setVisible(false);
                            addTab("Node Tree", new JScrollPane(new JTree() {
                                private static final long serialVersionUID = 1L;
                                {
                                    tree = this;
                                    setShowsRootHandles(true);
                                    // Set default DefaultTreeModel with no root
                                    setModel(new DefaultTreeModel(new DefaultMutableTreeNode("No root node...")));
                                    setFocusable(false);
                                    setDoubleBuffered(true);
                                    setCellRenderer(new DefaultTreeCellRenderer() {
                                        private static final long serialVersionUID = 1L;
                                        @Override
                                        public final Component getTreeCellRendererComponent(final JTree tree,
                                                                                            final Object value,
                                                                                            final boolean sel,
                                                                                            final boolean expanded,
                                                                                            final boolean leaf,
                                                                                            final int row,
                                                                                            final boolean hasFocus) {
                                            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                                            // Dispatch cell
                                            mzController.dispatchCell(this, value);
                                            return this;
                                        }
                                    });
                                    addTreeSelectionListener(e -> {
                                        final TreePath path = e.getNewLeadSelectionPath();
                                        if (path == null)
                                            // Unselect CellView
                                            CellView.select(null);
                                        else
                                            // Select CellView
                                            CellView.select(((CellComposite) path.getLastPathComponent()).getView());
                                    });
                                }
                            }, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
                                private static final long serialVersionUID = 1L;
                                {
                                    setMinimumSize(new Dimension(135, getMinimumSize().height));
                                }
                            });
                        }
                    },
                    new JPanel(new BorderLayout(0, 0)) {
                        private static final long serialVersionUID = 1L;
                        {
                            add(flyweight, BorderLayout.CENTER);
                            add(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)) {
                                private static final long serialVersionUID = 1L;
                                {
                                    add(new JLabel("Maze", null, SwingConstants.LEADING) {
                                        private static final long serialVersionUID = 1L;
                                        {
                                            label = this;
                                            final Timer timer = new Timer(2500, e -> {
                                                // Set default JLabel text
                                                setText("Maze");
                                                ((Timer) e.getSource()).stop();
                                            });
                                            // Set JLabel visible
                                            addPropertyChangeListener("enabled", e -> setVisible(!isVisible()));
                                            addPropertyChangeListener("text", e -> {
                                                // Restart Timer
                                                if (timer.isRunning())
                                                    timer.restart();
                                                // Start Timer
                                                else
                                                    timer.start();
                                            });
                                        }
                                    });
                                }
                            }, BorderLayout.SOUTH);
                        }
                    })
                    {
                        private static final long serialVersionUID = 1L;
                        {
                            split = this;
                            setEnabled(false);
                            setBorder(null);
                            // Set default JSplitPane divider location
                            addPropertyChangeListener("enabled", e -> setDividerLocation(-1));
                        }
                    }, BorderLayout.CENTER);
                add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)) {
                    private static final long serialVersionUID = 1L;
                    {
                        add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)) {
                            private static final long serialVersionUID = 1L;
                            {
                                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                                add(new JToolBar(SwingConstants.VERTICAL) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        add(new JPanel(new GridLayout(3, 1, 0, 0)) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                                                add(new ButtonDecorator("Dimension", "dimensionIcon.gif") {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        final JSlider slider = new JSlider(10, 50, 20) {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                setPreferredSize(new Dimension(100, getPreferredSize().height));
                                                                addChangeListener(e -> {
                                                                    // Ignore if adjusting JSlider
                                                                    if (getValueIsAdjusting())
                                                                        return;
                                                                    // Resize panel with JSlider value
                                                                    mzController.resize(getValue());
                                                                });
                                                            }
                                                        };
                                                        addActionListener(e -> new JPopupMenu(null) {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                setFocusable(false);
                                                                add(slider);
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                add(new ButtonDecorator("Delay", "delayIcon.gif") {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        final JSlider slider = new JSlider(0, 250, 100) {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                setPreferredSize(new Dimension(100, getPreferredSize().height));
                                                                // Set algorithm delay with JSlider value
                                                                addChangeListener(e -> manager.setDelay(getValue()));
                                                            }
                                                        };
                                                        addActionListener(e -> new JPopupMenu(null) {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                setFocusable(false);
                                                                add(slider);
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                add(new ButtonDecorator("Density", "densityIcon.gif") {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        final JSlider slider = new JSlider(1, 99, 50) {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                setPreferredSize(new Dimension(100, getPreferredSize().height));
                                                                // Set Generator density with JSlider value
                                                                addChangeListener(e -> manager.setDensity(getValue()));
                                                            }
                                                        };
                                                        addActionListener(e -> new JPopupMenu(null) {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                setFocusable(false);
                                                                add(slider);
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                                add(new JToolBar(SwingConstants.VERTICAL) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        add(new JPanel(new GridLayout(2, 1, 0, 0)) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                                                add(new ButtonDecorator("Run PathFinder", "pathfinderRunIcon.gif") {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        // Awake PathFinder
                                                        addActionListener(e -> manager.awake(PathFinder.class));
                                                    }
                                                });
                                                add(new ButtonDecorator("Run Generator", "generatorRunIcon.gif") {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        // Awake Generator
                                                        addActionListener(e -> manager.awake(Generator.class));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }, BorderLayout.EAST);
                add(new JMenuBar() {
                    private static final long serialVersionUID = 1L;
                    {
                        add(new MenuDecorator("PathFinder", "pathfinderIcon.gif", KeyEvent.VK_P) {
                            private static final long serialVersionUID = 1L;
                            {
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        // Set PathFinder
                                        add(new JRadioButtonMenuItem("A Star", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                addItemListener(e -> manager.setAlgorithm(new AStar()));
                                            }
                                        });
                                        add(new JRadioButtonMenuItem("BFS", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                addItemListener(e -> manager.setAlgorithm(new BFS()));
                                            }
                                        });
                                        add(new JRadioButtonMenuItem("Dijkstra", null, true) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                addItemListener(e -> manager.setAlgorithm(new Dijkstra()));
                                            }
                                        });
                                    }
                                }.getElements(); e.hasMoreElements();) {
                                    add(e.nextElement());
                                }
                            }
                        });
                        add(new MenuDecorator("Generator", "generatorIcon.gif", KeyEvent.VK_G) {
                            private static final long serialVersionUID = 1L;
                            {
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        // Set Generator
                                        add(new JRadioButtonMenuItem("BackTracker", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                addItemListener(e -> manager.setAlgorithm(new BackTracker()));
                                            }
                                        });
                                        add(new JRadioButtonMenuItem("DFS", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                addItemListener(e -> manager.setAlgorithm(new DFS()));
                                            }
                                        });
                                        add(new JRadioButtonMenuItem("Prim", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                addItemListener(e -> manager.setAlgorithm(new Prim()));
                                            }
                                        });
                                        add(new JRadioButtonMenuItem("Randomizer", null, true) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                addItemListener(e -> manager.setAlgorithm(new Randomizer()));
                                            }
                                        });
                                    }
                                }.getElements(); e.hasMoreElements();) {
                                    add(e.nextElement());
                                }
                            }
                        });
                    }
                }, BorderLayout.NORTH);
            }
        }, BorderLayout.CENTER);
        add(new JMenuBar() {
            private static final long serialVersionUID = 1L;
            {
                add(new MenuDecorator("File", KeyEvent.VK_F) {
                    private static final long serialVersionUID = 1L;
                    {
                        // TODO: JFileChooser
                        add(new MenuItemDecorator("Open", "openIcon.gif", KeyEvent.VK_O) {
                            private static final long serialVersionUID = 1L;
                            {
                                addActionListener(e -> {
                                    // Read serialized object
                                    mzController.readMaze("components/ser/maze.ser");
                                    // final int returnVal = new JFileChooser().showOpenDialog(MazeView.this);
                                });
                            }
                        });
                        add(new JSeparator(SwingConstants.HORIZONTAL));
                        add(new MenuItemDecorator("Save", "saveIcon.gif", KeyEvent.VK_S) {
                            private static final long serialVersionUID = 1L;
                            {
                                addActionListener(e -> {
                                    // Serialize object
                                    mzController.writeMaze("components/ser/maze.ser");
                                    // final int returnVal = new JFileChooser().showSaveDialog(MazeView.this);
                                });
                            }
                        });
                    }
                });
                add(new MenuDecorator("Edit", KeyEvent.VK_E) {
                    private static final long serialVersionUID = 1L;
                    {
                        add(new MenuDecorator("Grid", "gridIcon.gif") {
                            private static final long serialVersionUID = 1L;
                            {
                                add(new MenuItemDecorator("Clear", "clearIcon.gif", KeyEvent.VK_Z) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        // Clear structure
                                        addActionListener(e -> mzController.clear());
                                    }
                                });
                                add(new MenuItemDecorator("Reset", "resetIcon.gif", KeyEvent.VK_R) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        // Reset structure
                                        addActionListener(e -> mzController.reset());
                                    }
                                });
                            }
                        });
                        add(new JSeparator(SwingConstants.HORIZONTAL));
                        add(new MenuDecorator("Preferences", "preferencesIcon.gif") {
                            private static final long serialVersionUID = 1L;
                            {
                                add(new JCheckBoxMenuItem("Periodic", null, false) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        // Set periodic cycling value
                                        addActionListener(e -> flyweight.setPeriodic(!flyweight.isPeriodic()));
                                    }
                                });
                                add(new JCheckBoxMenuItem("Edged", null, false) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        // Set edged cycling value
                                        addActionListener(e -> flyweight.setEdged(!flyweight.isEdged()));
                                    }
                                });
                                add(new JCheckBoxMenuItem("Status Bar", null, true) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        // Enable JLabel cycling value
                                        addItemListener(e -> label.setEnabled(!label.isEnabled()));
                                    }
                                });
                                add(new JCheckBoxMenuItem("Node Tree", null, false) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        // Enable JSplitPane cycling value
                                        addItemListener(e -> {
                                            final Component component = split.getLeftComponent();
                                            split.setEnabled(!split.isEnabled());
                                            component.setVisible(!component.isVisible());
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
                add(new MenuDecorator("Run", KeyEvent.VK_R) {
                    private static final long serialVersionUID = 1L;
                    {
                        add(new MenuItemDecorator("PathFinder", "pathfinderRunIcon.gif", KeyEvent.VK_1) {
                            private static final long serialVersionUID = 1L;
                            {
                                // Awake PathFinder
                                addActionListener(e -> manager.awake(PathFinder.class));
                            }
                        });
                        add(new MenuItemDecorator("Generator", "generatorRunIcon.gif", KeyEvent.VK_2) {
                            private static final long serialVersionUID = 1L;
                            {
                                // Awake Generator
                                addActionListener(e -> manager.awake(Generator.class));
                            }
                        });
                    }
                });
            }
        }, BorderLayout.NORTH);
    }

    public final JTree getTree() {
        return tree;
    }

    public final JLabel getLabel() {
        return label;
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

}
