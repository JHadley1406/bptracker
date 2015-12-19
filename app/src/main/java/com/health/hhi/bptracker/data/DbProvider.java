package com.health.hhi.bptracker.data;


import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


public class DbProvider extends ContentProvider{


    private static final String LOG_TAG = DbProvider.class.getSimpleName();
    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    private DbHelper mDbHelper;

    static final int READING = 100;
    static final int READING_WITH_DATE = 101;

    private static final SQLiteQueryBuilder READING_QUERY_BUILDER;

    static{
        READING_QUERY_BUILDER = new SQLiteQueryBuilder();
        READING_QUERY_BUILDER.setTables(DbContract.ReadingEntry.TABLE_NAME);
    }

    private Cursor getReadings(Uri uri, String[] projection, String sortOrder){
        return READING_QUERY_BUILDER.query(mDbHelper.getReadableDatabase(), null, null, null, null, null, sortOrder);
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DbContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DbContract.PATH_READINGS, READING);

        return matcher;
    }

    @Override
    public boolean onCreate(){
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri){
        final int match = URI_MATCHER.match(uri);

        switch (match){
            case READING:
                return DbContract.ReadingEntry.CONTENT_TYPE;
            case READING_WITH_DATE:
                //not implemented yet
                return null;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor retCursor;
        switch(URI_MATCHER.match(uri)){
            case READING:{
                retCursor = getReadings(uri, projection, sortOrder);
                break;
            }
            case READING_WITH_DATE:{
                //not implemented yet
                retCursor = null;
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        Uri returnUri;
        Log.i(LOG_TAG, "in insert");
        switch(match){
            case READING:{
                Log.i(LOG_TAG, "systolic value: " + values.getAsString(DbContract.ReadingEntry.COLUMN_READINGS_SYS));
                Log.i(LOG_TAG, "diastolic value: " + values.getAsString(DbContract.ReadingEntry.COLUMN_READINGS_DIA));
                Log.i(LOG_TAG, "pulse value: " + values.getAsString(DbContract.ReadingEntry.COLUMN_READINGS_PULSE));
                long id = db.insert(DbContract.ReadingEntry.TABLE_NAME, null, values);
                Log.i(LOG_TAG, "insertion id = " + id);
                if(id > 0){
                    returnUri = DbContract.ReadingEntry.buildReadingUri(id);
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            case READING_WITH_DATE:{
                //not implemented yet
                returnUri = null;
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsDeleted;
        if(null == selection){
            selection = "1";
        }

        switch (match){
            case READING:
                rowsDeleted = db.delete(DbContract.ReadingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsUpdated;

        switch(match){
            case READING:
                rowsUpdated = db.update(DbContract.ReadingEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        switch(match){
            case READING:
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for(ContentValues value : values){
                        long id = db.insert(DbContract.ReadingEntry.TABLE_NAME, null, value);
                        if(id != -1){
                            returnCount++;
                        }
                    }
                } finally{
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown(){
        mDbHelper.close();
        super.shutdown();
    }

}