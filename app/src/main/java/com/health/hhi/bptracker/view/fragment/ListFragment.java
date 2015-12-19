package com.health.hhi.bptracker.view.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.health.hhi.bptracker.R;
import com.health.hhi.bptracker.data.DbContract;
import com.health.hhi.bptracker.view.adapter.BpCursorAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Josiah Hadley on 12/11/2015.
 */
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ListFragment.class.getSimpleName();
    private static final int READING_LOADER = 0;

    @Bind(R.id.listview_readings)
    ListView mReadingsList;

    private int mPosition = ListView.INVALID_POSITION;
    private BpCursorAdapter mBpCursorAdapter;

    public ListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Bundle arguments = getArguments();

        View rootView = inflater.inflate(R.layout.fragment_bp_list, container, false);
        ButterKnife.bind(this, rootView);

        mBpCursorAdapter = new BpCursorAdapter(getContext(), null, 0);

        mReadingsList.setAdapter(mBpCursorAdapter);

        mReadingsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Uri deleteUri = DbContract.ReadingEntry.buildReadingUri(position);
                getContext().getContentResolver().delete(DbContract.ReadingEntry.CONTENT_URI, DbContract.ReadingEntry._ID+"="+mBpCursorAdapter.getItemId(position), null);
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(READING_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "onCreateLoader id: " + id);
        String sortOrder = DbContract.ReadingEntry.COLUMN_READINGS_DATE + " ASC";
        Uri readingUri = DbContract.ReadingEntry.buildReadingUri(id);

        return new CursorLoader(getContext(),
                DbContract.ReadingEntry.CONTENT_URI,
                DbContract.READING_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mBpCursorAdapter.swapCursor(data);
        if(mPosition != ListView.INVALID_POSITION){
            mReadingsList.smoothScrollToPosition(mPosition);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBpCursorAdapter.swapCursor(null);

    }

}
