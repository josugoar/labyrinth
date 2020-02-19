package utils.Parsers;

import java.util.ArrayList;
import java.util.Arrays;

public class StringSplitter {

    public static ArrayList<int[]> parse(String[] args, String l, String r, String m) {
        ArrayList<int[]> points = new ArrayList<int[]>();
        for (String arg : args) {
            points.add(Arrays.stream(arg.substring(l.length(), arg.length() - r.length()).split(m))
                    .mapToInt(Integer::parseInt).toArray());
        }
        return points;
    }

}
