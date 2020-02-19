package utils.DataStructures;

public final class Node {

    public Node parent;
    public int val;
    public int[] seed;

    public Node(int val, int[] seed) {
        this.parent = null;
        this.val = val;
        this.seed = seed;
    }

    public String toString() {
        return "Node(val: " + val + ", seed: " + seed + ")";
    }

}
