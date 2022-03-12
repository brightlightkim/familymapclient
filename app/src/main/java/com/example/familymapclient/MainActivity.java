package com.example.familymapclient;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.familymapclient.fragment.LoginFragment;
import com.example.familymapclient.fragment.MapFragment;

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
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //savedInstanceState.putInt(KEY_INDEX, getQuizViewModel().getCurrentIndex());
    }


    @Override
    public void notifyDone() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}