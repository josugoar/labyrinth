package app.maze.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.InvalidParameterException;
import java.util.Enumeration;
import java.util.Objects;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import app.maze.components.algorithm.generator.Generator;
import app.maze.components.algorithm.generator.traversers.BackTracker;
import app.maze.components.algorithm.generator.traversers.DFS;
import app.maze.components.algorithm.generator.traversers.Prim;
import app.maze.components.algorithm.generator.traversers.Randomizer;
import app.maze.components.algorithm.pathfinder.PathFinder;
import app.maze.components.algorithm.pathfinder.traversers.AStar;
import app.maze.components.algorithm.pathfinder.traversers.BFS;
import app.maze.components.algorithm.pathfinder.traversers.Dijkstra;
import app.maze.components.cell.observer.CellObserver;
import app.maze.components.cell.subject.CellSubject;
import app.maze.controller.MazeController;
import app.maze.view.components.widgets.decorator.ButtonDecorator;
import utils.JWrapper;

public final class MazeView extends JFrame {

    private static final long serialVersionUID = 1L;

    public JTree tree;

    public JLabel label;

    {
        // Set Cross-Platform Look-And-Feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (final Exception e) {
            System.err.println("Unsupported look and feel...");
        }
        // Do not consume JPopupMenu event on close
        UIManager.put("PopupMenu.consumeEventOnClose", false);
    }

    {
        // Change cursor state depending on user input key
        this.addKeyListener(new KeyAdapter() {
            @Override
            public final void keyPressed(final KeyEvent e) {
                // Dispatch KeyEvent
                MazeView.this.mzController.dispatchKey(e);
            }
            @Override
            public final void keyReleased(final KeyEvent e) {
                // Reset Cursor state
                MazeView.this.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    public MazeView(final MazeController mzController) {
        super("MazeApp");
        this.setController(mzController);
    }

    public MazeView() {
        this(null);
    }

    public final void display() {
        try {
            this.initComponent();
            this.initFrame();
            this.setVisible(true);
        } catch (final NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    private JSplitPane split;

    private final void initComponent() throws NullPointerException {
        this.add(new JPanel(new BorderLayout(0, 0)) {
            private static final long serialVersionUID = 1L;
            {
                this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                        new JTabbedPane(SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT) {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setVisible(false);
                                this.addTab("Node Tree", new JScrollPane(new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("No root node..."))) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        MazeView.this.tree = this;
                                        this.setShowsRootHandles(true);
                                        this.setFocusable(false);
                                        this.setDoubleBuffered(true);
                                        this.setCellRenderer(new DefaultTreeCellRenderer() {
                                            private static final long serialVersionUID = 1L;
                                            @Override
                                            public final Component getTreeCellRendererComponent(final JTree tree, final Object value,final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
                                                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                                                MazeView.this.mzController.dispatchCell(this, value);
                                                return this;
                                            }
                                        });
                                        this.addTreeSelectionListener(e -> {
                                            if (CellSubject.getSelected() != null || e.getNewLeadSelectionPath() == null)
                                                return;
                                            CellSubject.focus((MazeView.this.mzController.getFlyweight().request((CellObserver) e.getNewLeadSelectionPath().getLastPathComponent())));
                                        });
                                    }
                                }, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setMinimumSize(new Dimension(135, this.getMinimumSize().height));
                                    }
                                });
                            }
                        }, new JPanel(new BorderLayout(0, 0)) {
                            private static final long serialVersionUID = 1L;
                            {
                                this.add(MazeView.this.mzController.getFlyweight(), BorderLayout.CENTER);
                                this.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JLabel("Maze", null, SwingConstants.LEADING) {
                                            private static final long serialVersionUID = 1L;
                                            final Timer timer = new Timer(2500, e -> {
                                                MazeView.this.label.setText("Maze");
                                                ((Timer) e.getSource()).stop();
                                            });
                                            {
                                                MazeView.this.label = this;
                                                this.addPropertyChangeListener("enabled", e -> ((JLabel) e.getSource()).setVisible(!((JLabel) e.getSource()).isVisible()));
                                                this.addPropertyChangeListener("text", e -> {
                                                    if (timer.isRunning())
                                                        timer.restart();
                                                    else
                                                        timer.start();
                                                });
                                            }
                                        });
                                    }
                                }, BorderLayout.SOUTH);
                            }
                        }) {
                    private static final long serialVersionUID = 1L;
                    {
                        MazeView.this.split = this;
                        this.setEnabled(false);
                        this.setBorder(null);
                        this.addPropertyChangeListener("enabled", e -> ((JSplitPane) e.getSource()).setDividerLocation(-1));
                    }
                }, BorderLayout.CENTER);
                this.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)) {
                    private static final long serialVersionUID = 1L;
                    {
                        this.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)) {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                                this.add(new JToolBar(SwingConstants.VERTICAL) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JPanel(new GridLayout(3, 1, 0, 0)) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                                                this.add(new ButtonDecorator("Dimension", new ImageIcon(MazeView.class.getResource("assets/dimensionIcon.gif"))) {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new JPopupMenu() {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.setFocusable(false);
                                                                this.add(new JSlider(10, 50, 20) {
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
                                                                        this.addChangeListener(e -> {
                                                                            if (((JSlider) e.getSource()).getValueIsAdjusting())
                                                                                return;
                                                                            MazeView.this.mzController.resize(((JSlider) e.getSource()).getValue());
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                this.add(new ButtonDecorator("Delay", new ImageIcon(MazeView.class.getResource("assets/delayIcon.gif"))) {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new JPopupMenu() {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.setFocusable(false);
                                                                this.add(new JSlider(0, 250, 100) {
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
                                                                        this.addChangeListener(e -> MazeView.this.mzController.getProcess().setDelay(((JSlider) e.getSource()).getValue()));
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                this.add(new ButtonDecorator("Density", new ImageIcon(MazeView.class.getResource("assets/densityIcon.gif"))) {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new JPopupMenu() {
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.setFocusable(false);
                                                                this.add(new JSlider(1, 99, 50) {
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
                                                                        this.addChangeListener(e -> MazeView.this.mzController.getProcess().setDensity(((JSlider) e.getSource()).getValue()));
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                                this.add(new JToolBar(SwingConstants.VERTICAL) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JPanel(new GridLayout(2, 1, 0, 0)) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                                                this.add(new ButtonDecorator("Run PathFinder", new ImageIcon(MazeView.class.getResource("assets/pathfinderRunIcon.gif"))) {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> MazeView.this.mzController.getProcess().awake(PathFinder.class));
                                                    }
                                                });
                                                this.add(new ButtonDecorator("Run Generator", new ImageIcon(MazeView.class.getResource("assets/generatorRunIcon.gif"))) {
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> MazeView.this.mzController.getProcess().awake(Generator.class));
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
                this.add(new JMenuBar() {
                    private static final long serialVersionUID = 1L;
                    {
                        this.add(new JMenu("PathFinder") {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_P);
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/pathfinderIcon.gif")));
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("A Star", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.mzController.getProcess().setAlgorithm(new AStar()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("BFS", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.mzController.getProcess().setAlgorithm(new BFS()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Dijkstra", null, true) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.mzController.getProcess().setAlgorithm(new Dijkstra()));
                                            }
                                        });
                                    }
                                }.getElements(); e.hasMoreElements();) {
                                    this.add(e.nextElement());
                                }
                            }
                        });
                        this.add(new JMenu("Generator") {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_G);
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/generatorIcon.gif")));
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("BackTracker", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.mzController.getProcess().setAlgorithm(new BackTracker()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("DFS", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.mzController.getProcess().setAlgorithm(new DFS()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Prim", null, false) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.mzController.getProcess().setAlgorithm(new Prim()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Randomizer", null, true) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.mzController.getProcess().setAlgorithm(new Randomizer()));
                                            }
                                        });
                                    }
                                }.getElements(); e.hasMoreElements();) {
                                    this.add(e.nextElement());
                                }
                            }
                        });
                    }
                }, BorderLayout.NORTH);
            }
        }, BorderLayout.CENTER);
        this.add(new JMenuBar() {
            private static final long serialVersionUID = 1L;
            {
                this.add(new JMenu("File") {
                    private static final long serialVersionUID = 1L;
                    {
                        this.setMnemonic(KeyEvent.VK_F);
                        this.add(new JMenuItem("Open", new ImageIcon(MazeView.class.getResource("assets/openIcon.gif"))) {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_O);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> {
                                    MazeView.this.mzController.readMaze();
                                    // TODO: Open
                                    final int returnVal = new JFileChooser().showOpenDialog(MazeView.this);
                                });
                            }
                        });
                        this.add(new JSeparator(SwingConstants.HORIZONTAL));
                        this.add(new JMenuItem("Save", new ImageIcon(MazeView.class.getResource("assets/saveIcon.gif"))) {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_S);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> {
                                    MazeView.this.mzController.writeMaze();
                                    // TODO: Save
                                    final int returnVal = new JFileChooser().showSaveDialog(MazeView.this);
                                });
                            }
                        });
                    }
                });
                this.add(new JMenu("Edit") {
                    private static final long serialVersionUID = 1L;
                    {
                        this.setMnemonic(KeyEvent.VK_E);
                        this.add(new JMenu("Grid") {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/gridIcon.gif")));
                                this.add(new JMenuItem("Clear", new ImageIcon(MazeView.class.getResource("assets/clearIcon.gif"))) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setMnemonic(KeyEvent.VK_Z);
                                        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
                                        this.addActionListener(e -> MazeView.this.mzController.clear());
                                    }
                                });
                                this.add(new JMenuItem("Reset", new ImageIcon(MazeView.class.getResource("assets/resetIcon.gif"))) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setMnemonic(KeyEvent.VK_R);
                                        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
                                        this.addActionListener(e -> MazeView.this.mzController.reset());
                                    }
                                });
                            }
                        });
                        this.add(new JSeparator(SwingConstants.HORIZONTAL));
                        this.add(new JMenu("Preferences") {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/preferencesIcon.gif")));
                                this.add(new JCheckBoxMenuItem("Periodic", null, false) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> MazeView.this.mzController.getFlyweight().setPeriodic(!MazeView.this.mzController.getFlyweight().isPeriodic()));
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Edged", null, false) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> MazeView.this.mzController.getFlyweight().setEdged(!MazeView.this.mzController.getFlyweight().isEdged()));
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Status Bar", null, true) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> MazeView.this.label.setEnabled(!MazeView.this.label.isEnabled()));
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Node Tree", null, false) {
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> {
                                            MazeView.this.split.setEnabled(!MazeView.this.split.isEnabled());
                                            MazeView.this.split.getLeftComponent().setVisible(!MazeView.this.split.getLeftComponent().isVisible());
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
                this.add(new JMenu("Run") {
                    private static final long serialVersionUID = 1L;
                    {
                        this.setMnemonic(KeyEvent.VK_R);
                        this.add(new JMenuItem("PathFinder", new ImageIcon(MazeView.class.getResource("assets/pathfinderRunIcon.gif"))) {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_1);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeView.this.mzController.getProcess().awake(PathFinder.class));
                            }
                        });
                        this.add(new JMenuItem("Generator", new ImageIcon(MazeView.class.getResource("assets/generatorRunIcon.gif"))) {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_2);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeView.this.mzController.getProcess().awake(Generator.class));
                            }
                        });
                    }
                });
            }
        }, BorderLayout.NORTH);
    }

    private final void initFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(450, 525));
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.pack();
    }

    public final JPopupMenu releasePopup(final CellSubject cell) throws InvalidParameterException {
        return new JPopupMenu() {
            private static final long serialVersionUID = 1L;
            {
                this.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public final void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                        CellSubject.select(Objects.requireNonNull(cell, "CellSubject must not be null..."));
                    }
                    @Override
                    public final void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
                        CellSubject.select(null);
                    }
                    @Override
                    public final void popupMenuCanceled(final PopupMenuEvent e) {
                        CellSubject.select(null);
                    }
                });
                this.add(new JMenuItem("Start", new ImageIcon(MazeView.class.getResource("assets/startIcon.gif"))) {
                    private static final long serialVersionUID = 1L;
                    {
                        this.addActionListener(e -> MazeView.this.mzController.getModel().setRoot(cell.getObserver()));
                    }
                });
                this.add(new JMenuItem("End", new ImageIcon(MazeView.class.getResource("assets/endIcon.gif"))) {
                    private static final long serialVersionUID = 1L;
                    {
                        this.addActionListener(e -> MazeView.this.mzController.getModel().setTarget(cell.getObserver()));
                    }
                });
            }
        };
    }

    public final JTree getTree() {
        return this.tree;
    }

    public final JLabel getLabel() {
        return this.label;
    }

    private transient MazeController mzController;

    public final MazeController getController() {
        return this.mzController;
    }

    public final void setController(final MazeController mzController) {
        this.mzController = mzController;
    }

}
