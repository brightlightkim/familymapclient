package com.example.familymapclient.view;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.familymapclient.R;
import com.example.familymapclient.data.DataCache;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.example.familymapclient.fragment.LoginFragment;
import com.example.familymapclient.fragment.MapFragment;

import Model.Event;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {
    private boolean isSettingDone;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment =
                fragmentManager.findFragmentById(R.id.fragment_container);
        boolean isLogout = getIntent().getBooleanExtra(DataCache.getLogoutKey(), false);
        isSettingDone = getIntent().getBooleanExtra(DataCache.getSettingKey(), false);
        if (isSettingDone){
            notifyDone();
        } else {
            if (currentFragment == null || isLogout) {
                currentFragment = createLoginFragment();
                fragmentManager
                        .beginTransaction()
                        .add(R.id.fragment_container, currentFragment)
                        .commit();
            } else {
                if (currentFragment instanceof LoginFragment) {
                    ((LoginFragment) currentFragment).registerListener(this);
                }
            }
        }

        Iconify.with(new FontAwesomeModule());
    }

    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String TAG = "MainActivity";
        Log.i(TAG, "onSaveInstanceState");
    }


    @Override
    public void notifyDone() {
        DataCache data = DataCache.getInstance();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        Event selectedEvent = data.getSelectedEvent();
        bundle.putString(DataCache.getEventIdKey(), selectedEvent.getEventID());
        bundle.putBoolean(DataCache.getSettingKey(), isSettingDone);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}