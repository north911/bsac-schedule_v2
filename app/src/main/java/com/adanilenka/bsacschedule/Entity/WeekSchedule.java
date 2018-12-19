package com.adanilenka.bsacschedule.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeekSchedule implements Serializable {
    private int numberOfWeek;
    private List<DaySchedule> dayScheduleList;

    public WeekSchedule(int numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
        dayScheduleList = new ArrayList<>(6);
    }

    public int getNumberOfWeek() {
        return numberOfWeek;
    }

    public void setNumberOfWeek(int numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
    }

    public List<DaySchedule> getDayScheduleList() {
        return dayScheduleList;
    }

    public void setDayScheduleList(List<DaySchedule> dayScheduleList) {
        this.dayScheduleList = dayScheduleList;
    }
}
