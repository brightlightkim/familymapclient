package com.example.familymapclient;

import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.familymapclient.data.DataCache;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.example.familymapclient.fragment.LoginFragment;
import com.example.familymapclient.fragment.MapFragment;

import Model.Event;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {
    private final String TAG = "MainActivity";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment =
                fragmentManager.findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            currentFragment = createLoginFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
                    .commit();
        } else {
            if(currentFragment instanceof LoginFragment) {
                ((LoginFragment) currentFragment).registerListener(this);
            }
        }

        Iconify.with(new FontAwesomeModule()); //Setting the Icon.
        /**
         * For Activity to change to another Activity,
         * We do it by intent.
         *
         * Sending
         * Intent intent = new Intent(MainActivity.this, ReceivingActivity.class);
         * intent.putExtra(ReceivingActivity.TEXT_KEY, editText.getText().toString());
         * startActivity(intent); //It begins the activity.
         *
         * Getting
         * Intent intent = getIntent();
         * String receivedText = intent.getString(TEXT_KEY);
         */
    }

    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    private Fragment createMapFragment() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //savedInstanceState.putInt(KEY_INDEX, getQuizViewModel().getCurrentIndex());
    }


    @Override
    public void notifyDone() {
        DataCache data = DataCache.getInstance();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        String userPersonID = data.getUser().getPersonID();
        Event userFirstEvent = data.getLifeEventsByPersonID(userPersonID).get(0);
        bundle.putString(DataCache.getEventIdKey(), userFirstEvent.getEventID());
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}