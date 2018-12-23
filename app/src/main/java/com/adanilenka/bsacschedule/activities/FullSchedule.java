package com.adanilenka.bsacschedule.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
                        showSchedule(Calendar.getInstance().get(Calendar.DAY_OF_WEEK), DateCalc.getCurrentWeek());
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

        setListenerToViewPager();

        String groupName = arrayList.get(0).getName();

        getSupportActionBar().setTitle(groupName);
        showSchedule(Calendar.getInstance().get(Calendar.DAY_OF_WEEK), DateCalc.getCurrentWeek());
        dayViewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        //end of loading the schedule
    }

    private void setListenerToViewPager() {
        dayViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                showSchedule2(position);
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    private void startDownloadActivity() {
        Intent intent = new Intent(this, SelectGroupActivity.class);
        startActivity(intent);
    }

    private void showSchedule(int currentDay, int week) {
        ScheduleItemAdapter itemsAdapter;

        List<Pair> pairs = getDayPairsFromDB(currentDay, week);
        ArrayList<Pair> pairsSchedule = new ArrayList<>();

        setBackgroundImageIfEmpty(pairs);

        for (Pair pair : pairs) {
            if (!StringUtil.isBlank(pair.getName()))
                pairsSchedule.add(pair);
        }

        listView = (ListView) findViewById(R.id.listview);
        itemsAdapter = new ScheduleItemAdapter(this, R.layout.list_item, pairsSchedule);
        listView.setAdapter(itemsAdapter);
    }

    private void setBackgroundImageIfEmpty(List<Pair> pairs) {
        View listView = findViewById(R.id.fragment_schedule);
        if (pairs.isEmpty()) {
            listView.setBackground(getResources().getDrawable(R.drawable.no_pairs));
        } else {
            listView.setBackgroundColor(Color.GREEN);
        }
    }

    private void showSchedule2(int currentDay) {
        ScheduleItemAdapter itemsAdapter = (ScheduleItemAdapter) listView.getAdapter();
        itemsAdapter.clear();
        int week = DateCalc.getCurrentWeek();
        List<Pair> pairs = getDayPairsFromDB(currentDay, week);
        setBackgroundImageIfEmpty(pairs);
        for (Pair pair : pairs) {
            if (!StringUtil.isBlank(pair.getName()))
                itemsAdapter.add(pair);
        }
    }

    private List<Pair> getDayPairsFromDB(int currentDay, int week) {
        DBHelperSchedule dbHelperSchedule = new DBHelperSchedule(this);
        return dbHelperSchedule.getPairsByDayAndWeek(Integer.toString(week), DateCalc.getDayNameByNumber(currentDay));
    }
}
