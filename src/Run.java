import javax.swing.SwingUtilities;

import app.Maze;

public class Run {

    public static final void main(final String[] args) {
        SwingUtilities.invokeLater(new Maze());
    }

}
