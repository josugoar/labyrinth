package app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

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
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import app.controller.Cell;
import app.controller.MazeController;
import app.model.Generator;
import app.model.PathFinder;
import app.view.components.JWButton;
import app.view.components.JWSlider;

public class MazeView extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    private final MazeController controller;

    {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (final Exception e) {
            System.out.println("Unsupported look and feel...");
        }
    }

    public MazeView(final MazeController controller) {
        super("MazeApp");
        this.controller = controller;
        this.initView();
    }

    private final void initView() {
        this.add(new JPanel(new BorderLayout()) {
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
                                this.setShowsRootHandles(true);
                            }
                            {
                                controller.setNodeTree(this);
                            }
                        });
                    }
                }, new JPanel(new BorderLayout()) {
                    // pnl_modelPanelWrapper
                    private static final long serialVersionUID = 1L;
                    {
                        // pnl_modelPanel
                        this.add(controller.getModel(), BorderLayout.CENTER);
                        this.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {
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
                        this.setEnabled(false);
                        this.setBorder(null);
                    }
                    {
                        controller.setViewWrapper(this);
                    }
                }, BorderLayout.CENTER);
                this.add(new JPanel() {
                    // pnl_toolBarWrapper
                    private static final long serialVersionUID = 1L;
                    {
                        this.add(new JPanel() {
                            // pnl_toolBar
                            private static final long serialVersionUID = 1L;
                            {
                                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                                this.add(new JToolBar(SwingConstants.VERTICAL) {
                                    // tlb_featureBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JPanel(new GridLayout(3, 1)) {
                                            // pnl_featuresBar
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.setBorder(new EtchedBorder());
                                                this.add(new JWButton(new ImageIcon(
                                                        MazeView.class.getResource("assets/dimensionIcon.gif")), "Dimension") {
                                                    // btn_dimensionSelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new JPopupMenu() {
                                                            // pmn_dimensionSelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new JWSlider(controller.getDimension()) {
                                                                    // sld_dimensionSelector
                                                                    private static final long serialVersionUID = 1L;
                                                                    {
                                                                        this.addChangeListener(e -> controller.setDimension(this.getValue()));
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                this.add(new JWButton(new ImageIcon(
                                                        MazeView.class.getResource("assets/delayIcon.gif")), "Delay") {
                                                    // btn_delaySelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new JPopupMenu() {
                                                            // pmn_delaySelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new JWSlider(controller.getDelay()) {
                                                                    // sld_delaySelector
                                                                    private static final long serialVersionUID = 1L;

                                                                    {
                                                                        this.addChangeListener(e -> controller.setDelay(this.getValue()));
                                                                    }
                                                                });
                                                            }
                                                        }.show(this, -100, 2));
                                                    }
                                                });
                                                this.add(new JWButton(new ImageIcon(
                                                        MazeView.class.getResource("assets/densityIcon.gif")), "Density") {
                                                    // btn_densitySelector
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> new JPopupMenu() {
                                                            // pmn_densitySelector
                                                            private static final long serialVersionUID = 1L;
                                                            {
                                                                this.add(new JWSlider(controller.getDensity()) {
                                                                    // sld_densitySlector
                                                                    private static final long serialVersionUID = 1L;

                                                                    {
                                                                        this.addChangeListener(e -> controller.setDensity(this.getValue()));
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
                                        this.add(new JPanel(new GridLayout(2, 1)) {
                                            // pnl_runBar
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.setBorder(new EtchedBorder());
                                                this.add(new JWButton(new ImageIcon(
                                                        MazeView.class.getResource("assets/pathfinderRunIcon.gif")), "Run PathFinder") {
                                                    // btn_runPathFinder
                                                    private static final long serialVersionUID = 1L;
                                                    {
                                                        this.addActionListener(e -> controller.awakePathFinder());
                                                    }
                                                });
                                                this.add(new JWButton(new ImageIcon(
                                                        MazeView.class.getResource("assets/generatorRunIcon.gif")), "Run Generator") {
                                                    // btn_runGenerator
                                                    private static final long serialVersionUID = 1L;
                                                    {
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
                                this.setMnemonic(KeyEvent.VK_P);
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/pathfinderIcon.gif")));
                                for (final Enumeration<AbstractButton> e = new ButtonGroup() {
                                    // btn_grp_pathfinderSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.add(new JRadioButtonMenuItem("A Star") {
                                            // rd_btn_mni_pathfinderAStar
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> controller.setPathFinder(new PathFinder.AStar()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("BFS") {
                                            // rd_btn_mni_pathfinderBFS
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> controller.setPathFinder(new PathFinder.BFS()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Dijkstra", true) {
                                            // rd_btn_mni_pathfinderDijkstra
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> controller.setPathFinder(new PathFinder.Dijkstra()));
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
                                        this.add(new JRadioButtonMenuItem("BackTracker") {
                                            // rd_btn_mni_generatorBackTracker
                                            private static final long serialVersionUID = 1L;
                                            {
                                                this.addItemListener(e -> controller.setGenerator(new Generator.BackTracker()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("DPS", true) {
                                            // rd_btn_mni_generatorDPS
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> controller.setGenerator(new Generator.DPS()));
                                            }
                                        });
                                        this.add(new JRadioButtonMenuItem("Prim") {
                                            // rd_btn_mni_generatorPrim
                                            private static final long serialVersionUID = 1L;
                                            {
                                                // this.addItemListener(e -> controller.setGenerator(new Generator.Prim()));
                                            }
                                        });
                                    }
                                }.getElements(); e.hasMoreElements();) {
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
                                this.setMnemonic(KeyEvent.VK_M);
                                this.setIcon(new ImageIcon(MazeView.class.getResource("assets/modeIcon.gif")));
                                this.add(new JMenu("Draw") {
                                    // mn_drawSelector
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setIcon(new ImageIcon(MazeView.class.getResource("assets/drawIcon.gif")));
                                        for (final Enumeration<AbstractButton> e = modeGroup.getElements(); e .hasMoreElements();) {
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
                                        modeGroup.add(this);
                                    }
                                    {
                                        this.setIcon(new ImageIcon(MazeView.class.getResource("assets/nodeIcon.gif")));
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
                                this.setMnemonic(KeyEvent.VK_O);
                                this.addActionListener(e -> System.out.println("Open"));
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
                            }
                        });
                        // spr_menuFile
                        this.add(new JSeparator());
                        this.add(new JMenuItem("Save",
                                new ImageIcon(MazeView.class.getResource("assets/saveIcon.gif"))) {
                            // mni_fileSave
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_S);
                                this.addActionListener(e -> System.out.println("Save"));
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
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
                                        this.setMnemonic(KeyEvent.VK_C);
                                        this.addActionListener(e -> controller.clearGridPanel());
                                        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
                                    }
                                });
                                this.add(new JMenuItem("Reset",
                                        new ImageIcon(MazeView.class.getResource("assets/resetIcon.gif"))) {
                                    // mni_gridReset
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.setMnemonic(KeyEvent.VK_Z);
                                        this.addActionListener(e -> controller.resetGridPanel());
                                        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
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
                                        this.addActionListener(e -> controller.cycleArrows());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Diagonals", true) {
                                    // chb_mni_viewDiagonals
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addActionListener(e -> controller.cycleDiagonals());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Status Bar", true) {
                                    // chb_mni_viewStatusBar
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> controller.cycleStatusLabel());
                                    }
                                });
                                this.add(new JCheckBoxMenuItem("Node Tree") {
                                    // chb_mni_viewWrapper
                                    private static final long serialVersionUID = 1L;
                                    {
                                        this.addItemListener(e -> controller.cycleViewWrapper());
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
                                this.setMnemonic(KeyEvent.VK_1);
                                this.addActionListener(e -> controller.awakePathFinder());
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
                            }
                        });
                        this.add(new JMenuItem("Generator",
                                new ImageIcon(MazeView.class.getResource("assets/generatorRunIcon.gif"))) {
                            // mni_runGenerator
                            private static final long serialVersionUID = 1L;
                            {
                                this.setMnemonic(KeyEvent.VK_2);
                                this.addActionListener(e -> controller.awakeGenerator());
                                this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
                            }
                        });
                    }
                });
            }
        }, BorderLayout.NORTH);
        this.initFrame();
    }

    private final void initFrame() {
        this.setMinimumSize(new Dimension(450, 400));
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public final void run() {
        this.setVisible(true);
    }

    public final MazeController getController() {
        return this.controller;
    }

}
