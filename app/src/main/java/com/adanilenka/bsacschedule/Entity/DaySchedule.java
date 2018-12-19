package com.adanilenka.bsacschedule.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DaySchedule implements Serializable {
    private String dayOfWeek;
    private List<Pair> pairList;

    public DaySchedule() {
        pairList = new ArrayList<>();
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<Pair> getPairList() {
        return pairList;
    }

    public void setPairList(List<Pair> pairList) {
        this.pairList = pairList;
    }
}
