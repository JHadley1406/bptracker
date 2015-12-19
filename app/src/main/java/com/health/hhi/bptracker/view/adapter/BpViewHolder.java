package com.health.hhi.bptracker.view.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.health.hhi.bptracker.R;
import com.health.hhi.bptracker.data.DbContract;
import com.health.hhi.bptracker.model.Reading;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Josiah Hadley on 12/11/2015.
 */
public class BpViewHolder {
    @Bind(R.id.value_systolic) TextView mSystolic;
    @Bind(R.id.value_diastolic) TextView mDiastolic;
    @Bind(R.id.value_pulse) TextView mPulse;
    @Bind(R.id.value_date) TextView mDate;

    public BpViewHolder(View itemView){
        ButterKnife.bind(this, itemView);
    }


}
