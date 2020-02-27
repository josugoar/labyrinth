package Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * Personalized wrapper/helper methods for GUI components
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

    public static class JWPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        /**
         * Enclose JPanel LayoutManager, Component and Dimension
         *
         * @param mgr           LayoutManager
         * @param components    List<Component>
         * @param preferredSize Dimension
         */
        public JWPanel(final LayoutManager mgr, final List<Component> components, final Dimension preferredSize) {
            super(mgr);
            if (components != null) {
                this.addJW(components);
            }
            if (preferredSize != null) {
                this.setPreferredSize(preferredSize);
            }
        }

        /**
         * Enclose JPanel LayoutManager and Component
         *
         * @param mgr           LayoutManager
         * @param components    List<Component>
         */
        public JWPanel(final LayoutManager mgr, final List<Component> components) {
            this(mgr, components, null);
        }

        /**
         * Enclose JPanel LayoutManager and Dimension
         *
         * @param mgr           LayoutManager
         * @param preferredSize Dimension
         */
        public JWPanel(final LayoutManager mgr, final Dimension preferredSize) {
            this(mgr, null, preferredSize);
        }

        /**
         * Enclose JPanel LayoutManager
         *
         * @param mgr        LayoutManager
         */
        public JWPanel(final LayoutManager mgr) {
            this(mgr, null, null);
        }

        /**
         * Add multiple child components to panel
         *
         * @param components List<Component>
         */
        public final void addJW(final List<Component> components) {
            for (final Component component : components) {
                this.add(component);
            }
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
            this.setOneTouchExpandable(false);
        }

    }

    public static final class JWGridLayout extends JWPanel {

        private static final long serialVersionUID = 1L;

        /**
         * Create GridLayout JPanel of given shape
         *
         * @param rows          int
         * @param cols          int
         * @param preferredSize Dimension
         */
        public JWGridLayout(final int rows, final int cols, final Dimension preferredSize) {
            super(new GridLayout(rows, cols, 0, 0), preferredSize);
            final List<Component> cells = new ArrayList<Component>(rows * cols);
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    cells.add(new Cell());
                }
            }
            this.addJW(cells);
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
                        Cell.this.setState(State.OBSTACLE);
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
