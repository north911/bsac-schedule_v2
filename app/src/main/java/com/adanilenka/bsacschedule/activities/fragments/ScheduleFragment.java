package com.adanilenka.bsacschedule.activities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adanilenka.bsacschedule.R;
import com.adanilenka.bsacschedule.activities.FullScheduleActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private int page;
    private FullScheduleActivity act;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(int pos) {
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.page = pos;
        return fragment;
    }

    public void setActivity(FullScheduleActivity act){
        this.act = act;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

}
