package app.maze;

import java.io.Serializable;

import app.maze.controller.MazeController;
import app.maze.model.MazeModel;
import app.maze.view.MazeView;

public final class Maze extends Thread implements Serializable {

    private static final long serialVersionUID = 1L;

    private final MazeModel mzModel;

    private final MazeView mzView;

    private final MazeController mzController;

    {
        mzModel = new MazeModel();
        mzView = new MazeView();
        mzController = new MazeController();
    }

    private final void initMaze() {
        mzModel.setController(mzController);
        mzView.setController(mzController);
        mzController.setModel(mzModel);
        mzController.setView(mzView);
    }

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
