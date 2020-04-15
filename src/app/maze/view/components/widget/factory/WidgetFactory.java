package app.maze.view.components.widget.factory;

import java.io.Serializable;
import java.util.Objects;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import app.maze.view.MazeView;


public final class WidgetFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Icon createIcon(final String fileName) {
        Objects.requireNonNull(fileName, "Resource must not be null...");
        return new ImageIcon(MazeView.class.getResource("assets/" + fileName));
    }

}
