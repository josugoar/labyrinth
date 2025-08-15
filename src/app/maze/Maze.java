package app.maze;

import java.io.Serializable;

import app.maze.controller.MazeController;
import app.maze.model.MazeModel;
import app.maze.view.MazeView;

/**
 * Maze structure representation, extending <code>java.lang.Thread</code> and
 * implementing <code>java.io.Serializable</code>.
 *
 * @author josugoar
 * @see java.lang.Thread Thread
 * @see java.io.Serializable Serializable
 */
public final class Maze extends Thread implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final void main(final String[] args) {
        new Maze().start();
    }

    /**
     * Current <code>app.maze.model.MazeModel</code>.
     *
     * @see app.maze.model.MazeModel MazeModel
     */
    private final MazeModel mzModel;

    /**
     * Current <code>app.maze.view.MazeView</code>.
     *
     * @see app.maze.view.MazeView MazeView
     */
    private final MazeView mzView;

    /**
     * Current <code>app.maze.controller.MazeController</code>.
     *
     * @see app.maze.controller.MazeController MazeController
     */
    private final MazeController mzController;

    {
        mzModel = new MazeModel();
        mzView = new MazeView();
        mzController = new MazeController();
    }

    /**
     * Initialize maze.
     */
    private final void initMaze() {
        mzModel.setController(mzController);
        mzView.setController(mzController);
        mzController.setModel(mzModel);
        mzController.setView(mzView);
    }

    /**
     * Run maze.
     */
    private final void runMaze() {
        mzView.display();
    }

    @Override
    public void run() {
        initMaze();
        runMaze();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mzController == null) ? 0 : mzController.hashCode());
        result = prime * result + ((mzModel == null) ? 0 : mzModel.hashCode());
        result = prime * result + ((mzView == null) ? 0 : mzView.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Maze other = (Maze) obj;
        if (mzController == null)
            if (other.mzController != null)
                return false;
            else if (!mzController.equals(other.mzController))
                return false;
        if (mzModel == null)
            if (other.mzModel != null)
                return false;
            else if (!mzModel.equals(other.mzModel))
                return false;
        if (mzView == null)
            if (other.mzView != null)
                return false;
            else if (!mzView.equals(other.mzView))
                return false;
        return true;
    }

    @Override
    public final String toString() {
        return "Maze";
    }

}
