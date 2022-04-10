package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.familymapclient.data.DataCache;
import com.example.familymapclient.fragment.MapFragment;

import Model.Event;

public class EventActivity extends AppCompatActivity {
    private final DataCache data = DataCache.getInstance();
    private static final String EVENT_ID_KEY = "EVENTID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        Intent intent = getIntent();
        Bundle arguments = new Bundle();
        arguments.putString(EVENT_ID_KEY, intent.getStringExtra(EVENT_ID_KEY));
        arguments.putBoolean(DataCache.getEventBooleanKey(),
                intent.getBooleanExtra(DataCache.getEventBooleanKey(), true));
        fragment.setArguments(arguments);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}