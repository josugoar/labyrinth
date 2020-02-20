package utils.Parsers;

import java.util.ArrayList;
import java.util.Arrays;

public class StringSplitter {

    /**
     * Parse string array conatining integer point arrays
     *
     * @param args String[]
     * @param l    String
     * @param r    String
     * @param m    String
     * @return parsed points
     */
    public static ArrayList<int[]> parse(final String[] args, final String l, final String r, final String m) {
        final ArrayList<int[]> points = new ArrayList<int[]>();
        for (final String arg : args) {
            final int[] point = Arrays.stream(arg.substring(l.length(), arg.length() - r.length()).split(m))
                    .mapToInt(Integer::parseInt).toArray();
            points.add(point);
        }
        return points;
    }

}
