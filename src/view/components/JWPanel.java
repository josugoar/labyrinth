package src.view.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

public class JWPanel extends JPanel {

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
     * Add multiple child Component to panel
     *
     * @param comps List<Component>
     */
    public final void addJW(final Collection<Component> comps) {
        for (final Component comp : comps) {
            this.add(comp);
        }
    }

}
