package com.adanilenka.bsacschedule.InterfaceAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UriBuilder {
    private static ScheduleApi scheduleApi;


    public static void buildUri() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://node54149-bgasapi.mycloud.by/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        scheduleApi = retrofit.create(ScheduleApi.class);
    }

    public static ScheduleApi getApi() {
        return scheduleApi;
    }
}
