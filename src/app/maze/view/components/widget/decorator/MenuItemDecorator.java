package app.maze.view.components.widget.decorator;

import java.awt.event.InputEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.maze.view.MazeView;

public class MenuItemDecorator extends JMenuItem {

	private static final long serialVersionUID = 1L;

    public MenuItemDecorator(final String text, final String fileName, final Integer mnemonic) {
        super(text, new ImageIcon(MazeView.class.getResource("assets/" + fileName)));
        if (mnemonic == null)
            return;
        setMnemonic(mnemonic);
        setAccelerator(KeyStroke.getKeyStroke(mnemonic, InputEvent.CTRL_MASK));
    }

    public MenuItemDecorator(final String text, final String fileName) {
        this(text, fileName, null);
    }

}
