package utils.Algorithms;

import utils.DataStructures.Element;
import utils.DataStructures.Elements.*;

public class Backtracker {

    public static Element[][] traverse(Element[][] grid, Node last_child) {
        if (last_child.get_parent() != null) {
            last_child.set_path(true);
            return traverse(grid, last_child.get_parent());
        } else {
            return grid;
        }
    }

}
