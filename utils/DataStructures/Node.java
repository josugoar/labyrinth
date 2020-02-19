package utils.DataStructures;

public final class Node {

    public Node parent;
    public int val;
    public int[] seed;

    public Node(Node parent, int val, int[] seed) {
        this.parent = parent;
        this.val = val;
        this.seed = seed;
    }

    public String toString() {
        return "Node(val: " + val + ", seed: " + seed + ")";
    }

}
