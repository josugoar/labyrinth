package app;

import app.controller.MazeController;
import app.model.MazeModel;
import app.view.MazeView;

/**
 * Maze architecture initializer.
 */
public final class Maze implements Runnable {

    /**
     * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
     * pivotal <code>app.view.MazeView</code> component, extending
     * <code>java.awt.JFrame</code>.
     *
     * @author JoshGoA
     * @version 0.1
     * @see javax.swing.JFrame JFrame
     */
    private final MazeModel mazeModel;

    /**
     * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
     * pivotal <code>app.model.MazeModel</code> component, extending
     * <code>javax.swing.JPanel</code> and storing
     * <code>app.model.components.CellPanel</code>.
     *
     * @author JoshGoA
     * @version 0.1
     * @see javax.swing.JPanel JPanel
     * @see app.model.components.CellPanel CellPanel
     */
    private final MazeView mazeView;

    /**
     * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
     * pivotal <code>app.controller.MazeController</code> component, handling
     * multiple pivotal interactions, implementing
     * <code>java.io.Serializable</code>.
     *
     * @author JoshGoA
     * @version 0.1
     * @see java.io.Serializable Serializable
     */
    private final MazeController mazeController;

    /**
     * Create a new maze architecture initializer.
     */
    public Maze() {
        this.mazeModel = new MazeModel();
        this.mazeView = new MazeView();
        this.mazeController = new MazeController();
    }

    /**
     * Assemble pivotal relationships.
     */
    private final void initMaze() {
        this.mazeModel.setController(this.mazeController);
        this.mazeView.setController(this.mazeController);
        this.mazeController.setModel(this.mazeModel);
        this.mazeController.setView(this.mazeView);
    }

    /**
     * Pivot initializer wrapper.
     */
    private final void runMaze() {
        this.mazeController.run();
    }

    /**
     * <code>java.lang.Thread</code> invokation initializer.
     *
     * @see java.lang.Thread Thread
     * @see javax.swing.SwingUtilities#invokeLater(Runnable doRun) invokeLater()
     */
    @Override
    public void run() {
        this.initMaze();
        this.runMaze();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.mazeController == null) ? 0 : this.mazeController.hashCode());
        result = prime * result + ((this.mazeModel == null) ? 0 : this.mazeModel.hashCode());
        result = prime * result + ((this.mazeView == null) ? 0 : this.mazeView.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final Maze other = (Maze) obj;
        if (this.mazeController == null)
            if (other.mazeController != null)
                return false;
        else if (!this.mazeController.equals(other.mazeController))
            return false;
        if (this.mazeModel == null)
            if (other.mazeModel != null)
                return false;
        else if (!this.mazeModel.equals(other.mazeModel))
            return false;
        if (this.mazeView == null)
            if (other.mazeView != null)
                return false;
        else if (!this.mazeView.equals(other.mazeView))
            return false;
        return true;
    }

    @Override
    public final String toString() {
        return this.mazeController.toString();
    }

}
