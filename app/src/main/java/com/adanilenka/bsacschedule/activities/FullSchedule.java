package com.adanilenka.bsacschedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.adanilenka.bsacschedule.Entity.Group;
import com.adanilenka.bsacschedule.Entity.Pair;
import com.adanilenka.bsacschedule.R;
import com.adanilenka.bsacschedule.adapters.ScheduleItemAdapter;
import com.adanilenka.bsacschedule.logic.DBHelper;
import com.adanilenka.bsacschedule.logic.DBHelperSchedule;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import java.util.ArrayList;
import java.util.Calendar;

public class FullSchedule extends AppCompatActivity {
    private ListView listView;
    int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_schedule);

        /*Intent intent = new Intent(this, WeekScheduleActivity.class);
        startActivity(intent);*/
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
                        showSchedule("" + group.getName());
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

        //loading the first schedule so that the activity won't be empty on the first start
        String groupName = arrayList.get(0).getName();
        int groupID = arrayList.get(0).getId();

        getSupportActionBar().setTitle(groupName);
        showSchedule("" + groupID);
        //end of loading the schedule
    }

    private void startDownloadActivity() {
        Intent intent = new Intent(this, SelectGroupActivity.class);
        startActivity(intent);
    }

    private void showSchedule(String groupNumber) {
        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        DBHelperSchedule dbHelperSchedule = new DBHelperSchedule(this);
        ArrayList<Pair> pairs = (ArrayList) dbHelperSchedule.getAllPairs();
        ArrayList<Pair> pairsSchedule = new ArrayList<>();

        for (Pair pair : pairs) {
            pairsSchedule.add(pair);
        }

        listView = (ListView) findViewById(R.id.listview);
        ScheduleItemAdapter itemsAdapter =
                new ScheduleItemAdapter(this, R.layout.list_item, pairsSchedule);
        listView.setAdapter(itemsAdapter);
    }

    private void getWeeksBetween() {

    }
}
