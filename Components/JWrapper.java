package Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.MouseInputAdapter;

/**
 * Personalized wrapper/helper methods for GUI components
 */
@FunctionalInterface
public abstract interface JWrapper {

    /**
     * Single Component multiple reference pointer
     *
     * @param component Component
     */
    abstract Component JWComponent(final Component component);

    public static class JWPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        /**
         * Enclose JPanel LayoutManager, Dimension, Component and MouseInputAdapter
         *
         * @param mgr           LayoutManager
         * @param preferredSize Dimension
         * @param components    List<Component>
         * @param listener      MouseInputAdapter
         */
        public JWPanel(final LayoutManager mgr, final Dimension preferredSize, final List<Component> components,
                final MouseInputAdapter l) {
            super();
            this.setLayout(mgr);
            this.setPreferredSize(preferredSize);
            this.addJW(components);
            if (l != null) {
                this.addMouseListener(l);
            }
        }

        /**
         * Enclose JPanel LayoutManager, Dimension and Component
         *
         * @param mgr           LayoutManager
         * @param preferredSize Dimension
         * @param components    List<Component>
         */
        public JWPanel(final LayoutManager mgr, final Dimension preferredSize, final List<Component> components) {
            this(mgr, preferredSize, components, null);
        }

        /**
         * Add multiple child components to panel
         *
         * @param components List<Component>
         */
        public final void addJW(final List<Component> components) {
            if (components != null) {
                for (final Component component : components) {
                    this.add(component);
                }
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
            super(new GridLayout(rows, cols, 0, 0), preferredSize, null);
            final List<Component> cells = new ArrayList<Component>(rows * cols);
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    final JPanel cell = new JPanel();
                    cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    cells.add(cell);
                }
            }
            this.addJW(cells);
        }

    }

}
