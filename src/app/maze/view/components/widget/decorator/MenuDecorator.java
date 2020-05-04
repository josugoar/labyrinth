package app.maze.view.components.widget.decorator;

import javax.swing.JMenu;

import app.maze.view.components.widget.factory.WidgetFactory;

/**
 * <code>javax.swing.JMenu</code> decorator wrapper, extending
 * <code>javax.swing.JMenu</code>.
 *
 * @see javax.swing.JMenu JMenu
 */
public class MenuDecorator extends JMenu {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new <code>javax.swing.JMenu</code> iconyfied mnemonic decorator wrapper.
     *
     * @param text     String
     * @param fileName String
     * @param mnemonic Integer
     */
    public MenuDecorator(final String text, final String fileName, final Integer mnemonic) {
        super(text);
        if (fileName != null)
            setIcon(WidgetFactory.createIcon(fileName));
        if (mnemonic != null)
            setMnemonic(mnemonic);
    }

    /**
     * Create a new <code>javax.swing.JMenu</code> iconyfied decorator wrapper.
     *
     * @param text     String
     * @param fileName String
     */
    public MenuDecorator(final String text, final String fileName) {
        this(text, fileName, null);
    }

    /**
     * Create a new <code>javax.swing.JMenu</code> mnemonic decorator wrapper.
     *
     * @param text     String
     * @param mnemonic Integer
     */
    public MenuDecorator(final String text, final Integer mnemonic) {
        this(text, null, mnemonic);
    }

}
