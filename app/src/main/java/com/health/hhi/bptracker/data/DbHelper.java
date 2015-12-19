package com.health.hhi.bptracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.RequiresPermission;

/**
 * Created by Josiah Hadley on 12/11/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "bpreadings.db";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_READINGS_TABLE = "CREATE TABLE " + DbContract.ReadingEntry.TABLE_NAME + " ("+
                DbContract.ReadingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DbContract.ReadingEntry.COLUMN_READINGS_DATE + " INTEGER NOT NULL, " +
                DbContract.ReadingEntry.COLUMN_READINGS_DIA + " INTEGER NOT NULL, " +
                DbContract.ReadingEntry.COLUMN_READINGS_PULSE + " INTEGER NOT NULL, " +
                DbContract.ReadingEntry.COLUMN_READINGS_SYS + " INTEGER NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_READINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        // re-populate existing data on upgrade here

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.ReadingEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
