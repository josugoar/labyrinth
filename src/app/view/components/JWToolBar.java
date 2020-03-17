package app.view.components;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

public final class JWToolBar extends JToolBar {

    private static final long serialVersionUID = 1L;

    private final int components;

    public JWToolBar(int orientation, int components) {
        super(orientation);
        this.components = components;
        this.initToolbar();
    }

    private final void initToolbar() {
        this.setFloatable(false);
        this.add(new JPanel() {
            private static final long serialVersionUID = 1L;
            {
                this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
                if (JWToolBar.this.getOrientation() == 0) {
                    this.setLayout(new GridLayout(JWToolBar.this.components, 1));
                } else {
                    this.setLayout(new GridLayout(1, JWToolBar.this.components));
                }
                for (int i = 0; i < JWToolBar.this.components; i++) {
                    this.add(new JWButton());
                }
            }
        });
    }

}
