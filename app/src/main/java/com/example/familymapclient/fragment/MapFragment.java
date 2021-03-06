package com.example.familymapclient.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.util.BuildHelper;
import com.example.familymapclient.view.EventActivity;
import com.example.familymapclient.data.Setting;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.example.familymapclient.R;
import com.example.familymapclient.data.DataCache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {
    private static final int COLOR_LIGHT_GREEN_ARGB = 0xff81C784;
    private static final int COLOR_LIGHT_ORANGE_ARGB = 0xffF9A825;
    private static final float DEFAULT_LINE_WIDTH = 8.0f;
    private static final String PERSON_ID_KEY = "PERSONID";
    private Setting setting;
    private GoogleMap map;
    private DataCache data;
    private TextView textView;
    private ImageView icon;
    private Polyline lifeEventLine;
    private Polyline spouseLine;
    private ArrayList<Polyline> fatherSideLine;
    private ArrayList<Polyline> motherSideLine;
    private BuildHelper helper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        Iconify.with(new FontAwesomeModule());
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        setting = Setting.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        data = DataCache.getInstance();
        icon = view.findViewById(R.id.iconView);
        textView = view.findViewById(R.id.mapTextView);
        helper = new BuildHelper();

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        map.setOnMapLoadedCallback(this);

        setSetting();
        if (getArguments() != null) {

            boolean isEventActivity = getArguments().getBoolean(DataCache.getEventBooleanKey());
            boolean isSettingDone = getArguments().getBoolean(DataCache.getSettingKey());
            String selectedEventID = getArguments().getString(DataCache.getEventIdKey());

            Event selectedEvent = data.getEventByEventID(selectedEventID);
            Person selectedPerson = data.getPersonByID(selectedEvent.getPersonID());
            LatLng eventLocation = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
            setEventsMarkersBySetting();
            if (isEventActivity || isSettingDone) {
                helper.makeGenderIcon(getContext(), icon, selectedPerson.getGender());
                makeEventText(selectedPerson, selectedEvent);
                makeLinesBySettings(selectedPerson, selectedEvent);
            } else {
                helper.makeAndroidIcon(getContext(), icon);
            }
            map.setOnMarkerClickListener(this);
        } else {
            throw new Error("does not have selected event");
        }
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        assert getArguments() != null;
        boolean isEventActivity = getArguments().getBoolean(DataCache.getEventBooleanKey());
        if (isEventActivity) {
            ((EventActivity) getActivity()).getSupportActionBar().setTitle("Family Map: Event");
        } else {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.main_menu, menu);

            MenuItem searchMenuItem = menu.findItem(R.id.searchItem);
            searchMenuItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_search)
                    .colorRes(R.color.white).actionBarSize());

            MenuItem settingsMenuItem = menu.findItem(R.id.settings);
            settingsMenuItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_gear)
                    .colorRes(R.color.white).actionBarSize());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case R.id.searchItem:
                helper.moveToSearchActivity(getContext());
                return true;
            case R.id.settings:
                helper.moveToSettingsActivity(getContext());
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Event selectedEvent = (Event) marker.getTag();
        Person selectedPerson = data.getPersonByID(selectedEvent.getPersonID());

        helper.makeGenderIcon(getContext(), icon, selectedPerson.getGender());
        makeEventText(selectedPerson, selectedEvent);

        makeLinesBySettings(selectedPerson, selectedEvent);
        data.setSelectedEvent(selectedEvent);

        return false;
    }

    private void setEventsMarkersBySetting() {
        Set<Event> events = new HashSet<>();
        if (setting.isMaleEventsOn()) {
            events.addAll(data.getMaleEvents());
        }
        if (setting.isFemaleEventsOn()) {
            events.addAll(data.getFemaleEvents());
        }
        data.setSelectedEvents(events);
        for (Event event : events) {
            LatLng eventPoint = new LatLng(event.getLatitude(), event.getLongitude());
            float color;

            if (data.getEventTypeColor().get(event.getEventType().toLowerCase(Locale.ROOT)) == null) {
                color = data.getColors()[data.getColorNum()];
                data.getEventTypeColor().put(event.getEventType().toLowerCase(Locale.ROOT), color);

                if (data.getColorNum() + 1 == DataCache.getMaxColorNum()) {
                    data.setColorNum(DataCache.getMinColorNum()); //set to minimum number.
                }
                data.setColorNum(data.getColorNum() + 1);
            }
            else {
                color = data.getEventTypeColor().get(event.getEventType().toLowerCase(Locale.ROOT));
            }

            Marker marker = map.addMarker(new MarkerOptions().position(eventPoint).icon(BitmapDescriptorFactory.defaultMarker
                    (color)));

            marker.setTag(event);
        }
    }

    private void makeEventText(Person person, Event event) {
        String text = person.getFirstName() + " " + person.getLastName() +
                "\n" +
                event.getEventType() + ": " + event.getCity() +
                " " + event.getCountry() +
                " (" + event.getYear() + ")";
        textView.setText(text);
        textView.setOnClickListener(v -> helper.moveToPersonActivity(getActivity(), PERSON_ID_KEY, person.getPersonID()));
    }

    //Call this whenever change occur in setting.
    private void makeLinesBySettings(Person selectedPerson, Event selectedEvent) {
        clearLines();
        if (helper.checkDisplayByGender(selectedPerson.getGender())) {
            if (setting.isFamilyTreeLineOn()) {
                createFamilyTreeLines(selectedPerson, selectedEvent);
            }
            if (setting.isLifeStoryLineOn()) {
                createLifeEventsLines(selectedPerson.getPersonID());
            }
            if (setting.isSpouseLineOn()) {
                createSpouseLine(selectedPerson, selectedEvent);
            }
        }
    }

    private void setSetting() {
        SharedPreferences settingPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        setting.setLifeStoryLineOn(settingPreferences.getBoolean(getString(R.string.life_story_line_key), true));
        setting.setFamilyTreeLineOn(settingPreferences.getBoolean(getString(R.string.family_tree_lines_key), true));
        setting.setFatherSideOn(settingPreferences.getBoolean(getString(R.string.father_side_key), true));
        setting.setMotherSideOn(settingPreferences.getBoolean(getString(R.string.mother_side_key), true));
        setting.setSpouseLineOn(settingPreferences.getBoolean(getString(R.string.spouse_lines_key), true));
        setting.setMaleEventsOn(settingPreferences.getBoolean(getString(R.string.filter_male_key), true));
        setting.setFemaleEventsOn(settingPreferences.getBoolean(getString(R.string.filter_female_key), true));
    }

    private void createFamilyTreeLines(Person person, Event event) {
        fatherSideLine = new ArrayList<>();
        motherSideLine = new ArrayList<>();
        if (person.getFatherID() != null && setting.isFatherSideOn() && setting.isMaleEventsOn()) {
            Person father = data.getPersonByID(person.getFatherID());
            ArrayList<Event> fatherLifeEvents = data.getLifeEventsByPersonID(father.getPersonID());
            Event fatherBirthEvent = fatherLifeEvents.get(0);
            makeLineBetweenChildAndParent(event, fatherBirthEvent, "father", DEFAULT_LINE_WIDTH);
            createParentSideLine(father, fatherBirthEvent, DEFAULT_LINE_WIDTH - 3f);
        }
        //gets a person's mother id
        if (person.getMotherID() != null && setting.isMotherSideOn() && setting.isFemaleEventsOn()) {
            Person mother = data.getPersonByID(person.getMotherID());
            ArrayList<Event> motherLifeEvents = data.getLifeEventsByPersonID(mother.getPersonID());
            Event motherBirthEvent = motherLifeEvents.get(0);
            makeLineBetweenChildAndParent(event, motherBirthEvent, "mother", DEFAULT_LINE_WIDTH);
            createParentSideLine(mother, motherBirthEvent, DEFAULT_LINE_WIDTH - 3f);
        }
    }

    private void createParentSideLine(Person person, Event event, float lineWidth) {
        float shortenedLineWidth = lineWidth - 3f;
        if (shortenedLineWidth <= 2f) {
            shortenedLineWidth = 2f;
        }
        //gets a person's father side
        if (person.getFatherID() != null && setting.isMaleEventsOn()) {
            ArrayList<Event> fatherLifeEvents = data.getLifeEventsByPersonID(person.getFatherID());
            Person father = data.getPersonByID(person.getFatherID());
            if (father != null && fatherLifeEvents != null) {
                Event fatherBirthEvent = fatherLifeEvents.get(0);
                makeLineBetweenChildAndParent(event, fatherBirthEvent, "father", lineWidth);
                createParentSideLine(father, fatherBirthEvent, shortenedLineWidth);
            }
        }
        //gets a person's mother side
        if (person.getMotherID() != null && setting.isFemaleEventsOn()) {
            ArrayList<Event> motherLifeEvents = data.getLifeEventsByPersonID(person.getMotherID());
            Person mother = data.getPersonByID(person.getMotherID());
            if (mother != null && motherLifeEvents != null) {
                Event motherBirthEvent = motherLifeEvents.get(0);
                makeLineBetweenChildAndParent(event, motherBirthEvent, "mother", lineWidth);
                createParentSideLine(mother, motherBirthEvent, shortenedLineWidth);
            }
        }
    }

    private void makeLineBetweenChildAndParent(Event childEvent, Event parentEvent, String parent, float lineWidth) {
        Polyline lineToParent = map.addPolyline(new PolylineOptions()
                .add(new LatLng(childEvent.getLatitude(), childEvent.getLongitude()))
                .add(new LatLng(parentEvent.getLatitude(), parentEvent.getLongitude()))
                .color(Color.BLUE).width(lineWidth));
        if (parent.equals("father")) {
            fatherSideLine.add(lineToParent);
        } else {
            motherSideLine.add(lineToParent);
        }
    }

    private void clearLines() {
        if (lifeEventLine != null) {
            lifeEventLine.remove();
        }

        if (fatherSideLine != null) {
            removeLine(fatherSideLine);
        }

        if (motherSideLine != null) {
            removeLine(motherSideLine);
        }

        if (spouseLine != null) {
            spouseLine.remove();
        }
    }

    private void removeLine(ArrayList<Polyline> lines) {
        for (Polyline line : lines) {
            line.remove();
        }
    }

    private void createLifeEventsLines(String personID) {
        if (lifeEventLine != null) {
            lifeEventLine.remove();
        }
        ArrayList<Event> lifeEventList = data.getLifeEventsByPersonID(personID);
        ArrayList<LatLng> lifePoints = new ArrayList<>();
        if (lifeEventList != null) {
            for (int i = 0; i < lifeEventList.size(); i++) {
                lifePoints.add(new LatLng(lifeEventList.get(i).getLatitude(), lifeEventList.get(i).getLongitude()));
            }
            lifeEventLine = map.addPolyline(new PolylineOptions().addAll(lifePoints).color(COLOR_LIGHT_GREEN_ARGB));
        }
    }

    private void createSpouseLine(Person person, Event event) {
        if (person.getSpouseID() != null) {
            Person spouse = data.getPersonByID(person.getSpouseID());
            if (helper.checkDisplayByGender(spouse.getGender())){
                Event spouseBirthEvent = data.getLifeEventsByPersonID(person.getSpouseID()).get(0);
                spouseLine = map.addPolyline(new PolylineOptions().add(new LatLng(event.getLatitude(), event.getLongitude()))
                        .add(new LatLng(spouseBirthEvent.getLatitude(), spouseBirthEvent.getLongitude())).color(COLOR_LIGHT_ORANGE_ARGB));
            }
        }
    }
}