package com.example.familymapclient.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

import androidx.preference.SwitchPreferenceCompat;

import com.example.familymapclient.R;
import com.example.familymapclient.data.DataCache;
import com.example.familymapclient.data.Setting;
import com.example.familymapclient.view.MainActivity;

public class SettingsFragment extends PreferenceFragment {
    private final Setting setting = Setting.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SwitchPreferenceCompat lifeStoryLineOption = findPreference(
                getString(R.string.life_story_line_key));

        assert lifeStoryLineOption != null;
        lifeStoryLineOption.setOnPreferenceClickListener(preference -> {
            setting.setLifeStoryLineOn(lifeStoryLineOption.isChecked());
            return false;
        });

        SwitchPreferenceCompat familyTreeLineOption = findPreference(
                getString(R.string.family_tree_lines_key));
        assert familyTreeLineOption != null;
        familyTreeLineOption.setOnPreferenceClickListener(preference -> {
            setting.setFamilyTreeLineOn(familyTreeLineOption.isChecked());
            return false;
        });

        SwitchPreferenceCompat spouseLineOption = findPreference(
                getString(R.string.spouse_lines_key));
        assert spouseLineOption != null;
        spouseLineOption.setOnPreferenceClickListener(preference -> {
            setting.setSpouseLineOn(spouseLineOption.isChecked());
            return false;
        });

        SwitchPreferenceCompat fatherSideOption = findPreference(
                getString(R.string.father_side_key));
        assert fatherSideOption != null;
        fatherSideOption.setOnPreferenceClickListener(preference -> {
            setting.setFatherSideOn(fatherSideOption.isChecked());
            return false;
        });

        SwitchPreferenceCompat motherSideOption = findPreference(
                getString(R.string.mother_side_key));
        assert motherSideOption != null;
        motherSideOption.setOnPreferenceClickListener(preference -> {
            setting.setMotherSideOn(motherSideOption.isChecked());
            return false;
        });

        SwitchPreferenceCompat maleEventsOption = findPreference(
                getString(R.string.filter_male_key));
        assert maleEventsOption != null;
        maleEventsOption.setOnPreferenceClickListener(preference -> {
            setting.setMaleEventsOn(maleEventsOption.isChecked());
            return false;
        });

        SwitchPreferenceCompat femaleEventsOption = findPreference(
                getString(R.string.filter_female_key));
        assert femaleEventsOption != null;
        femaleEventsOption.setOnPreferenceClickListener(preference -> {
            setting.setFemaleEventsOn(femaleEventsOption.isChecked());
            return false;
        });

        Preference logoutButton = findPreference(getString(R.string.logout_key));
        assert logoutButton != null;
        logoutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(DataCache.getLogoutKey(), true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {

    }

}