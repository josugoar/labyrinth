package src.view.components;

import java.awt.Component;

import javax.swing.JSplitPane;

public final class JWSplitPane extends JSplitPane {

    private static final long serialVersionUID = 1L;

    /**
     * Anchor JSplitPane disabling divider
     *
     * @param newOrientation    int
     * @param newLeftComponent  Component
     * @param newRightComponent Component
     */
    public JWSplitPane(final int newOrientation, final Component newLeftComponent, final Component newRightComponent) {
        super(newOrientation, newLeftComponent, newRightComponent);
        this.setEnabled(false);
        this.setDividerLocation(-1);
        this.setOneTouchExpandable(true);
    }

}
