package com.health.hhi.bptracker.view;




import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.health.hhi.bptracker.R;
import com.health.hhi.bptracker.view.fragment.EntryFragment;
import com.health.hhi.bptracker.view.fragment.ListFragment;


/**
 * Created by Josiah Hadley on 12/12/2015.
 */
public class MainActivity extends FragmentActivity {

    public static final String INTENT_READING = "intent_reading";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        EntryFragment entryFragment = new EntryFragment();
        ListFragment listFragment = new ListFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.container_input, entryFragment);
        transaction.add(R.id.container_display, listFragment);

        transaction.commit();
    }

}
