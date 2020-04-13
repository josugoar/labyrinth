package app.maze;

import app.maze.controller.MazeController;
import app.maze.model.MazeModel;
import app.maze.view.MazeView;

public final class Maze extends Thread {

    private final MazeModel MazeModel;

    private final MazeView MazeView;

    private final MazeController MazeController;

    public Maze() {
        this.MazeModel = new MazeModel();
        this.MazeView = new MazeView();
        this.MazeController = new MazeController();
    }

    private final void initMaze() {
        this.MazeModel.setController(this.MazeController);
        this.MazeView.setController(this.MazeController);
        this.MazeController.setModel(this.MazeModel);
        this.MazeController.setView(this.MazeView);
    }

    private final void runMaze() {
        this.MazeView.display();
    }

    @Override
    public void run() {
        this.initMaze();
        this.runMaze();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.MazeController == null) ? 0 : this.MazeController.hashCode());
        result = prime * result + ((this.MazeModel == null) ? 0 : this.MazeModel.hashCode());
        result = prime * result + ((this.MazeView == null) ? 0 : this.MazeView.hashCode());
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
        if (this.MazeController == null)
            if (other.MazeController != null)
                return false;
            else if (!this.MazeController.equals(other.MazeController))
                return false;
        if (this.MazeModel == null)
            if (other.MazeModel != null)
                return false;
            else if (!this.MazeModel.equals(other.MazeModel))
                return false;
        if (this.MazeView == null)
            if (other.MazeView != null)
                return false;
            else if (!this.MazeView.equals(other.MazeView))
                return false;
        return true;
    }

    @Override
    public final String toString() {
        return this.MazeController.toString();
    }

}
