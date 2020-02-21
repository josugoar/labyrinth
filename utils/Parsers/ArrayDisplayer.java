package utils.Parsers;

import utils.DataStructures.Element;
import utils.DataStructures.Elements.*;

public class ArrayDisplayer {

    /**
     * Show grid in command line
     *
     * @param arr Element[][]
     */
    public static void plot(final Element[][] arr) {
        // clearScreen();
        for (final Element[] row : arr) {
            for (Element cell : row) {
                if (cell instanceof Empty) {
                    System.out.printf("-");
                } else if (cell instanceof Node) {
                    if (((Node) cell).get_path()) {
                        System.out.printf("^");
                    } else {
                        System.out.printf("*");
                    }
                } else if (cell instanceof Obstacle) {
                    System.out.printf("#");
                } else {
                    System.out.printf("0");
                }
                System.out.printf(" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    /**
     * Clear screen
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
