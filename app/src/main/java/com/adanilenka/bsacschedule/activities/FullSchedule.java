package com.adanilenka.bsacschedule.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.adanilenka.bsacschedule.Entity.Group;
import com.adanilenka.bsacschedule.Entity.Pair;
import com.adanilenka.bsacschedule.R;
import com.adanilenka.bsacschedule.adapters.ScheduleItemAdapter;
import com.adanilenka.bsacschedule.adapters.SectionPagerAdapter;
import com.adanilenka.bsacschedule.logic.DBHelper;
import com.adanilenka.bsacschedule.logic.DBHelperSchedule;
import com.adanilenka.bsacschedule.logic.DateCalc;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FullSchedule extends AppCompatActivity {
    private ListView listView;
    private ViewPager dayViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_schedule);
        dayViewPager = (ViewPager) findViewById(R.id.pager);
        dayViewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bseu_profile_pic)
                .build();
        //setting up action drawer
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        Drawer result = drawerBuilder
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentNavigationBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .build();

        DBHelper dbHelper = new DBHelper(this);
        ArrayList<Group> arrayList = (ArrayList<Group>) dbHelper.getAllScheduleInfo();
        for (Group group : arrayList) {
            result.addItem(new PrimaryDrawerItem().withIcon(R.drawable.calendar).withName(group.getName())
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        showSchedule(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                        toolbar.setTitle(group.getName());
                        result.closeDrawer();
                        return true;
                    }));
        }
        result.addItem(new PrimaryDrawerItem().withName("Загрузить расписание").withIcon(R.drawable.download)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    startDownloadActivity();
                    return true;
                }));
        //end of setting up action drawer

        String groupName = arrayList.get(0).getName();

        getSupportActionBar().setTitle(groupName);
        showSchedule(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        dayViewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        //end of loading the schedule
    }

    private void startDownloadActivity() {
        Intent intent = new Intent(this, SelectGroupActivity.class);
        startActivity(intent);
    }

    private void showSchedule(int currentDay) {
        int week = DateCalc.getCurrentWeek();

        DBHelperSchedule dbHelperSchedule = new DBHelperSchedule(this);
        List<Pair> pairs =  dbHelperSchedule.getPairsByDayAndWeek(Integer.toString(week), DateCalc.getDayNameByNumber(currentDay));
        ArrayList<Pair> pairsSchedule = new ArrayList<>();

        if (pairs.isEmpty()) {
            View listView = findViewById(R.id.fragment_schedule);
            listView.setBackground(getResources().getDrawable(R.drawable.no_pairs));
        }

        for (Pair pair : pairs) {
            if (!StringUtil.isBlank(pair.getName()))
                pairsSchedule.add(pair);
        }

        listView = (ListView) findViewById(R.id.listview);
        ScheduleItemAdapter itemsAdapter =
                new ScheduleItemAdapter(this, R.layout.list_item, pairsSchedule);
        listView.setAdapter(itemsAdapter);
    }
}
