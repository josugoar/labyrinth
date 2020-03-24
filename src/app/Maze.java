package app;

import app.controller.MazeController;
import app.model.MazeModel;
import app.view.MazeView;

/**
 * Maze architecture initializer.
 */
public final class Maze implements Runnable {

    // TODO: add toString, hascode, equals

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

}
