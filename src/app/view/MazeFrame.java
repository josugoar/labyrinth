package app.view;

import java.awt.BorderLayout;
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
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;

import app.controller.MazeDelegator;
import app.model.Generator;
import app.model.PathFinder;
import app.model.components.CellPanel;
import app.view.components.FocusedPopup;
import app.view.components.IconifiedButton;
import app.view.components.RangedSlider;
import utils.JWrapper;

/**
 * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
 * pivotal <code>app.view.MazeFrame</code> component, extending
 * <code>java.awt.JFrame</code>.
 *
 * @author JoshGoA
 * @version 0.1
 * @see javax.swing.JFrame JFrame
 */
public class MazeFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Two-sided <code>app.controller.MazeDelegator</code>
     * <code>app.view.MazeFrame</code> interaction pipeline.
     *
     * @see app.controller.MazeDelegator MazeDelegator
     */
    private MazeDelegator delegator;

    /**
     * <code>javax.swing.JTree</code> component displaying
     * <code>app.model.components.CellPanel</code> and
     * <code>app.model.components.Node</code> child generations.
     *
     * @see javax.swing.JTree JTree
     * @see app.model.components.CellPanel CellPanel
     * @see app.model.components.Node Node
     */
    private JTree treeComponent;

    /**
     * <code>javax.swing.JLabel</code> component displaying custom application
     * output messages.
     *
     * @see javax.swing.JLabel JLabel
     */
    private JLabel statusComponent;

    /**
     * <code>javax.swing.JSplitPane</code> component responsible of visual
     * interactions.
     *
     * @see javax.swing.JSplitPane JSplitPane
     */
    private JSplitPane splitComponent;

    {
        // Set Cross-Platform Look-And-Feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (final Exception e) {
            System.err.println("Unsupported look and feel...");
        }
    }

    {
        this.addKeyListener(new KeyAdapter() {
            // Change cursor state depending on user input key
            @Override
            public final void keyPressed(final KeyEvent e) {
                // Enable draw state
                if (e.isShiftDown())
                    MazeFrame.this.delegator.dispatchKey();
            }
            @Override
            public final void keyReleased(final KeyEvent e) {
                // Disable draw state
                MazeFrame.this.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    /**
     * Create a new two-sided <code>app.controller.MazeDelegator</code> interaction
     * <code>app.view.MazeFrame</code> pipeline component.
     *
     * @param delegator MazeDelegator
     */
    public MazeFrame(final MazeDelegator delegator) {
        super("MazeApp");
        this.setDelegator(delegator);
    }

    /**
     * Create a new isolated pipeline component.
     */
    public MazeFrame() {
        this(null);
    }

    /**
     * Display frame on screen.
     */
    public final void display() {
        try {
            this.initLayout();
            this.initFrame();
            this.setVisible(true);
        } catch (final NullPointerException e) {
            JWrapper.dispatchException(e);
        }
    }

    /**
     * Initialize entire <code>java.awt.Component</code> tree structure.
     *
     * @see java.awt.Component Component
     * @throws NullPointerException if (delegator == null)
     */
    private final void initLayout() throws NullPointerException {
        if (this.delegator == null)
            throw new NullPointerException("MazeDelegator might not hvae been initialized...");
        this.add(new JPanel(new BorderLayout(0, 0)) {
            // pnl_splitComponentWrapper
            private static final long serialVersionUID = 1L;
            {
                this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JTabbedPane(SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT) {
                    // tab_treeComponentWrapper
                    private static final long serialVersionUID = 1L;
                    {
                        this.setVisible(false);
                        this.addTab("Node Tree", new JScrollPane(new JTree(new DefaultMutableTreeNode("Start")) {
                            // tre_treeComponent
                            private static final long serialVersionUID = 1L;
                            {
                                this.setShowsRootHandles(true);
                                MazeFrame.this.setTreeComponent(this);
                            }
                        }, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
                            // scr_treeComponentScroll
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMinimumSize(new Dimension(100, this.getMinimumSize().height));
                            }
                        });
                    }
                }, new JPanel(new BorderLayout(0, 0)) {
                    // pnl_mazeModelWrapper
                    private static final long serialVersionUID = 1L;
                    {
                        // pnl_mazeModel
                        this.add(MazeFrame.this.delegator.getPanel(), BorderLayout.CENTER);
                        this.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)) {
                            // pnl_statusComponentWrapper
                            private static final long serialVersionUID = 1L;
                            {
                                this.add(new JLabel(MazeFrame.this.delegator.toString(), null, SwingConstants.LEADING) {
                                    // lbl_statusComponent
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addPropertyChangeListener("text",e -> MazeFrame.this.delegator.resetStatusComponent());
                                        MazeFrame.this.setStatusComponent(this);
                                    }
                                });
                            }
                        }, BorderLayout.SOUTH);
                    }
                }) {
                    // spl_splitComponent
                    private static final long serialVersionUID = 1L;
                    {
                        this.setEnabled(false);
                        this.setBorder(null);
                        MazeFrame.this.setSplitComponent(this);
                    }
                }, BorderLayout.CENTER);
                this.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)) {
                    // pnl_toolBarWrapper
                    private static final long serialVersionUID = 1L;
                    {
                        this.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)) {
                            // pnl_toolBar
                            private static final long serialVersionUID = 1L;
                            {
                                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                                this.add(new JToolBar(SwingConstants.VERTICAL) {
                                    // tlb_featureBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JPanel(new GridLayout(3, 1, 0, 0)) {
                                            // pnl_featuresBarWrapper
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                                                this.add(new IconifiedButton("Dimension",
                                                        new ImageIcon(MazeFrame.class.getResource("assets/dimensionIcon.gif"))) {
                                                    // btn_dimensionSelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new FocusedPopup(MazeFrame.this) {
                                                            // pmn_dimensionSelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new RangedSlider(MazeFrame.this.delegator.getDimension()) {
                                                                    // sld_dimensionSelector
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.addChangeListener(e -> {
                                                                            if (!((JSlider) e.getSource()).getValueIsAdjusting())
                                                                                MazeFrame.this.delegator.setDimension(this.getValue());
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                this.add(new IconifiedButton("Delay",
                                                        new ImageIcon(MazeFrame.class.getResource("assets/delayIcon.gif"))) {
                                                    // btn_delaySelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new FocusedPopup(MazeFrame.this) {
                                                            // pmn_delaySelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new RangedSlider(MazeFrame.this.delegator.getDelay()) {
                                                                    // sld_delaySelector
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.addChangeListener(e -> MazeFrame.this.delegator.setDelay(this.getValue()));
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                this.add(new IconifiedButton("Density",
                                                        new ImageIcon(MazeFrame.class.getResource("assets/densityIcon.gif"))) {
                                                    // btn_densitySelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new FocusedPopup(MazeFrame.this) {
                                                            // pmn_densitySelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new RangedSlider(MazeFrame.this.delegator.getDensity()) {
                                                                    // sld_densitySelector
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.addChangeListener(e -> MazeFrame.this.delegator.setDensity(this.getValue()));
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
                                    // tlb_runBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JPanel(new GridLayout(2, 1, 0, 0)) {
                                            // pnl_runBarWrapper
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                                                this.add(new IconifiedButton("Run PathFinder",
                                                        new ImageIcon(MazeFrame.class.getResource("assets/pathfinderRunIcon.gif"))) {
                                                    // btn_runPathFinder
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> MazeFrame.this.delegator.awakePathFinder());
                                                    }
                                                });
                                                this.add(new IconifiedButton("Run Generator",
                                                        new ImageIcon(MazeFrame.class.getResource("assets/generatorRunIcon.gif"))) {
                                                    // btn_runGenerator
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> MazeFrame.this.delegator.awakeGenerator());
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
                    // mnu_algorithmSelector
                    private static final long serialVersionUID = 1L;
                    {
                        this.add(new JMenu("PathFinder") {
                            // mn_pathfinderSelector
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_P);
                                this.setIcon(new ImageIcon(MazeFrame.class.getResource("assets/pathfinderIcon.gif")));
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    // btn_grp_pathfinderSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("A Star", null, false) {
                                            // rd_btn_mni_pathfinderAStar
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> MazeFrame.this.delegator.setPathFinder(new PathFinder.AStar()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("BFS", null, false) {
                                            // rd_btn_mni_pathfinderBFS
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> MazeFrame.this.delegator.setPathFinder(new PathFinder.BFS()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Dijkstra", null, true) {
                                            // rd_btn_mni_pathfinderDijkstra
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeFrame.this.delegator.setPathFinder(new PathFinder.Dijkstra()));
                                            }
                                        });
                                    }
                                }.getElements(); e.hasMoreElements();) {
                                    this.add(e.nextElement());
                                }
                            }
                        });
                        this.add(new JMenu("Generator") {
                            // mn_generatorSelector
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_G);
                                this.setIcon(new ImageIcon(MazeFrame.class.getResource("assets/generatorIcon.gif")));
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    // btn_grp_generatorSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("BackTracker", null, false) {
                                            // rd_btn_mni_generatorBackTracker
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeFrame.this.delegator.setGenerator(new Generator.BackTracker()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("DFS", null, true) {
                                            // rd_btn_mni_generatorDFS
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> MazeFrame.this.delegator.setGenerator(new Generator.DFS()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Prim", null, false) {
                                            // rd_btn_mni_generatorPrim
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> MazeFrame.this.delegator.setGenerator(new Generator.Prim()));
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
            // mnu_menuBar
            private static final long serialVersionUID = 1L;
            {
                this.add(new JMenu("File") {
                    // mn_menuFile
                    private static final long serialVersionUID = 1L;
                    {
                        this.setMnemonic(KeyEvent.VK_F);
                        this.add(new JMenuItem("Open", new ImageIcon(MazeFrame.class.getResource("assets/openIcon.gif"))) {
                            // mni_fileOpen
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_O);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeFrame.this.delegator.readMaze());
                            }
                        });
                        // spr_menuFile
                        this.add(new JSeparator(SwingConstants.HORIZONTAL));
                        this.add(new JMenuItem("Save", new ImageIcon(MazeFrame.class.getResource("assets/saveIcon.gif"))) {
                            // mni_fileSave
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_S);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeFrame.this.delegator.writeMaze());
                            }
                        });
                    }
                });
                this.add(new JMenu("Edit") {
                    // mn_menuEdit
                    private static final long serialVersionUID = 1L;
                    {
                        this.setMnemonic(KeyEvent.VK_E);
                        this.add(new JMenu("Grid") {
                            // mn_editGrid
                            private static final long serialVersionUID = 1L;
                            {
                                this.setIcon(new ImageIcon(MazeFrame.class.getResource("assets/gridIcon.gif")));
                                this.add(new JMenuItem("Clear", new ImageIcon(MazeFrame.class.getResource("assets/clearIcon.gif"))) {
                                    // mni_gridClear
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setMnemonic(KeyEvent.VK_Z);
                                        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
                                        this.addActionListener(e -> MazeFrame.this.delegator.reset());
                                    }
                                });
                                this.add(new JMenuItem("Refresh", new ImageIcon(MazeFrame.class.getResource("assets/refreshIcon.gif"))) {
                                    // mni_gridRefresh
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setMnemonic(KeyEvent.VK_R);
                                        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
                                        this.addActionListener(e -> MazeFrame.this.delegator.clear());
                                    }
                                });
                            }
                        });
                        // spr_menuEdit
                        this.add(new JSeparator(SwingConstants.HORIZONTAL));
                        this.add(new JMenu("Preferences") {
                            // mn_editPreferences
                            private static final long serialVersionUID = 1L;
                            {
                                this.setIcon(new ImageIcon(MazeFrame.class.getResource("assets/preferencesIcon.gif")));
                                this.add(new JCheckBoxMenuItem("Arrows", null, false) {
                                    // chb_mni_preferencesArrows
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> MazeFrame.this.delegator.cycleArrows());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Diagonals", null, true) {
                                    // chb_mni_preferencesDiagonals
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> MazeFrame.this.delegator.cycleDiagonals());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Status Bar", null, true) {
                                    // chb_mni_preferencesStatusBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> MazeFrame.this.delegator.cycleStatusComponent());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Node Tree", null, false) {
                                    // chb_mni_preferencesWrapper
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> MazeFrame.this.delegator.cycleSplitComponent());
                                    }
                                });
                            }
                        });
                    }
                });
                this.add(new JMenu("Run") {
                    // mn_menuRun
                    private static final long serialVersionUID = 1L;
                    {
                        this.setMnemonic(KeyEvent.VK_R);
                        this.add(new JMenuItem("PathFinder", new ImageIcon(MazeFrame.class.getResource("assets/pathfinderRunIcon.gif"))) {
                            // mni_runPathFinder
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_1);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeFrame.this.delegator.awakePathFinder());
                            }
                        });
                        this.add(new JMenuItem("Generator", new ImageIcon(MazeFrame.class.getResource("assets/generatorRunIcon.gif"))) {
                            // mni_runGenerator
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_2);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeFrame.this.delegator.awakeGenerator());
                            }
                        });
                    }
                });
            }
        }, BorderLayout.NORTH);
    }

    /**
     * Initialize <code>javax.swing.JFrame</code> custom parameters.
     */
    private final void initFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(450, 525));
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.pack();
    }

    /**
     * Release a new <code>javax.swing.JPopupMenu</code> selection changer.
     *
     * @param cell CellPanel
     * @return JPopupMenu
     * @throws InvalidParameterException if (cell == null)
     */
    public final JPopupMenu releaseCellPopup(final CellPanel cell) throws InvalidParameterException {
        if (cell == null)
            throw new InvalidParameterException("Invalid cell...");
        return new FocusedPopup(this) {
            private static final long serialVersionUID = 1L;
            {
                this.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                        cell.setFocused(true);
                    }
                    @Override
                    public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
                        cell.setFocused(false);
                    }
                    @Override
                    public void popupMenuCanceled(final PopupMenuEvent e) {
                    }
                });
                this.add(new JMenuItem("Start", new ImageIcon(MazeFrame.class.getResource("assets/startIcon.gif"))) {
                    private static final long serialVersionUID = 1L;
                    {
                        this.addActionListener(e -> cell.getAncestor().setStart(cell));
                    }
                });
                this.add(new JMenuItem("End", new ImageIcon(MazeFrame.class.getResource("assets/endIcon.gif"))) {
                    private static final long serialVersionUID = 1L;
                    {
                        this.addActionListener(e -> cell.getAncestor().setEnd(cell));
                    }
                });
            }
        };
    }

    /**
     * Return current <code>app.controller.MazeDelegator</code> instance.
     *
     * @return MazeDelegator
     */
    public final MazeDelegator getDelegator() {
        return this.delegator;
    }

    /**
     * Set current <code>app.controller.MazeDelegator</code> instance.
     *
     * @param delegator MazeDelegator
     */
    public final void setDelegator(final MazeDelegator delegator) {
        this.delegator = delegator;
    }

    /**
     * Retun current <code>javax.swing.JTree</code> instance.
     *
     * @return JTree
     */
    public final JTree getTreeComponent() {
        return this.treeComponent;
    }

    /**
     * Set current <code>javax.swing.JTree</code> instance.
     */
    public final void setTreeComponent(final JTree treeComponent) {
        this.treeComponent = Objects.requireNonNull(treeComponent, "'treeComponent' must not be null");
    }

    /**
     * Return current <code>javax.swing.JLabel</code> instance.
     *
     * @return JLabel
     */
    public final JLabel getStatusComponent() {
        return this.statusComponent;
    }

    /**
     * Set current <code>javax.swing.JLabel</code> instance.
     */
    public final void setStatusComponent(final JLabel statusComponent) {
        this.statusComponent = Objects.requireNonNull(statusComponent, "'statusComponent' must not be null");
    }

    /**
     * Return current <code>javax.swing.JSplitPane</code> instance.
     *
     * @return JSplitPane
     */
    public final JSplitPane getSplitComponent() {
        return this.splitComponent;
    }

    /**
     * Set current <code>javax.swing.JSplitPane</code> instance.
     */
    public final void setSplitComponent(final JSplitPane splitComponent) {
        this.splitComponent = Objects.requireNonNull(splitComponent, "'splitComponent' must not be null");
    }

}
