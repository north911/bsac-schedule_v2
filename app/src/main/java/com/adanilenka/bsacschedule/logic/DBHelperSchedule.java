package com.adanilenka.bsacschedule.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adanilenka.bsacschedule.Entity.DaySchedule;
import com.adanilenka.bsacschedule.Entity.Pair;
import com.adanilenka.bsacschedule.Entity.WeekSchedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adanilenka
 */

public class DBHelperSchedule extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "schedule";

    // Contacts Table Columns names
    private static final String KEY_NAME = "name";
    private static final String TIME = "time";
    private static final String CABINET = "cabinet";
    private static final String DAY = "day";
    private static final String PROFESSOR = "professor";
    private static final String PAIRS = "pairs";
    private static final String WEEK = "week";


    public DBHelperSchedule(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS %s ( %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
                PAIRS, KEY_NAME, TIME, CABINET, DAY, PROFESSOR, WEEK);
        db.execSQL(sqlQuery);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<Pair> getAllPairs(){
        List<Pair> pairs = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + PAIRS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pair pair = new Pair();
                pair.setName(cursor.getString(1));
                pair.setTime(cursor.getString(2));
                pair.setCabinet(cursor.getString(3));
                pair.setProfessor(cursor.getString(4));
                // Adding contact to list
                pairs.add(pair);
            } while (cursor.moveToNext());
        }
        // return contact list
        return pairs;
    }

    public List<Pair> getPairsByWeek(String week){
        List<Pair> pairs = new ArrayList<>();

        String selectQuery =String.format("SELECT  * FROM %s WHERE WEEK=%s" , PAIRS , week) ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pair pair = new Pair();
                pair.setName(cursor.getString(0));
                pair.setTime(cursor.getString(1));
                pair.setCabinet(cursor.getString(2));
                pair.setProfessor(cursor.getString(4));
                // Adding contact to list
                pairs.add(pair);
            } while (cursor.moveToNext());
        }
        // return contact list
        return pairs;
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void setScheduleToDb(WeekSchedule weekSchedule) {
        for (DaySchedule daySchedule : weekSchedule.getDayScheduleList()) {
            for (Pair pair : daySchedule.getPairList()) {
                setPairToDB(
                        pair.getName(),
                        pair.getTime(),
                        pair.getCabinet(),
                        daySchedule.getDayOfWeek(),
                        pair.getProfessor(),
                        Integer.toString(weekSchedule.getNumberOfWeek()));
            }
        }
    }


    private void setPairToDB(String name, String time, String cabinet, String day, String professor, String week){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(TIME, time);
        values.put(CABINET, cabinet);
        values.put(DAY, day);
        values.put(PROFESSOR, professor);
        values.put(WEEK, week);
        db.insert(PAIRS, null, values);
        db.close(); // Closing database connection
    }

}
