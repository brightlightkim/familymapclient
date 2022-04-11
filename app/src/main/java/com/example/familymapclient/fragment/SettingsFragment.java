package com.example.familymapclient.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

import androidx.preference.SwitchPreferenceCompat;

import com.example.familymapclient.R;
import com.example.familymapclient.data.DataCache;
import com.example.familymapclient.data.Setting;

public class SettingsFragment extends PreferenceFragment {
    private final DataCache data = DataCache.getInstance();
    private final Setting setting = Setting.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SwitchPreferenceCompat lifeStoryLineOption = (SwitchPreferenceCompat)findPreference(
                getString(R.string.life_story_line_key));
        lifeStoryLineOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                setting.setLifeStoryLineOn(lifeStoryLineOption.isChecked());
                return false;
            }
        });

        SwitchPreferenceCompat familyTreeLineOption = (SwitchPreferenceCompat)findPreference(
                getString(R.string.family_tree_lines_key));
        familyTreeLineOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                setting.setFamilyTreeLineOn(familyTreeLineOption.isChecked());
                return false;
            }
        });

        SwitchPreferenceCompat spouseLineOption = (SwitchPreferenceCompat)findPreference(
                getString(R.string.spouse_lines_key));
        spouseLineOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                setting.setSpouseLineOn(spouseLineOption.isChecked());
                return false;
            }
        });

        SwitchPreferenceCompat fatherSideOption = (SwitchPreferenceCompat)findPreference(
                getString(R.string.father_side_key));
        fatherSideOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                setting.setFatherSideOn(fatherSideOption.isChecked());
                return false;
            }
        });

        SwitchPreferenceCompat motherSideOption = (SwitchPreferenceCompat)findPreference(
                getString(R.string.mother_side_key));
        motherSideOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                setting.setMotherSideOn(motherSideOption.isChecked());
                return false;
            }
        });

        SwitchPreferenceCompat maleEventsOption = (SwitchPreferenceCompat)findPreference(
                getString(R.string.filter_male_key));
        maleEventsOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                setting.setMaleEventsOn(maleEventsOption.isChecked());
                return false;
            }
        });

        SwitchPreferenceCompat femaleEventsOption = (SwitchPreferenceCompat)findPreference(
                getString(R.string.filter_female_key));
        femaleEventsOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                setting.setFemaleEventsOn(femaleEventsOption.isChecked());
                return false;
            }
        });

        Preference logoutButton = findPreference(getString(R.string.logout_key));
        logoutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Open the Login Fragment again with no inputs
                return false;
            }
        });
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {

    }
}