package com.tmdbapp.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
    public static String formatDate(String date) {
        if (date == null) return "";

        if (date.contains("-")) {
            try {
                return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US).format(Calendar.getInstance().getTime());
    }
}
