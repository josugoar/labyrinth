package src.view.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

/**
 * An extended <code>javax.swing.JPanel</code> implementation.
 *
 * @see javax.swing.JPanel JPanel
 */
public class JWPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Enclose <code>javax.swing.JPanel</code> LayoutManager, Component and
     * Dimension.
     *
     * @param layout        LayoutManager
     * @param comps         List<Component>
     * @param preferredSize Dimension
     */
    public JWPanel(final LayoutManager layout, final List<Component> comps, final Dimension preferredSize) {
        super(layout);
        this.addJW(comps);
        if (preferredSize != null) {
            this.setPreferredSize(preferredSize);
        }
    }

    /**
     * Enclose <code>javax.swing.JPanel</code> LayoutManager and Component.
     *
     * @param layout LayoutManager
     * @param comps  List<Component>
     */
    public JWPanel(final LayoutManager layout, final List<Component> comps) {
        this(layout, comps, null);
    }

    /**
     * Add multiple child <code>java.awt.Component</code>
     * <code>java.util.Collection<T></code> to <code>javax.swing.JPanel</code>.
     *
     * @param comps List<Component>
     */
    public final <T extends Component> void addJW(final Collection<T> comps) {
        for (final T comp : comps) {
            this.add(comp);
        }
    }

}
