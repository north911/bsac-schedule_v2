package com.artoymdmitriev.bsacschedule.logic;

import android.os.AsyncTask;

import com.artoymdmitriev.bsacschedule.Entity.WeekSchedule;
import com.artoymdmitriev.bsacschedule.InterfaceAPI.UriBuilder;
import com.artoymdmitriev.bsacschedule.parser.ScheduleInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by adanilenka
 */

public class AsyncParser extends AsyncTask<ScheduleInfo, Void, Object> {
    private ResultsListener resultsListener;
    private ArrayList<WeekSchedule> weekSchedules;
    private int code = 1;

    @Override
    protected Object doInBackground(ScheduleInfo... scheduleInfos) {

        ScheduleInfo scheduleInfo = scheduleInfos[0];

        sendResponse(scheduleInfo.getGroupName(), scheduleInfo.getSubGroup());

        return weekSchedules;
    }

    @Override
    protected void onPostExecute(Object o) {
        resultsListener.onResultSucceeded(o);
    }

    public void setResultsListener(ResultsListener resultsListener) {
        this.resultsListener = resultsListener;
    }

    private ArrayList<WeekSchedule> sendResponse(String groupName, int subGroup) {
        UriBuilder.buildUri();
        UriBuilder.getApi().getData(groupName, subGroup).enqueue(new Callback<ArrayList<WeekSchedule>>() {
            @Override
            public void onResponse(Call<ArrayList<WeekSchedule>> call, Response<ArrayList<WeekSchedule>> response) {
                ArrayList<WeekSchedule> rates = response.body();
                code = response.code();
                weekSchedules = rates;
            }

            @Override
            public void onFailure(Call<ArrayList<WeekSchedule>> call, Throwable t) {
                weekSchedules = null;
            }
        });

        while (code == 1) {
        }
        return weekSchedules;
    }
}
