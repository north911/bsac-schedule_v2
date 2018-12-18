package com.artoymdmitriev.bsacschedule.parser;

import java.io.Serializable;

/**
 * Created by adanilenka
 */
public class ScheduleInfo implements Serializable {

    private String groupName;
    private int subGroup;
    private int currentWeek;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(int subGroup) {
        this.subGroup = subGroup;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }
}
