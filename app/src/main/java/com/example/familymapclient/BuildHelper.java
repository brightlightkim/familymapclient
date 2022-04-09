package com.example.familymapclient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.familymapclient.data.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Locale;

import Model.Event;
import Model.Person;

public class BuildHelper {
    private DataCache data;
    public BuildHelper() {
        data = DataCache.getInstance();
    }

    public String eventToString(Event event){
        return event.getEventType() + ": " + event.getCity() + ", "
                + event.getCountry() + "(" + event.getYear() + ")";
    }

    public String personNameToString(Person person){
        return person.getFirstName() + " " + person.getLastName();
    }

    public void makeLocationIcon(Context context, ImageView icon, String eventType){
        Drawable locationIcon;
        float color = data.getEventTypeColor().get(eventType);
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
}
