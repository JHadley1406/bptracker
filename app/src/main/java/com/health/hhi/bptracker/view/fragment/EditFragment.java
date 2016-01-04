package com.health.hhi.bptracker.view.fragment;


import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.telephony.CellIdentityCdma;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.health.hhi.bptracker.R;
import com.health.hhi.bptracker.data.DbContract;
import com.health.hhi.bptracker.view.MainActivity;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jrh1406 on 12/19/15.
 */
public class EditFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String LOG_TAG = EditFragment.class.getSimpleName();

    @Bind(R.id.edit_date)private DatePicker mDatePicker;
    @Bind(R.id.edit_time)private TimePicker mTimePicker;
    @Bind(R.id.edit_systolic)private EditText mSystolicEdit;
    @Bind(R.id.edit_diastolic)private EditText mDiastolicEdit;
    @Bind(R.id.edit_pusle)private EditText mPulseEdit;
    @Bind(R.id.edit_submit)private Button mSubmit;
    @Bind(R.id.edit_cancel)private Button mCancel;
    @Bind(R.id.edit_delete)private Button mDelete;

    private String[] mReadingId;

    public EditFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInsanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bp_edit, container, false);

        Bundle arguments = getArguments();

        mReadingId[0] = Integer.toString(getActivity().getIntent().getIntExtra(MainActivity.INTENT_READING, -1));

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.edit_submit) void submit(){
        ContentValues inputValues = new ContentValues();
        try{
            Log.i(LOG_TAG, "Systolic value is: " + mSystolicEdit.getText());
            inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_SYS, mSystolicEdit.getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(getContext(), "Non-Numeric Value Found Instead of Systolic Reading", Toast.LENGTH_LONG).show();
            mSystolicEdit.setText("");
            inputValues = null;
            return;
        }
        try{
            Log.i(LOG_TAG, "Diastolic value is: " + mDiastolicEdit.getText());
            inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_DIA, mDiastolicEdit.getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(getContext(), "Non-Numeric Value Found Instead of Diastolic Reading", Toast.LENGTH_LONG).show();
            mDiastolicEdit.setText("");
            inputValues = null;
            return;
        }
        try{
            Log.i(LOG_TAG, "Pulse value is: " + mPulseEdit.getText());
            inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_PULSE, mPulseEdit.getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(getContext(), "Non-Numeric Value Found Instead of Pulse Reading", Toast.LENGTH_LONG).show();
            mPulseEdit.setText("");
            inputValues = null;
            return;
        }

        if(Build.VERSION.SDK_INT>=23) {
            inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_DATE, buildDateMarshmallow());
        } else {
            inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_DATE, buildDate());
        }

        getContext().getContentResolver().insert(DbContract.ReadingEntry.CONTENT_URI, inputValues);
    }

    @OnClick(R.id.edit_cancel) void cancel(){
        //return to the list with no changes

    }

    @OnClick(R.id.edit_delete) void delete(){
        getContext().getContentResolver()
                .delete(DbContract.ReadingEntry.CONTENT_URI
                        , DbContract.ReadingEntry._ID + "= ?"
                        , mReadingId);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getContext(),
                DbContract.ReadingEntry.CONTENT_URI,
                DbContract.READING_COLUMNS,
                DbContract.ReadingEntry._ID+"=?",
                mReadingId,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cursor.getLong(cursor
                .getColumnIndex(DbContract
                        .ReadingEntry
                        .COLUMN_READINGS_DATE)));

        mDiastolicEdit.setText(Integer.toString(cursor.getInt(cursor
                .getColumnIndex(DbContract
                        .ReadingEntry
                        .COLUMN_READINGS_DIA))));
        mSystolicEdit.setText(Integer.toString(cursor.getInt(cursor
                .getColumnIndex(DbContract
                        .ReadingEntry
                        .COLUMN_READINGS_SYS))));
        mPulseEdit.setText(Integer.toString(cursor.getInt(cursor
                .getColumnIndex(DbContract
                        .ReadingEntry
                        .COLUMN_READINGS_PULSE))));

        mDatePicker.updateDate(cal.get(Calendar.YEAR)
                , cal.get(Calendar.MONTH)
                , cal.get(Calendar.DAY_OF_MONTH));
        if(Build.VERSION.SDK_INT>=23) {
            setTimeMarshmallow(cal);
        }
        else{
            setTime(cal);
        }
    }

    @SuppressWarnings("deprecation")
    private void setTime(Calendar cal){
        mTimePicker.setCurrentHour(cal.get(Calendar.HOUR));
        mTimePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
    }

    @SuppressWarnings("deprecation")
    private long buildDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(mDatePicker.getYear()
                , mDatePicker.getMonth()
                , mDatePicker.getDayOfMonth()
                , mTimePicker.getCurrentHour()
                , mTimePicker.getCurrentMinute());
        return cal.getTimeInMillis();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setTimeMarshmallow(Calendar cal){
        mTimePicker.setHour(cal.get(Calendar.HOUR));
        mTimePicker.setMinute(cal.get(Calendar.MINUTE));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private long buildDateMarshmallow(){
        Calendar cal = Calendar.getInstance();
        cal.set(mDatePicker.getYear()
                , mDatePicker.getMonth()
                , mDatePicker.getDayOfMonth()
                , mTimePicker.getHour()
                , mTimePicker.getMinute());
        return cal.getTimeInMillis();
    }

    private long buildDate(int year, int month, int day, int hour, int minute){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        return cal.getTimeInMillis();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
