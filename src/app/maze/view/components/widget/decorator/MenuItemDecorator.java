package app.maze.view.components.widget.decorator;

import java.awt.event.InputEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import app.maze.view.MazeView;

/**
 * <code>javax.swing.JMenuItem</code> decorator wrapper, extending
 * <code>javax.swing.JMenuItem</code>.
 *
 * @see javax.swing.JMenuItem JMenuItem
 */
public class MenuItemDecorator extends JMenuItem {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new <code>javax.swing.JMenuItem</code> mnemonic decorator wrapper.
     *
     * @param text     String
     * @param fileName String
     * @param mnemonic Integer
     */
    public MenuItemDecorator(final String text, final String fileName, final Integer mnemonic) {
        super(text, new ImageIcon(MazeView.class.getResource("assets/" + fileName)));
        if (mnemonic == null)
            return;
        setMnemonic(mnemonic);
        setAccelerator(KeyStroke.getKeyStroke(mnemonic, InputEvent.CTRL_MASK));
    }

    /**
     * Create a new <code>javax.swing.JMenuItem</code> decorator wrapper.
     *
     * @param text     String
     * @param fileName String
     */
    public MenuItemDecorator(final String text, final String fileName) {
        this(text, fileName, null);
    }

}
