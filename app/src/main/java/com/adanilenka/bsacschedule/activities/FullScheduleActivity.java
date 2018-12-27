package com.adanilenka.bsacschedule.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.adanilenka.bsacschedule.Entity.Group;
import com.adanilenka.bsacschedule.Entity.Pair;
import com.adanilenka.bsacschedule.R;
import com.adanilenka.bsacschedule.adapters.ScheduleItemAdapter;
import com.adanilenka.bsacschedule.adapters.SectionPagerAdapter;
import com.adanilenka.bsacschedule.logic.DBHelper;
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

public class FullScheduleActivity extends AppCompatActivity {
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
                .withHeaderBackground(R.drawable.bgas_profile_pic)
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
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                if (spinner != null) {
                    showSchedule2(position, spinner.getSelectedItemPosition() + 1);
                } else {
                    showSchedule2(position, DateCalc.getCurrentWeek());
                }
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
            listView.setBackgroundColor(Color.parseColor("#18ad62"));
        }
    }

    private void showSchedule2(int currentDay, int week) {
        ScheduleItemAdapter itemsAdapter = (ScheduleItemAdapter) listView.getAdapter();
        itemsAdapter.clear();
        List<Pair> pairs = getDayPairsFromDB(currentDay, week);
        setBackgroundImageIfEmpty(pairs);
        for (Pair pair : pairs) {
            if (!StringUtil.isBlank(pair.getName()))
                itemsAdapter.add(pair);
        }
    }

    private List<Pair> getDayPairsFromDB(int currentDay, int week) {
        DBHelper dbHelperSchedule = new DBHelper(this);
        return dbHelperSchedule.getPairsByDayAndWeek(Integer.toString(week), DateCalc.getDayNameByNumber(currentDay));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_week_schedule, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();
        setUpWeekSpinner(spinner);


        return true;
    }

    private void setUpWeekSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weeks_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(DateCalc.getCurrentWeek());

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                showSchedule2(dayViewPager.getCurrentItem(), spinner.getSelectedItemPosition() + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {
            showInfoDialog("Info", "Developed by Andrei Danilenka.\nSource code is on github.com/north911\n" +
                    "Questions, bug reports, cooperation offers to vk.com/north911");
            return true;
        }
        if (id == R.id.action_settings2) {
            showInfoDialog("About app", "This application was developed to help \n" +
                    "students of Belarussian State Academy of Communications.\n" +
                    "App parse schedule from bsac.by:8080/timetable");
            return true;
        }

        if (id == R.id.action_user){
            dayViewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            spinner.setSelection(DateCalc.getCurrentWeek() - 1);
            showSchedule2(Calendar.getInstance().get(Calendar.DAY_OF_WEEK), DateCalc.getCurrentWeek());
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(FullScheduleActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
