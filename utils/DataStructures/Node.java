package utils.DataStructures;

public final class Node {

    private Node parent;
    private int val;
    private int[] seed;

    public Node(Node parent, int val, int[] seed) {
        this.parent = parent;
        this.val = val;
        this.seed = seed;
    }

    public String toString() {
        return "Node(val: " + val + ", seed: " + seed + ")";
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
