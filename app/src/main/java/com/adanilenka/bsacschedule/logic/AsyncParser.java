package com.adanilenka.bsacschedule.logic;

import android.os.AsyncTask;
import android.os.Handler;

import com.adanilenka.bsacschedule.Entity.WeekSchedule;
import com.adanilenka.bsacschedule.InterfaceAPI.UriBuilder;
import com.adanilenka.bsacschedule.parser.ScheduleInfo;

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
                weekSchedules = rates;
            }

            @Override
            public void onFailure(Call<ArrayList<WeekSchedule>> call, Throwable t) {
                weekSchedules = null;
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return weekSchedules;
    }
}
