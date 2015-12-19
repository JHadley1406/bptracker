package com.health.hhi.bptracker.view.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.health.hhi.bptracker.R;
import com.health.hhi.bptracker.data.DbContract;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Josiah Hadley on 12/11/2015.
 */
public class EntryFragment extends Fragment {

    private static final String LOG_TAG = EntryFragment.class.getSimpleName();

    @Bind(R.id.input_diastolic)
    EditText mDiastolic;

    @Bind(R.id.input_systolic) EditText mSystolic;
    @Bind(R.id.input_pulse) EditText mPulse;


    public EntryFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_enter_bp, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.button_bp) void submit(){

        ContentValues inputValues = new ContentValues();
        try{
            Log.i(LOG_TAG, "Systolic value is: " + mSystolic.getText());
            inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_SYS, mSystolic.getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(getContext(), "Non-Numeric Value Found Instead of Systolic Reading", Toast.LENGTH_LONG).show();
            mSystolic.setText("");
            inputValues = null;
            return;
        }
        try{
            Log.i(LOG_TAG, "Diastolic value is: " + mDiastolic.getText());
            inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_DIA, mDiastolic.getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(getContext(), "Non-Numeric Value Found Instead of Diastolic Reading", Toast.LENGTH_LONG).show();
            mDiastolic.setText("");
            inputValues = null;
            return;
        }
        try{
            Log.i(LOG_TAG, "Pulse value is: " + mPulse.getText());
            inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_PULSE, mPulse.getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(getContext(), "Non-Numeric Value Found Instead of Pulse Reading", Toast.LENGTH_LONG).show();
            mPulse.setText("");
            inputValues = null;
            return;
        }

        inputValues.put(DbContract.ReadingEntry.COLUMN_READINGS_DATE, System.currentTimeMillis());

        getContext().getContentResolver().insert(DbContract.ReadingEntry.CONTENT_URI, inputValues);

    }


}
