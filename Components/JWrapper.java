package Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * Personalized wrapper/helper methods for GUI components
 *
 * @author JoshGoA
 */
@FunctionalInterface
public abstract interface JWrapper extends Serializable {

    static final long serialVersionUID = 1L;

    /**
     * Single Component multiple reference pointer
     *
     * @param component Component
     */
    abstract Component JWComponent(final Component component);

    public static final class JWSplitPane extends JSplitPane {

        private static final long serialVersionUID = 1L;

        /**
         * Anchor JSplitPane disabling divider
         *
         * @param newOrientation    int
         * @param newLeftComponent  Component
         * @param newRightComponent Component
         */
        public JWSplitPane(final int newOrientation, final Component newLeftComponent,
                final Component newRightComponent) {
            super(newOrientation, newLeftComponent, newRightComponent);
            this.setEnabled(false);
            this.setDividerLocation(-1);
            this.setOneTouchExpandable(true);
        }

    }

    public static final class JWButton extends JButton {

        private static final long serialVersionUID = 1L;

        /**
         * Enclose JButton text, Dimension and ActionListener
         *
         * @param text          String
         * @param preferredSize Dimension
         * @param l             ActionListener
         */
        public JWButton(final String text, final Dimension preferredSize, final ActionListener l) {
            super(text);
            this.setPreferredSize(preferredSize);
            this.addActionListener(l);
        }

    }

    public static class JWPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        /**
         * Enclose JPanel LayoutManager, Component and Dimension
         *
         * @param layout        LayoutManager
         * @param comps         List<Component>
         * @param preferredSize Dimension
         */
        public JWPanel(final LayoutManager layout, final List<Component> comps, final Dimension preferredSize) {
            super(layout);
            if (comps != null) {
                this.addJW(comps);
            }
            if (preferredSize != null) {
                this.setPreferredSize(preferredSize);
            }
        }

        /**
         * Enclose JPanel LayoutManager and Component
         *
         * @param layout LayoutManager
         * @param comps  List<Component>
         */
        public JWPanel(final LayoutManager layout, final List<Component> comps) {
            this(layout, comps, null);
        }

        /**
         * Enclose JPanel LayoutManager and Dimension
         *
         * @param layout        LayoutManager
         * @param preferredSize Dimension
         */
        public JWPanel(final LayoutManager layout, final Dimension preferredSize) {
            this(layout, null, preferredSize);
        }

        /**
         * Enclose JPanel LayoutManager
         *
         * @param layout LayoutManager
         */
        public JWPanel(final LayoutManager layout) {
            this(layout, null, null);
        }

        /**
         * Add multiple child components to panel
         *
         * @param comps List<Component>
         */
        public final void addJW(final Collection<Component> comps) {
            for (final Component comp : comps) {
                this.add(comp);
            }
        }

    }

    // TODO: Move logic to own file
    public static final class JWGridLayout extends JWPanel {

        private static final long serialVersionUID = 1L;

        private final Map<Point, Component> grid;

        /**
         * Create GridLayout JPanel of given shape
         *
         * @param rows          int
         * @param cols          int
         * @param preferredSize Dimension
         */
        public JWGridLayout(final int rows, final int cols, final Dimension preferredSize) {
            super(new GridLayout(rows, cols, 0, 0), preferredSize);
            this.grid = new HashMap<Point, Component>(rows * cols) {
                private static final long serialVersionUID = 1L;
                {
                    for (int row = 0; row < rows; row++) {
                        for (int col = 0; col < cols; col++) {
                            this.put(new Point(row, col), new Cell());
                        }
                    }
                }
            };
            this.addJW(this.getGrid().values());
        }

        public final Map<Point, Component> getGrid() {
            return this.grid;
        }

        public static final class Cell extends JWPanel {

            private static enum State {
                START, END, CURR_NODE, NEXT_NODE, OBSTACLE, EMPTY
            }

            private static final long serialVersionUID = 1L;

            private State state = State.EMPTY;

            public Cell() {
                super(new FlowLayout(), null, null);
                this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                this.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(final MouseEvent e) {
                        switch (((MazeApp) SwingUtilities.getWindowAncestor(Cell.this)).getMode()) {
                            case START:
                                Cell.this.setState(State.START);
                                break;
                            case END:
                                Cell.this.setState(State.END);
                                break;
                            case EMPTY:
                                Cell.this.setState(State.EMPTY);
                                break;
                            default:
                                Cell.this.setState(State.OBSTACLE);
                        }
                    }
                });
            }

            @Override
            protected final void paintComponent(final Graphics g) {
                Color color;
                switch (this.getState()) {
                    case START:
                        color = Color.GREEN;
                        break;
                    case END:
                        color = Color.RED;
                        break;
                    case OBSTACLE:
                        color = Color.BLACK;
                        break;
                    case CURR_NODE:
                        color = Color.CYAN;
                        break;
                    case NEXT_NODE:
                        color = Color.BLUE;
                        break;
                    default:
                        color = Color.WHITE;
                }
                g.setColor(color);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }

            @Override
            public final String toString() {
                return String.format("%s (state: %s)", this.getClass(), this.getState());
            }

            public final State getState() {
                return this.state;
            }

            private final void setState(final State state) {
                this.state = Objects.requireNonNull(state, "'state' must not be null");
                this.repaint();
            }

        }

    }

}
