package will.wzhihu.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import will.wzhihu.R;
import will.wzhihu.WApplication;
import will.wzhihu.common.log.Log;

/**
 * @author wendeping
 */
public class DateUtils {
    private static final String TAG = "DateUtils";

    public static String getShowTime(String dateString) {
        boolean today = isToday(dateString);
        if (today) {
            return WApplication.getInstance().getString(R.string.today_news);
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = dateFormat.parse(dateString);
            String week = new SimpleDateFormat("EEE", Locale.CHINA).format(date);

            String month = dateString.substring(4, 6);
            String day = dateString.substring(6);

            return WApplication.getInstance().getString(R.string.date_format, month, day, week);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static boolean isToday(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = dateFormat.parse(dateString);
            return isSameDay(date, Calendar.getInstance().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String subtractDay(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = dateFormat.parse(dateString);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date time = cal.getTime();
            String dateBefore = dateFormat.format(time);
            Log.d(TAG, "%s before date is %s", dateString, dateBefore);
            return dateBefore;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
