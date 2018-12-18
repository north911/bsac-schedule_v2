package com.artoymdmitriev.bsacschedule.InterfaceAPI;


import com.artoymdmitriev.bsacschedule.Entity.WeekSchedule;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScheduleApi {
    @GET("getSchedule")
    Call<ArrayList<WeekSchedule>> getData(@Query("groupName") String group,
                                          @Query("subGroup") int subGroup);

}
