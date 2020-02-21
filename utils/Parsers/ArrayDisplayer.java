package utils.Parsers;

import java.util.Arrays;

public class ArrayDisplayer {

    public static void plot(int[][] arr) {
        for (int[] row : arr) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("");
    }
}
