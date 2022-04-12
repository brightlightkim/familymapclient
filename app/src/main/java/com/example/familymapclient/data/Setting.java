package com.example.familymapclient.data;

import android.content.SharedPreferences;

import androidx.preference.SwitchPreferenceCompat;

import com.example.familymapclient.R;

public class Setting {

    private static Setting instance;
    private static boolean defaultValue;
    private static boolean isLifeStoryLineOn;
    private static boolean isFamilyTreeLineOn;
    private static boolean isSpouseLineOn;
    private static boolean isFatherSideOn;
    private static boolean isMotherSideOn;
    private static boolean isMaleEventsOn;
    private static boolean isFemaleEventsOn;

    private Setting(){

    }

    public synchronized static Setting getInstance() {
        if (instance == null) {
            instance = new Setting();
            defaultValue = true;
        }
        return instance;
    }

    public boolean isLifeStoryLineOn() {
        return isLifeStoryLineOn;
    }

    public void setLifeStoryLineOn(boolean lifeStoryLineOn) {
        isLifeStoryLineOn = lifeStoryLineOn;
    }

    public boolean isFamilyTreeLineOn() {
        return isFamilyTreeLineOn;
    }

    public void setFamilyTreeLineOn(boolean familyTreeLineOn) {
        isFamilyTreeLineOn = familyTreeLineOn;
    }

    public boolean isSpouseLineOn() {
        return isSpouseLineOn;
    }

    public void setSpouseLineOn(boolean spouseLineOn) {
        isSpouseLineOn = spouseLineOn;
    }

    public boolean isFatherSideOn() {
        return isFatherSideOn;
    }

    public void setFatherSideOn(boolean fatherSideOn) {
        isFatherSideOn = fatherSideOn;
    }

    public boolean isMotherSideOn() {
        return isMotherSideOn;
    }

    public void setMotherSideOn(boolean motherSideOn) {
        isMotherSideOn = motherSideOn;
    }

    public boolean isMaleEventsOn() {
        return isMaleEventsOn;
    }

    public void setMaleEventsOn(boolean maleEventsOn) {
        isMaleEventsOn = maleEventsOn;
    }

    public boolean isFemaleEventsOn() {
        return isFemaleEventsOn;
    }

    public void setFemaleEventsOn(boolean femaleEventsOn) {
        isFemaleEventsOn = femaleEventsOn;
    }
}
