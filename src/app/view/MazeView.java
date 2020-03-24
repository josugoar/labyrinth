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

import app.controller.MazeController;
import app.model.Generator;
import app.model.PathFinder;
import app.model.components.CellPanel;
import app.view.components.FocusedPopup;
import app.view.components.IconifiedButton;
import app.view.components.RangedSlider;

/**
 * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
 * pivotal <code>app.view.MazeView</code> component, extending
 * <code>java.awt.JFrame</code>.
 *
 * @author JoshGoA
 * @version 0.1
 * @see javax.swing.JFrame JFrame
 */
public class MazeView extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Two-sided <code>app.controller.MazeController</code>
     * <code>app.view.MazeView</code> interaction pipeline.
     *
     * @see app.controller.MazeController MazeController
     */
    private MazeController controller;

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
                    MazeView.this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }
            @Override
            public final void keyReleased(final KeyEvent e) {
                // Disable draw state
                MazeView.this.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    /**
     * Create a new isolated pipeline component.
     */
    public MazeView() { }

    /**
     * Create a new two-sided <code>app.controller.MazeController</code> interaction
     * <code>app.view.MazeView</code> pipeline component.
     *
     * @param controller MazeController
     */
    public MazeView(final MazeController controller) {
        super("MazeApp");
        this.setController(controller);
    }

    /**
     * Display frame on screen.
     */
    public final void display() {
        try {
            this.initComponent();
            this.initFrame();
            this.setVisible(true);
        } catch (final NullPointerException e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Initialize entire <code>java.awt.Component</code> tree structure.
     *
     * @see java.awt.Component Component
     * @throws NullPointerException if (controller == null)
     */
    private final void initComponent() throws NullPointerException {
        if (this.controller == null)
            throw new NullPointerException("MazeController might not hvae been initialized...");
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
                                MazeView.this.controller.setTreeComponent(this);
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
                        this.add(MazeView.this.controller.getModel(), BorderLayout.CENTER);
                        this.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)) {
                            // pnl_statusComponentWrapper
                            private static final long serialVersionUID = 1L;
                            {
                                this.add(new JLabel(MazeView.this.controller.toString(), null, SwingConstants.LEADING) {
                                    // lbl_statusComponent
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addPropertyChangeListener("text",e -> MazeView.this.controller.resetStatusComponent());
                                        MazeView.this.controller.setStatusComponent(this);
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
                        MazeView.this.controller.setSplitComponent(this);
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
                                                        new ImageIcon(MazeView.class.getResource("assets/dimensionIcon.gif"))) {
                                                    // btn_dimensionSelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new FocusedPopup(MazeView.this) {
                                                            // pmn_dimensionSelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new RangedSlider(MazeView.this.controller.getDimension()) {
                                                                    // sld_dimensionSelector
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.addChangeListener(e -> {
                                                                            if (!((JSlider) e.getSource()).getValueIsAdjusting())
                                                                                MazeView.this.controller.setDimension(this.getValue());
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                this.add(new IconifiedButton("Delay",
                                                        new ImageIcon(MazeView.class.getResource("assets/delayIcon.gif"))) {
                                                    // btn_delaySelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new FocusedPopup(MazeView.this) {
                                                            // pmn_delaySelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new RangedSlider(MazeView.this.controller.getDelay()) {
                                                                    // sld_delaySelector
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.addChangeListener(e -> MazeView.this.controller.setDelay(this.getValue()));
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                this.add(new IconifiedButton("Density",
                                                        new ImageIcon(MazeView.class.getResource("assets/densityIcon.gif"))) {
                                                    // btn_densitySelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new FocusedPopup(MazeView.this) {
                                                            // pmn_densitySelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new RangedSlider(MazeView.this.controller.getDensity()) {
                                                                    // sld_densitySelector
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.addChangeListener(e -> MazeView.this.controller.setDensity(this.getValue()));
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
                                                        new ImageIcon(MazeView.class.getResource("assets/pathfinderRunIcon.gif"))) {
                                                    // btn_runPathFinder
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> MazeView.this.controller.runPathFinder());
                                                    }
                                                });
                                                this.add(new IconifiedButton("Run Generator",
                                                        new ImageIcon(MazeView.class.getResource("assets/generatorRunIcon.gif"))) {
                                                    // btn_runGenerator
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> MazeView.this.controller.runGenerator());
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
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/pathfinderIcon.gif")));
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    // btn_grp_pathfinderSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("A Star", null, false) {
                                            // rd_btn_mni_pathfinderAStar
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> MazeView.this.controller.setPathFinder(new PathFinder.AStar()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("BFS", null, false) {
                                            // rd_btn_mni_pathfinderBFS
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> MazeView.this.controller.setPathFinder(new PathFinder.BFS()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Dijkstra", null, true) {
                                            // rd_btn_mni_pathfinderDijkstra
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.controller.setPathFinder(new PathFinder.Dijkstra()));
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
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/generatorIcon.gif")));
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    // btn_grp_generatorSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("BackTracker", null, false) {
                                            // rd_btn_mni_generatorBackTracker
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> MazeView.this.controller.setGenerator(new Generator.BackTracker()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("DFS", null, true) {
                                            // rd_btn_mni_generatorDFS
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> MazeView.this.controller.setGenerator(new Generator.DFS()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Prim", null, false) {
                                            // rd_btn_mni_generatorPrim
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> MazeView.this.controller.setGenerator(new Generator.Prim()));
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
                        this.add(new JMenuItem("Open", new ImageIcon(MazeView.class.getResource("assets/openIcon.gif"))) {
                            // mni_fileOpen
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_O);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeView.this.controller.readMaze());
                            }
                        });
                        // spr_menuFile
                        this.add(new JSeparator(SwingConstants.HORIZONTAL));
                        this.add(new JMenuItem("Save", new ImageIcon(MazeView.class.getResource("assets/saveIcon.gif"))) {
                            // mni_fileSave
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_S);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeView.this.controller.writeMaze());
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
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/gridIcon.gif")));
                                this.add(new JMenuItem("Clear", new ImageIcon(MazeView.class.getResource("assets/clearIcon.gif"))) {
                                    // mni_gridClear
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setMnemonic(KeyEvent.VK_Z);
                                        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
                                        this.addActionListener(e -> MazeView.this.controller.reset());
                                    }
                                });
                                this.add(new JMenuItem("Refresh", new ImageIcon(MazeView.class.getResource("assets/refreshIcon.gif"))) {
                                    // mni_gridRefresh
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setMnemonic(KeyEvent.VK_R);
                                        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
                                        this.addActionListener(e -> {
                                            // TODO: Generalize
                                            try {
                                                MazeView.this.controller.clear();
                                            } catch (final NullPointerException l) {
                                                System.err.println(l.toString());
                                            }
                                        });
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
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/preferencesIcon.gif")));
                                this.add(new JCheckBoxMenuItem("Arrows", null, false) {
                                    // chb_mni_preferencesArrows
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> MazeView.this.controller.cycleArrows());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Diagonals", null, true) {
                                    // chb_mni_preferencesDiagonals
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> MazeView.this.controller.cycleDiagonals());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Status Bar", null, true) {
                                    // chb_mni_preferencesStatusBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> MazeView.this.controller.cycleStatusComponent());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Node Tree", null, false) {
                                    // chb_mni_preferencesWrapper
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> MazeView.this.controller.cycleSplitComponent());
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
                        this.add(new JMenuItem("PathFinder", new ImageIcon(MazeView.class.getResource("assets/pathfinderRunIcon.gif"))) {
                            // mni_runPathFinder
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_1);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeView.this.controller.runPathFinder());
                            }
                        });
                        this.add(new JMenuItem("Generator", new ImageIcon(MazeView.class.getResource("assets/generatorRunIcon.gif"))) {
                            // mni_runGenerator
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_2);
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
                                this.addActionListener(e -> MazeView.this.controller.runGenerator());
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
                        cell.setSelected(true);
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
                        cell.setSelected(false);
                    }

                    @Override
                    public void popupMenuCanceled(final PopupMenuEvent e) {
                    }
                });
                this.add(new JMenuItem("Start", new ImageIcon(MazeView.class.getResource("assets/startIcon.gif"))) {
                    private static final long serialVersionUID = 1L;
                    {
                        this.addActionListener(e -> cell.getAncestor().setStart(cell));
                    }
                });
                this.add(new JMenuItem("End", new ImageIcon(MazeView.class.getResource("assets/endIcon.gif"))) {
                    private static final long serialVersionUID = 1L;
                    {
                        this.addActionListener(e -> cell.getAncestor().setEnd(cell));
                    }
                });
            }
        };
    }

    /**
     * Return current <code>app.controller.MazeController</code> instance.
     *
     * @return MazeController
     */
    public final MazeController getController() {
        return this.controller;
    }

    /**
     * Set current <code>app.controller.MazeController</code> instance.
     *
     * @param controller MazeController
     */
    public final void setController(final MazeController controller) {
        this.controller = Objects.requireNonNull(controller, "'controller' must not be null");
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.controller == null) ? 0 : this.controller.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final MazeView other = (MazeView) obj;
        if (this.controller == null) {
            if (other.controller != null)
                return false;
        } else if (!this.controller.equals(other.controller))
            return false;
        return true;
    }

}
