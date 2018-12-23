package com.adanilenka.bsacschedule.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.adanilenka.bsacschedule.activities.fragments.ScheduleFragment;

import java.util.Calendar;
import java.util.Locale;

public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return new ScheduleFragment();
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        Calendar c = Calendar.getInstance(l);
        if (position >= 0 && position < 7) {
            int day = ((position + c.getFirstDayOfWeek()) % 7);
            c.set(Calendar.DAY_OF_WEEK, day);
            return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, l);
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
