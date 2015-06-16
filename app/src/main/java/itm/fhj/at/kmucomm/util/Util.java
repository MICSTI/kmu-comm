package itm.fhj.at.kmucomm.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        Date date = new Date(timestamp * 1000);

        Format format = new SimpleDateFormat("dd.MM.yyyy kk:mm");

        //return Config.FORMAT_DATE_TIME.format(date);
        return format.format(date);
    }

    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String getUsernameFromResource(String resource) {
        int atPosition = resource.indexOf(Config.CHAR_AT);

        if (atPosition > 0)
            return resource.substring(0, atPosition);

        return null;
    }

    public static String getCamelCase(String text) {
        if (text.length() > 0)
            return text.toUpperCase().substring(0, 1) + text.substring(1);

        return text;
    }

    public static String getFirstName(String name) {
        int atPosition = name.indexOf(" ");

        if (atPosition > 0)
            return name.substring(0, atPosition);

        return "";
    }
}
