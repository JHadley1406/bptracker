package com.health.hhi.bptracker.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Josiah Hadley on 12/11/2015.
 */
public class DbContract{

    public static final String CONTENT_AUTHORITY = "com.health.hhi.bptracker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_READINGS = "readings";

    public static final String[] READING_COLUMNS = {
            ReadingEntry.TABLE_NAME + "." + ReadingEntry._ID,
            ReadingEntry.COLUMN_READINGS_SYS,
            ReadingEntry.COLUMN_READINGS_DIA,
            ReadingEntry.COLUMN_READINGS_PULSE,
            ReadingEntry.COLUMN_READINGS_DATE
    };

    public static final class ReadingEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_READINGS).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_READINGS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_READINGS;

        public static final String TABLE_NAME = "readings";
        public static final String COLUMN_READINGS_SYS = "systolic";
        public static final String COLUMN_READINGS_DIA = "diastolic";
        public static final String COLUMN_READINGS_PULSE = "pulse";
        public static final String COLUMN_READINGS_DATE = "date";

        public static Uri buildReadingUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReadingWithDate(long date){
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_READINGS_DATE, Long.toString(date)).build();
        }
    }
}
