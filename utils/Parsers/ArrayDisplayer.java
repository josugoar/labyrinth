package utils.Parsers;

import java.util.Arrays;

import utils.DataStructures.Element;
import utils.DataStructures.Elements.Empty;
import utils.DataStructures.Elements.Node;
import utils.DataStructures.Elements.Obstacle;

public class ArrayDisplayer {

    public static void plot(Element[][] arr) {
        for (Element[] row : arr) {
            for (Element cell : row) {
                if (cell instanceof Empty) {
                    System.out.printf("-");
                } else if (cell instanceof Obstacle) {
                    System.out.printf("#");
                } else if (cell instanceof Node) {
                    System.out.printf("*");
                } else {
                    System.out.printf("0");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
