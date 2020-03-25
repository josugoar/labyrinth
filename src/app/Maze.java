package app;

import app.controller.MazeDelegator;
import app.model.MazePanel;
import app.view.MazeFrame;

/**
 * Maze architecture initializer.
 */
public final class Maze implements Runnable {

    /**
     * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
     * pivotal <code>app.view.MazeFrame</code> component, extending
     * <code>java.awt.JFrame</code>.
     *
     * @author JoshGoA
     * @version 0.1
     * @see javax.swing.JFrame JFrame
     */
    private final MazePanel mazePanel;

    /**
     * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
     * pivotal <code>app.model.MazePanel</code> component, extending
     * <code>javax.swing.JPanel</code> and storing
     * <code>app.model.components.CellPanel</code>.
     *
     * @author JoshGoA
     * @version 0.1
     * @see javax.swing.JPanel JPanel
     * @see app.model.components.CellPanel CellPanel
     */
    private final MazeFrame mazeFrame;

    /**
     * Graphical-User-Inteface (GUI) Model-View-Controller (MVC) architecture
     * pivotal <code>app.controller.MazeDelegator</code> component, handling
     * multiple pivotal interactions, implementing
     * <code>java.io.Serializable</code>.
     *
     * @author JoshGoA
     * @version 0.1
     * @see java.io.Serializable Serializable
     */
    private final MazeDelegator mazeDelegator;

    /**
     * Create a new maze architecture initializer.
     */
    public Maze() {
        this.mazePanel = new MazePanel();
        this.mazeFrame = new MazeFrame();
        this.mazeDelegator = new MazeDelegator();
    }

    /**
     * Assemble pivotal relationships.
     */
    private final void initMaze() {
        this.mazePanel.setDelegator(this.mazeDelegator);
        this.mazeFrame.setDelegator(this.mazeDelegator);
        this.mazeDelegator.setPanel(this.mazePanel);
        this.mazeDelegator.setFrame(this.mazeFrame);
    }

    /**
     * Pivot initializer wrapper.
     */
    private final void runMaze() {
        this.mazeFrame.display();
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
        result = prime * result + ((this.mazeDelegator == null) ? 0 : this.mazeDelegator.hashCode());
        result = prime * result + ((this.mazePanel == null) ? 0 : this.mazePanel.hashCode());
        result = prime * result + ((this.mazeFrame == null) ? 0 : this.mazeFrame.hashCode());
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
        if (this.mazeDelegator == null)
            if (other.mazeDelegator != null)
                return false;
        else if (!this.mazeDelegator.equals(other.mazeDelegator))
            return false;
        if (this.mazePanel == null)
            if (other.mazePanel != null)
                return false;
        else if (!this.mazePanel.equals(other.mazePanel))
            return false;
        if (this.mazeFrame == null)
            if (other.mazeFrame != null)
                return false;
        else if (!this.mazeFrame.equals(other.mazeFrame))
            return false;
        return true;
    }

    @Override
    public final String toString() {
        return this.mazeDelegator.toString();
    }

}
