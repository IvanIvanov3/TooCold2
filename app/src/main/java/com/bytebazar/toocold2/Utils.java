package com.bytebazar.toocold2;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Locale;

public final class Utils {

    public static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("google.com");
            return !address.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDayOfWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public static int getIconFromDescription(int description) {
        if (description >= 200 && description <= 232) {
            return R.drawable.ic_storm;
        } else if (description >= 300 && description <= 321) {
            return R.drawable.ic_light_rain;
        } else if (description >= 500 && description <= 504) {
            return R.drawable.ic_rain;
        } else if (description == 511) {
            return R.drawable.ic_snow;
        } else if (description >= 520 && description <= 531) {
            return R.drawable.ic_rain;
        } else if (description >= 600 && description <= 622) {
            return R.drawable.ic_snow;
        } else if (description >= 701 && description <= 761) {
            return R.drawable.ic_fog;
        } else if (description == 761 || description == 781) {
            return R.drawable.ic_storm;
        } else if (description == 800) {
            return R.drawable.ic_clear;
        } else if (description == 801) {
            return R.drawable.ic_light_clouds;
        } else if (description >= 802 && description <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    public static String setLanguageResponse() {
        final String defaultLang = Locale.getDefault().getLanguage();
        final String[] languages = {"en", "ru", "it", "es", "uk", "de", "pt", "ro", "ro", "pl", "fi", "nl", "bg", "sv", "tr", "hr", "ca", "zh"};
        for (String language : languages) {
            if (defaultLang.equals(language)) return defaultLang;
        }
        return "en";
    }

}
