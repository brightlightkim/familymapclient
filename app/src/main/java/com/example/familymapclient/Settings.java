package com.example.familymapclient;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class Settings extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}