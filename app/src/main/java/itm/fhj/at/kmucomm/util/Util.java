package itm.fhj.at.kmucomm.util;

/**
 * Created by michael.stifter on 27.05.2015.
 */
public class Util {

    /**
     * Returns the elapsed time in words (e.g. "1 hour ago")
     * @param timestamp
     * @return
     */
    public static String getTextualTime(long timestamp) {
        // TODO: Implement actual formatting

        return "2 hours ago";
    }

    public static String getTime(long timestamp) {
        // TODO: Implement function

        return "14:36";
    }

    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
