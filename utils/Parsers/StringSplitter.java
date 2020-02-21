package utils.Parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringSplitter {

    // String filters
    private static String l = "(";
    private static String m = ", ";
    private static String r = ")";
    private static String s = " ";

    /**
     * Parse string array containing integer points
     *
     * @param args String[]
     * @return parsed points
     */
    public static List<List<int[]>> parse(final String[] args) {
        final ArrayList<List<int[]>> point_wrapper = new ArrayList<List<int[]>>(3);
        // Range through points
        for (final String arg : args) {
            int i = 0;
            final ArrayList<int[]> points = new ArrayList<int[]>();
            while (i < arg.length()) {
                // Get individual point
                final String point = arg.substring(arg.indexOf(l, i), arg.indexOf(r, i) + 1);
                // Convert to integer
                points.add(Arrays.stream(point.substring(l.length(), point.length() - r.length()).split(m))
                        .mapToInt(Integer::parseInt).toArray());
                // Advance to next point
                i += arg.indexOf(r, i) + 1 - arg.indexOf(l, i) + s.length();
            }
            point_wrapper.add(points);
        }
        return point_wrapper;
    }

    public static String get_l() {
        return l;
    }

    public static void set_l(final String new_l) {
        l = new_l;
    }

    public static String get_m() {
        return m;
    }

    public static void set_m(final String new_m) {
        m = new_m;
    }

    public static String get_r() {
        return r;
    }

    public static void set_r(final String new_r) {
        r = new_r;
    }

    public static String get_s() {
        return s;
    }

    public static void set_s(final String new_s) {
        s = new_s;
    }

}
