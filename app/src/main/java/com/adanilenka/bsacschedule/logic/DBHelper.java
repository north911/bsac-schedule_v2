package com.adanilenka.bsacschedule.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adanilenka.bsacschedule.Entity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adanilenka
 */

public class DBHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    private static final String GROUP_INFO = "GroupInfo";

    // Database Name
    private static final String DATABASE_NAME = "schedule_info";
    private static final String GROUP_NAME = "group_name";
    private static final String PAIRS = "pairs";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String TIME = "time";
    private static final String CABINET = "cabinet";
    private static final String DAY = "day";
    private static final String SUBGROUP = "subgroup";
    private static final String PROFESSOR = "professor";
    private static final String WEEK = "week";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                GROUP_INFO, KEY_ID, GROUP_NAME, SUBGROUP);
        String sqlQuery2 = String.format("CREATE TABLE IF NOT EXISTS %s ( %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
                PAIRS, KEY_NAME, TIME, CABINET, DAY, PROFESSOR, WEEK);
        db.execSQL(sqlQuery);
        db.execSQL(sqlQuery2);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addScheduleInfo(CustomScheduleInfo customScheduleInfo) {

        SQLiteDatabase db = this.getReadableDatabase();
        //now we support only 1 schedule
        resetDB(db);

        ContentValues values = new ContentValues();
        values.put(GROUP_NAME, customScheduleInfo.getGroupName());
        values.put(SUBGROUP, customScheduleInfo.getSubGroup());

        // Inserting Row
        db.insert(GROUP_INFO, null, values);
        db.close(); // Closing database connection
    }

    private void resetDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + PAIRS);
        onCreate(db);
    }

    // Getting All Contacts
    public List<Group> getAllScheduleInfo() {
        List<Group> groups = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + GROUP_INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Group group = new Group();
                group.setName(cursor.getString(1));
                group.setSubGroup(Integer.parseInt(cursor.getString(2)));
                groups.add(group);
            } while (cursor.moveToNext());
        }

        // return contact list
        return groups;
    }


    // Getting contacts Count
    public int getCustomScheduleInfoCount() {
        int index;
        try {
            String countQuery = "SELECT * FROM " + GROUP_INFO;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            index = cursor.getCount();
            cursor.close();
        } catch (SQLException e) {
            index = 0;
        }
        // return count
        return index;
    }
}
