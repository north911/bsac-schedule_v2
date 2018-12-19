package com.adanilenka.bsacschedule.logic;

import java.util.Date;

/**
 * Created by adanilenka
 */

public class DateCalc {
    private static final int SEPTEMBER_MONTH = 9;
    private static final int DATE_FIRST = 1;

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
}
