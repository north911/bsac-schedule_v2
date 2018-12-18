package com.artoymdmitriev.bsacschedule.logic;

import com.artoymdmitriev.bsacschedule.parser.ScheduleInfo;

import java.io.Serializable;

/**
 * Created by adanilenka.
 */

public class CustomScheduleInfo extends ScheduleInfo implements Serializable {
    String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
