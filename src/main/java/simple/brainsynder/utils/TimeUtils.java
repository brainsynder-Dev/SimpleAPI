package simple.brainsynder.utils;

import simple.brainsynder.math.MathUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    public TimeUtils() {
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        return sdf.format(cal.getTime());
    }

    public static String when(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        return sdf.format(time);
    }

    public static String date() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(cal.getTime());
    }

    public static String since(long epoch) {
        return "Took " + convertString(System.currentTimeMillis() - epoch, 1, TimeUtils.TimeUnit.FIT) + '.';
    }

    public static double convert(long time, int trim, TimeUtils.TimeUnit type) {
        if (type == TimeUtils.TimeUnit.FIT) {
            if (time < 60000L) {
                type = TimeUtils.TimeUnit.SECONDS;
            } else if (time < 3600000L) {
                type = TimeUtils.TimeUnit.MINUTES;
            } else if (time < 86400000L) {
                type = TimeUtils.TimeUnit.HOURS;
            } else {
                type = TimeUnit.DAYS;
            }
        }

        return type == TimeUnit.DAYS ? MathUtils.trim(trim, (double) time / 8.64E7D) : (type == TimeUtils.TimeUnit.HOURS ? MathUtils.trim(trim, (double) time / 3600000.0D) : (type == TimeUtils.TimeUnit.MINUTES ? MathUtils.trim(trim, (double) time / 60000.0D) : (type == TimeUtils.TimeUnit.SECONDS ? MathUtils.trim(trim, (double) time / 1000.0D) : MathUtils.trim(trim, (double) time))));
    }

    public static String MakeStr(long time) {
        return convertString(time, 1, TimeUnit.FIT);
    }

    public static String MakeStr(long time, int trim) {
        return convertString(time, trim, TimeUnit.FIT);
    }

    public static String convertString(long time, int trim, TimeUnit type) {
        if (time == -1L) {
            return "Permanent";
        } else {
            if (type == TimeUnit.FIT) {
                if (time < 60000L) {
                    type = TimeUnit.SECONDS;
                } else if (time < 3600000L) {
                    type = TimeUnit.MINUTES;
                } else if (time < 86400000L) {
                    type = TimeUnit.HOURS;
                } else {
                    type = TimeUnit.DAYS;
                }
            }

            return type == TimeUnit.DAYS ? MathUtils.trim(trim, (double) time / 8.64E7D) + " Days" : (type == TimeUtils.TimeUnit.HOURS ? MathUtils.trim(trim, (double) time / 3600000.0D) + " Hours" : (type == TimeUtils.TimeUnit.MINUTES ? MathUtils.trim(trim, (double) time / 60000.0D) + " Minutes" : (type == TimeUtils.TimeUnit.SECONDS ? MathUtils.trim(trim, (double) time / 1000.0D) + " Seconds" : MathUtils.trim(trim, (double) time) + " Milliseconds")));
        }
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public enum TimeUnit {
        FIT,
        DAYS,
        HOURS,
        MINUTES,
        SECONDS,
        MILLISECONDS;

        TimeUnit() {
        }
    }
}
