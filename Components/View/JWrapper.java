package Components.View;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


/**
 * Personalized wrapper/helper components for GUI layouts
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

}
