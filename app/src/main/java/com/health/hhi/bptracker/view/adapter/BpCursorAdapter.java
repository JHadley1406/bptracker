package com.health.hhi.bptracker.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.health.hhi.bptracker.R;
import com.health.hhi.bptracker.data.DbContract;
import com.health.hhi.bptracker.model.Reading;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by Josiah Hadley on 12/11/2015.
 */
public class BpCursorAdapter extends CursorAdapter {

    private List<Reading> readings;

    public BpCursorAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_bp, parent, false);

        BpViewHolder viewHolder = new BpViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        BpViewHolder viewHolder = (BpViewHolder) view.getTag();
        viewHolder.mSystolic.setText(Integer.toString(cursor.getInt(cursor
                .getColumnIndex(DbContract
                        .ReadingEntry
                        .COLUMN_READINGS_SYS))));
        viewHolder.mDiastolic.setText(Integer.toString(cursor.getInt(cursor
                .getColumnIndex(DbContract
                        .ReadingEntry
                        .COLUMN_READINGS_DIA))));
        viewHolder.mPulse.setText(Integer.toString(cursor.getInt(cursor
                .getColumnIndex(DbContract
                        .ReadingEntry
                        .COLUMN_READINGS_PULSE))));
        viewHolder.mDate.setText(readableDate(cursor.getLong(cursor
                .getColumnIndex(DbContract
                        .ReadingEntry
                        .COLUMN_READINGS_DATE))));
    }


    private String readableDate(long utc){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
        return simpleDateFormat.format(utc);
    }
}
