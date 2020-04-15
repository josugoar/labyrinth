package app.maze.view.components.widget.decorator;

import javax.swing.JMenu;

import app.maze.view.components.widget.factory.WidgetFactory;

public class MenuDecorator extends JMenu {

    private static final long serialVersionUID = 1L;

    public MenuDecorator(final String text, final String fileName, final Integer mnemonic) {
        super(text);
        if (fileName != null)
            setIcon(WidgetFactory.createIcon(fileName));
        if (mnemonic != null)
            setMnemonic(mnemonic);
    }

    public MenuDecorator(final String text, final String fileName) {
        this(text, fileName, null);
    }

    public MenuDecorator(final String text, final Integer mnemonic) {
        this(text, null, mnemonic);
    }

}
