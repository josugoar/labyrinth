package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import app.controller.Cell;
import app.controller.MazeController;
import app.controller.MazeModel;
import app.model.PathFinder;
import app.view.components.JWButton;

public class MazeView extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    private final MazeController controller = new MazeController(this);

    {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (final Exception e) {
            System.out.println("Non-supported look and feel...");
        }
    }

    public MazeView() {
        super("MazeApp");
        this.initView();
        this.initFrame();
    }

    private final void initView() {
        this.add(new JPanel(new BorderLayout(0, 0)) {
            // pnl_viewWrapper
            private static final long serialVersionUID = 1L;
            {
                this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane() {
                    // scr_nodeTree
                    private static final long serialVersionUID = 1L;
                    {
                        this.setMinimumSize(new Dimension(100, this.getMinimumSize().height));
                        this.setVisible(false);
                        this.setViewportView(new JTree(new DefaultMutableTreeNode("Start")) {
                            // tre_nodeTree
                            private static final long serialVersionUID = 1L;
                            {
                                controller.setNodeTree(this);
                            }
                            {
                                this.setShowsRootHandles(true);
                            }
                        });
                    }
                }, new JPanel(new BorderLayout(0, 0)) {
                    // pnl_gridPanelWrapper
                    private static final long serialVersionUID = 1L;
                    {
                        this.add(new MazeModel(controller.getDimension(), controller.getDimension()) {
                            // pnl_gridPanel
                            private static final long serialVersionUID = 1L;
                            {
                                controller.setGridPanel(this);
                            }
                            {
                                this.setBackground(Color.WHITE);
                                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                            }
                        }, BorderLayout.CENTER);
                        this.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)) {
                            // pnl_statusBar
                            private static final long serialVersionUID = 1L;
                            {
                                this.add(new JLabel("MazeApp") {
                                    // lbl_statusBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        controller.setStatusLabel(this);
                                    }
                                });
                            }
                        }, BorderLayout.SOUTH);
                    }
                }) {
                    // spl_viewWrapper
                    private static final long serialVersionUID = 1L;
                    {
                        controller.setViewWrapper(this);
                    }
                    {
                        this.setBorder(null);
                        this.setEnabled(false);
                    }
                }, BorderLayout.CENTER);
                this.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)) {
                    // pnl_toolBarWrapper
                    private static final long serialVersionUID = 1L;
                    {
                        this.add(new JPanel() {
                            // pnl_toolBar
                            private static final long serialVersionUID = 1L;
                            {
                                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                                // TODO: Add JWToolBar
                                this.add(new JToolBar(SwingConstants.VERTICAL) {
                                    // tlb_featureBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setFloatable(true);
                                        this.add(new JPanel(new GridLayout(3, 1)) {
                                            // pnl_featuresBar
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                                                this.add(new JButton("", new ImageIcon(
                                                        MazeView.class.getResource("assets/dimensionIcon.gif"))) {
                                                    // btn_dimensionSelector
                                                    private static final long serialVersionUID = 1L;
                                                    private final JSlider dimensionSelector = new JSlider(
                                                            JSlider.HORIZONTAL, 1, 100, controller.getDimension()) {
                                                        // sld_dimensionSelector
                                                        private static final long serialVersionUID = 1L;
                                                        {
                                                            this.setPreferredSize(
                                                                    new Dimension(100, this.getPreferredSize().height));
                                                            this.addChangeListener(
                                                                    e -> controller.setDimension(this.getValue()));
                                                        }
                                                    };
                                                    {
                                                        this.setBorder(new EmptyBorder(5, 5, 5, 5));
                                                        this.setContentAreaFilled(false);
                                                        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                                        this.setFocusPainted(false);
                                                        this.setToolTipText("Dimension");
                                                        this.addActionListener(e -> new JPopupMenu("") {
                                                            // pmn_dimensionSelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(dimensionSelector);
                                                            }
                                                        }.show(this, -this.dimensionSelector.getPreferredSize().width,
                                                                (this.getPreferredSize().height - this.dimensionSelector
                                                                        .getPreferredSize().height) / 5));
                                                    }
                                                });
                                                this.add(new JButton("", new ImageIcon(
                                                        MazeView.class.getResource("assets/speedIcon.gif"))) {
                                                    // btn_speedSlector
                                                    private static final long serialVersionUID = 1L;
                                                    private final JSlider speedSlector = new JSlider(JSlider.HORIZONTAL,
                                                            1, 100, 100 - controller.getSpeed()) {
                                                        // sld_speedSlector
                                                        private static final long serialVersionUID = 1L;
                                                        {
                                                            this.setPreferredSize(
                                                                    new Dimension(100, this.getPreferredSize().height));
                                                            this.addChangeListener(e -> controller
                                                                    .setSpeed(this.getMaximum() - this.getValue()));
                                                        }
                                                    };
                                                    {
                                                        this.setBorder(new EmptyBorder(5, 5, 5, 5));
                                                        this.setContentAreaFilled(false);
                                                        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                                        this.setFocusPainted(false);
                                                        this.setToolTipText("Speed");
                                                        this.addActionListener(e -> new JPopupMenu("") {
                                                            // pmn_speedSlector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(speedSlector);
                                                            }
                                                        }.show(this, -this.speedSlector.getPreferredSize().width,
                                                                (this.getPreferredSize().height
                                                                        - this.speedSlector.getPreferredSize().height)
                                                                        / 5));
                                                    }
                                                });
                                                this.add(new JButton("", new ImageIcon(
                                                        MazeView.class.getResource("assets/densityIcon.gif"))) {
                                                    // btn_densitySelector
                                                    private static final long serialVersionUID = 1L;
                                                    private final JSlider densitySelector = new JSlider(
                                                            JSlider.HORIZONTAL, 1, 100, controller.getDensity()) {
                                                        // sld_densitySelector
                                                        private static final long serialVersionUID = 1L;
                                                        {
                                                            this.setPreferredSize(
                                                                    new Dimension(100, this.getPreferredSize().height));
                                                            this.addChangeListener(
                                                                    e -> controller.setDensity(this.getValue()));
                                                        }
                                                    };
                                                    {
                                                        this.setBorder(new EmptyBorder(5, 5, 5, 5));
                                                        this.setContentAreaFilled(false);
                                                        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                                        this.setFocusPainted(false);
                                                        this.setToolTipText("Speed");
                                                        this.addActionListener(e -> new JPopupMenu("") {
                                                            // pmn_densitySelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(densitySelector);
                                                            }
                                                        }.show(this, -this.densitySelector.getPreferredSize().width,
                                                                (this.getPreferredSize().height - this.densitySelector
                                                                        .getPreferredSize().height) / 5));
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
                                        this.setFloatable(true);
                                        this.add(new JPanel(new GridLayout(2, 1)) {
                                            // pnl_runBar
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                                                this.add(new JButton("", new ImageIcon(
                                                        MazeView.class.getResource("assets/pathfinderRunIcon.gif"))) {
                                                    // btn_runPathFinder
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.setBorder(new EmptyBorder(5, 5, 5, 5));
                                                        this.setContentAreaFilled(false);
                                                        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                                        this.setFocusPainted(false);
                                                        this.setToolTipText("Run PathFinder");
                                                        this.addActionListener(e -> controller.awakePathFinder());
                                                    }
                                                });
                                                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                                                this.add(new JButton("", new ImageIcon(
                                                        MazeView.class.getResource("assetsgeneratorRunIcon.gif"))) {
                                                    // btn_runGenerator
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.setBorder(new EmptyBorder(5, 5, 5, 5));
                                                        this.setContentAreaFilled(false);
                                                        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                                        this.setFocusPainted(false);
                                                        this.setToolTipText("Run Generator");
                                                        this.addActionListener(e -> controller.awakeGenerator());
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
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/pathfinderIcon.gif")));
                                this.setMnemonic(KeyEvent.VK_P);
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    // btn_grp_pathfinderSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("A Star") {
                                            // rd_btn_mni_pathfinderAStar
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e ->
                                                // controller.setPathFinder(PathFinder.Algorithm.ASTAR));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("BFS") {
                                            // rd_btn_mni_pathfinderBFS
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e ->
                                                // controller.setPathFinder(PathFinder.Algorithm.BFS));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Dijkstra", true) {
                                            // rd_btn_mni_pathfinderDijkstra
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> controller
                                                        // .setPathFinder(PathFinder.Algorithm.DIJKSTRA));
                                                        .setPathFinder(new PathFinder.Dijkstra()));
                                            }
                                        });
                                    }
                                }.getElements();e.hasMoreElements();) {
                                    this.add(e.nextElement());
                                }
                            }
                        });
                        this.add(new JMenu("Generator") {
                            // mn_generatorSelector
                            private static final long serialVersionUID = 1L;
                            {
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/generatorIcon.gif")));
                                this.setMnemonic(KeyEvent.VK_G);
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    // btn_grp_generatorSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("BackTracker") {
                                            // rd_btn_mni_generatorBackTracker
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e ->
                                                // controller.setGenerator(Generator.Algorithm.BACKTRACKER);
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("DPS", true) {
                                            // rd_btn_mni_generatorDPS
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e ->
                                                // controller.setGenerator(Generator.Algorithm.DPS);
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Prim") {
                                            // rd_btn_mni_generatorPrim
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e ->
                                                // controller.setGenerator(Generator.Algorithm.PRIM);
                                            }
                                        });
                                    }
                                }.getElements();e.hasMoreElements();) {
                                    this.add(e.nextElement());
                                }
                            }
                        });
                        this.add(new JMenu("Mode") {
                            // mn_modeSelector
                            private static final long serialVersionUID = 1L;
                            private final ButtonGroup modeGroup = new ButtonGroup() {
                                // btn_grp_modeSelector
                                private static final long serialVersionUID = 1L;
                                {
                                    this.add(new JRadioButtonMenuItem("Start") {
                                        // rd_btn_mni_modeStart
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.addItemListener(e -> controller.setMode(Cell.State.START));
                                        }
                                    });
                                    this.add(new JRadioButtonMenuItem("End") {
                                        // rd_btn_mni_modeEnd
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.addItemListener(e -> controller.setMode(Cell.State.END));
                                        }
                                    });
                                    this.add(new JRadioButtonMenuItem("Obstacle", true) {
                                        // rd_btn_mni_modeObstacle
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.addItemListener(e -> controller.setMode(Cell.State.OBSTACLE));
                                        }
                                    });
                                    this.add(new JRadioButtonMenuItem("Empty") {
                                        // rd_btn_mni_modeEmpty
                                        private static final long serialVersionUID = 1L;
                                        {
                                            this.addItemListener(e -> controller.setMode(Cell.State.EMPTY));
                                        }
                                    });
                                }
                            };
                            {
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/modeIcon.gif")));
                                this.setMnemonic(KeyEvent.VK_M);
                                this.add(new JMenu("Draw") {
                                    // mn_drawSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setIcon(new ImageIcon(MazeView.class.getResource("assets/drawIcon.gif")));
                                        for (final Enumeration<AbstractButton> e = modeGroup.getElements(); e
                                                .hasMoreElements();) {
                                            this.add(e.nextElement());
                                        }
                                    }
                                });
                                // spr_modeSelector
                                this.add(new JSeparator());
                                this.add(new JRadioButtonMenuItem("Node") {
                                    // rd_btn_mni_modeNode
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setIcon(new ImageIcon(MazeView.class.getResource("assets/nodeIcon.gif")));
                                        modeGroup.add(this);
                                        this.addItemListener(e -> controller.setMode(null));
                                    }
                                });
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
                        this.add(new JMenuItem("Open",
                                new ImageIcon(MazeView.class.getResource("assets/openIcon.gif"))) {
                            // mni_fileOpen
                            private static final long serialVersionUID = 1L;
                            {
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
                                this.setMnemonic(KeyEvent.VK_O);
                                this.addActionListener(e -> System.out.println("Open"));
                            }
                        });
                        // spr_menuFile
                        this.add(new JSeparator());
                        this.add(new JMenuItem("Save",
                                new ImageIcon(MazeView.class.getResource("assets/saveIcon.gif"))) {
                            // mni_fileSave
                            private static final long serialVersionUID = 1L;
                            {
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
                                this.setMnemonic(KeyEvent.VK_S);
                                this.addActionListener(e -> System.out.println("Save"));
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
                                this.add(new JMenuItem("Clear",
                                        new ImageIcon(MazeView.class.getResource("assets/clearIcon.gif"))) {
                                    // mni_gridClear
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setAccelerator(
                                                KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
                                        this.setMnemonic(KeyEvent.VK_C);
                                        this.addActionListener(e -> System.out.println("Clear"));
                                    }
                                });
                                this.add(new JMenuItem("Reset",
                                        new ImageIcon(MazeView.class.getResource("assets/resetIcon.gif"))) {
                                    // mni_gridReset
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setAccelerator(
                                                KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
                                        this.setMnemonic(KeyEvent.VK_Z);
                                        this.addActionListener(e -> controller.resetGridPanel());
                                    }
                                });
                            }
                        });
                        // spr_menuEdit
                        this.add(new JSeparator());
                        this.add(new JMenu("View") {
                            // mn_editView
                            private static final long serialVersionUID = 1L;
                            {
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/viewIcon.gif")));
                                this.add(new JCheckBoxMenuItem("Arrows") {
                                    // chb_mni_viewArrows
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> controller.setArrows(!controller.isArrows()));
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Diagonals", true) {
                                    // chb_mni_viewDiagonals
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> controller.setDiagonals(!controller.isDiagonals()));
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Status Bar", true) {
                                    // chb_mni_viewStatusBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> {
                                            if (e.getStateChange() == ItemEvent.SELECTED)
                                                controller.getStatusLabel().setVisible(true);
                                            else
                                                controller.getStatusLabel().setVisible(false);
                                        });
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Node Tree") {
                                    // chb_mni_viewNodeTree
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> {
                                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                                controller.getViewWrapper().getLeftComponent().setVisible(true);
                                                controller.getViewWrapper().setEnabled(true);
                                            } else {
                                                controller.getViewWrapper().getLeftComponent().setVisible(false);
                                                controller.getViewWrapper().setEnabled(false);
                                            }
                                            controller.getViewWrapper().setDividerLocation(-1);
                                        });
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
                        this.add(new JMenuItem("PathFinder",
                                new ImageIcon(MazeView.class.getResource("assets/pathfinderRunIcon.gif"))) {
                            // mni_runPathFinder
                            private static final long serialVersionUID = 1L;
                            {
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
                                this.setMnemonic(KeyEvent.VK_1);
                                this.addActionListener(e -> controller.awakePathFinder());
                            }
                        });
                        this.add(new JMenuItem("Generator",
                                new ImageIcon(MazeView.class.getResource("assets/generatorRunIcon.gif"))) {
                            // mni_runGenerator
                            private static final long serialVersionUID = 1L;
                            {
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
                                this.setMnemonic(KeyEvent.VK_2);
                                this.addActionListener(e -> controller.awakeGenerator());
                            }
                        });
                    }
                });
            }
        }, BorderLayout.NORTH);
    }

    private final void initFrame() {
        this.setFocusable(true);
        this.setMinimumSize(new Dimension(450, 400));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    @Override
    public final void run() {
        this.setVisible(true);
    }

    public final MazeController getController() {
        return this.controller;
    }

}
