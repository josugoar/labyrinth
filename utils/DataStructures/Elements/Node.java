package utils.DataStructures.Elements;

import utils.DataStructures.Element;

public final class Node extends Element {

    private final Node parent;
    private final Element val;
    private final int[] seed;
    private boolean path = false;

    /**
     * Individual node which stores pointer to parent node
     *
     * @param parent Node
     * @param val    Element
     * @param seed   int[]
     */
    public Node(final Node parent, final Element val, final int[] seed) {
        this.parent = parent;
        this.val = val;
        this.seed = seed;
    }

    @Override
    public String toString() {
        if (parent == null) {
            return String.format("Node(parent: null, val: %s, seed: [%d, %d], path: %b)", this.val, this.seed[0],
                    this.seed[1], this.path);
        } else {
            return String.format("Node(parent: [%d, %d], val: %s, seed: [%d, %d], path: %b)", this.parent.seed[0],
                    this.parent.seed[1], this.val, this.seed[0], this.seed[1], this.path);

        }

    }

    public Node get_parent() {
        return this.parent;
    }

    public Element get_val() {
        return this.val;
    }

    public int[] get_seed() {
        return this.seed;
    }

    public boolean get_path() {
        return this.path;
    }

    public void set_path(final boolean path) {
        this.path = path;
    }

}
