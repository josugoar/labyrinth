import javax.swing.SwingUtilities;

import app.view.MazeView;

public class Run {

    public static final void main(final String[] args) {
        SwingUtilities.invokeLater(new MazeView());
    }

}
