package com.example.familymapclient.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.familymapclient.data.Setting;
import com.example.familymapclient.view.EventActivity;
import com.example.familymapclient.view.PersonActivity;
import com.example.familymapclient.R;
import com.example.familymapclient.view.SearchActivity;
import com.example.familymapclient.view.SettingsActivity;
import com.example.familymapclient.data.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Locale;

import Model.Event;
import Model.Person;

public class BuildHelper {
    private DataCache data;
    private Setting setting;
    public BuildHelper() {
        data = DataCache.getInstance();
        setting = Setting.getInstance();
    }

    public boolean checkDisplayByGender(String gender){
        return (isMale(gender) && setting.isMaleEventsOn()) ||
                (isFemale(gender) && setting.isFemaleEventsOn());
    }

    public boolean isMale(String gender){
        return gender.toLowerCase(Locale.ROOT).equals("m") || gender.toLowerCase(Locale.ROOT).equals("male");
    }

    public boolean isFemale(String gender){
        return gender.toLowerCase(Locale.ROOT).equals("f") || gender.toLowerCase(Locale.ROOT).equals("female");
    }

    public String eventToString(Event event){
        return event.getEventType() + ": " + event.getCity() + ", "
                + event.getCountry() + "(" + event.getYear() + ")";
    }

    public String personNameToString(Person person){
        return person.getFirstName() + " " + person.getLastName();
    }

    public void makeSearchIcon(Context context, ImageView icon){
        Drawable locationIcon;
        locationIcon = new IconDrawable(context, FontAwesomeIcons.fa_search).colorRes(R.color.purple_500).sizeDp(40);
        icon.setImageDrawable(locationIcon);
    }

    public void makeAndroidIcon(Context context, ImageView icon){
        Drawable locationIcon;
        locationIcon = new IconDrawable(context, FontAwesomeIcons.fa_android).colorRes(R.color.purple_500).sizeDp(40);
        icon.setImageDrawable(locationIcon);
    }

    public void makeLocationIcon(Context context, ImageView icon, String eventType){
        Drawable locationIcon;
        float color = 150f;
        if (data.getEventTypeColor().get(eventType.toLowerCase(Locale.ROOT))!=null){
            color = data.getEventTypeColor().get(eventType.toLowerCase(Locale.ROOT));
        }
        int intColor = (int) color;
        locationIcon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker).color(intColor).sizeDp(40);
        icon.setImageDrawable(locationIcon);
    }

    public void makeGenderIcon(Context context, ImageView icon, String gender) {
        Drawable genderIcon;
        if (gender.toLowerCase(Locale.ROOT).equals("MALE") || gender.toLowerCase(Locale.ROOT).equals("m")) {
            genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(40);
        } else {
            genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(40);
        }
        icon.setImageDrawable(genderIcon);
    }

    public void moveToSearchActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public void moveToSettingsActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public void moveToPersonActivity(Context context, String personActivityKey, String personID){
        Intent intent = new Intent();
        intent.putExtra(personActivityKey, personID);
        intent.setClass(context, PersonActivity.class);
        context.startActivity(intent);
    }

    public void moveToEventActivity(Context context, String eventActivityKey, String eventID){
        data.setSelectedEvent(data.getEventByEventID(eventID));
        Intent intent = new Intent();
        intent.putExtra(eventActivityKey, eventID);
        intent.putExtra(DataCache.getEventBooleanKey(), true);
        intent.setClass(context, EventActivity.class);
        context.startActivity(intent);
    }
}
