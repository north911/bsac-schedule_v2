package com.adanilenka.bsacschedule.logic;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by adanilenka
 */

public class DateCalc {
    private static final int SEPTEMBER_MONTH = 9;
    private static final int DATE_FIRST = 1;
    private static final HashMap<String, String> days = setUpDaysHashMap();

    public static int getCurrentWeek() {
        int deltaDays;
        Date curDate = new Date();
        Date firstSeptDate = new Date(curDate.getYear(), SEPTEMBER_MONTH, DATE_FIRST);
        int currentDateDays = curDate.getDay();
        int firstSeptDays = firstSeptDate.getDate();

        if (currentDateDays > firstSeptDays) {
            deltaDays = currentDateDays - firstSeptDays;
        } else {
            deltaDays = firstSeptDays - currentDateDays;
        }
        return calculateCurrentWeek(deltaDays);
    }

    private static int calculateCurrentWeek(int delta) {
        int x = (delta / 7) % 4;
        if (x < 0.25)
            return 1;
        if (x < 0.5)
            return 2;
        if (x < 0.75)
            return 3;
        return 4;
    }

    public static String getDayNameByNumber(int day){
        return days.get(Integer.toString(day));
    }

    private static HashMap<String, String> setUpDaysHashMap() {
        HashMap<String, String> days = new HashMap<>();
        days.put("1", "Понедельник");
        days.put("2", "Вторник");
        days.put("3", "Среда");
        days.put("4", "Четверг");
        days.put("5", "Пятница");
        days.put("6", "Суббота");
        return days;
    }
}
