package app;

import app.controller.MazeController;
import app.model.MazeModel;
import app.view.MazeView;

/**
 * Maze architecture initializer.
 */
public final class Maze implements Runnable {

    private MazeModel mazeModel;
    private MazeView mazeView;
    private MazeController mazeController;

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

}
