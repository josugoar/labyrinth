package utils.DataStructures;

public final class Node {

    private final Node parent;
    private final int val;
    private final int[] seed;

    /**
     * Individual node which stores pointer to parent node
     *
     * @param parent Node
     * @param val    int
     * @param seed   int[]
     */
    public Node(final Node parent, final int val, final int[] seed) {
        this.parent = parent;
        this.val = val;
        this.seed = seed;
    }

    public String toString() {
        return String.format("Node(val: %d, seed: [%d, %d])", this.val, this.seed[0], this.seed[1]);
    }

    public Node get_parent() {
        return this.parent;
    }

    public int get_val() {
        return this.val;
    }

    public int[] get_seed() {
        return this.seed;
    }

}
