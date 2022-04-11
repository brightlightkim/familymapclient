package com.example.familymapclient.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.BuildHelper;
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

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {
    private static final int COLOR_LIGHT_GREEN_ARGB = 0xff81C784;
    private static final int COLOR_LIGHT_ORANGE_ARGB = 0xffF9A825;
    private static final String PERSON_ID_KEY = "PERSONID";
    private GoogleMap map;
    private DataCache data;
    private TextView textView;
    private ImageView icon;
    private Polyline lifeEventLine;
    private Polyline spouseLine;
    private ArrayList<Polyline> fatherSideLine;
    private ArrayList<Polyline> motherSideLine;
    private BuildHelper helper;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        Iconify.with(new FontAwesomeModule());

        View view = inflater.inflate(R.layout.map_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        data = DataCache.getInstance();
        icon = view.findViewById(R.id.iconView);
        textView = view.findViewById(R.id.mapTextView);
        helper = new BuildHelper();

        return view;

        /**
         * For Drawing Icons
         *
         * import com.joanzapata.iconify.IconDrawable;
         * import com.joanzapata.iconify.fonts.FontAwesomeIcons;
         * …
         * Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
         * colorRes(R.color.male_icon).sizeDp(40);
         * genderImageView.setImageDrawable(genderIcon);
         * The constant values for the necessary icons are:
         * ● fa_male (male icon)
         * ● fa_female (female icon)
         * ● fa_map_marker (event icon)
         * ● fa_search (search icon)
         * fa_gear (settings icon)
         */
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;// Add a marker in Sydney and move the camera

        map.setOnMapLoadedCallback(this);

        setEventsMarkersForFirstLanding();
        if (getArguments() != null){
            boolean isEventActivity = getArguments().getBoolean(DataCache.getEventBooleanKey());
            String selectedEventID = getArguments().getString(DataCache.getEventIdKey());

            Event selectedEvent = data.getEventByEventID(selectedEventID);
            LatLng eventLocation = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            map.addMarker(new MarkerOptions().position(eventLocation).title(selectedEvent.getEventType()));
            map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));

            if (isEventActivity){
                Person selectedPerson = data.getPersonByID(selectedEvent.getPersonID());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.searchItem);
        searchMenuItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.white).actionBarSize());

        MenuItem settingsMenuItem = menu.findItem(R.id.settings);
        settingsMenuItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.white).actionBarSize());
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

        //TODO: It will be changed according to the Settings events
        //TODO: Create a setting activity.
        //TODO: Filters by gender and filters by father or mother side

        return false;
    }

    private void setEventsMarkersForFirstLanding() {
        for (Event event : data.getUserEvents()) {
            LatLng eventPoint = new LatLng(event.getLatitude(), event.getLongitude());
            float color;

            if (data.getEventTypeColor().get(event.getEventType()) == null) {
                color = data.getColors()[data.getColorNum()];
                data.getEventTypeColor().put(event.getEventType(), color);

                if (data.getColorNum() + 1 == DataCache.getMaxColorNum()) {
                    data.setColorNum(DataCache.getMinColorNum()); //set to minimum number.
                }
                data.setColorNum(data.getColorNum() + 1);
            } // Set until 0-9
            else { //after setting everything
                color = data.getEventTypeColor().get(event.getEventType());
            }

            Marker marker = map.addMarker(new MarkerOptions().position(eventPoint).icon(BitmapDescriptorFactory.defaultMarker
                    (color)));

            marker.setTag(event);
        }
    }

    private void makeEventText(Person person, Event event) {
        StringBuilder text = new StringBuilder();
        text.append(person.getFirstName()).append(" ").append(person.getLastName());
        text.append("\n");
        text.append(event.getEventType()).append(": ").append(event.getCity())
                .append(" ").append(event.getCountry())
                .append(" (").append(event.getYear()).append(")");
        textView.setText(text.toString());
        textView.setOnClickListener(v -> {
            helper.moveToPersonActivity(getActivity(), PERSON_ID_KEY, person.getPersonID());
        });
    }

    private void makeLinesBySettings(Person selectedPerson, Event selectedEvent){
        clearLines();
        createFamilyTreeLines(selectedPerson, selectedEvent);
        createLifeEventsLines(selectedPerson.getPersonID());
        createSpouseLine(selectedPerson, selectedEvent);
    }

    private void createFamilyTreeLines(Person person, Event event) {
        //gets a person's father id
        //TODO: Add settings here to make lines or not.
        if (person.getFatherID() != null) {
            fatherSideLine = new ArrayList<>();
            createParentSideLine(person, event, "father");
        }
        //gets a person's mother id
        if (person.getMotherID() != null) {
            motherSideLine = new ArrayList<>();
            createParentSideLine(person, event, "mother");
        }
    }

    private void createParentSideLine(Person person, Event event, String parent) {
        //gets a person's father side
        if (person.getFatherID() != null) {
            ArrayList<Event> fatherLifeEvents = data.getLifeEventsByPersonID(person.getFatherID());
            Person father = data.getPersonByID(person.getFatherID());
            if (father != null && fatherLifeEvents != null) {
                Event fatherBirthEvent = fatherLifeEvents.get(0);
                Polyline lineToParent = map.addPolyline(new PolylineOptions()
                        .add(new LatLng(event.getLatitude(), event.getLongitude()))
                        .add(new LatLng(fatherBirthEvent.getLatitude(), fatherBirthEvent.getLongitude()))
                        .color(Color.BLUE));
                if (parent.equals("father")) {
                    fatherSideLine.add(lineToParent);
                } else {
                    motherSideLine.add(lineToParent);
                }
                createParentSideLine(father, fatherBirthEvent, parent);
            }
        }
        //gets a person's mother side
        if (person.getMotherID() != null) {
            ArrayList<Event> motherLifeEvents = data.getLifeEventsByPersonID(person.getMotherID());
            Person mother = data.getPersonByID(person.getMotherID());
            if (mother != null && motherLifeEvents != null) {
                Event motherBirthEvent = motherLifeEvents.get(0);
                Polyline lineToParent = map.addPolyline(new PolylineOptions()
                        .add(new LatLng(event.getLatitude(), event.getLongitude()))
                        .add(new LatLng(motherBirthEvent.getLatitude(), motherBirthEvent.getLongitude()))
                        .color(Color.BLUE));
                if (parent.equals("father")) {
                    fatherSideLine.add(lineToParent);
                } else {
                    motherSideLine.add(lineToParent);
                }
                createParentSideLine(mother, motherBirthEvent, parent);
            }
        }
    }

    private void clearLines(){
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
        for (int i = 0; i < lifeEventList.size(); i++) {
            lifePoints.add(new LatLng(lifeEventList.get(i).getLatitude(), lifeEventList.get(i).getLongitude()));
        }
        lifeEventLine = map.addPolyline(new PolylineOptions().addAll(lifePoints).color(COLOR_LIGHT_GREEN_ARGB));
    }

    private void createSpouseLine(Person person, Event event) {
        if (person.getSpouseID() != null) {
            Event spouseBirthEvent = data.getLifeEventsByPersonID(person.getSpouseID()).get(0);
            spouseLine = map.addPolyline(new PolylineOptions().add(new LatLng(event.getLatitude(), event.getLongitude()))
                    .add(new LatLng(spouseBirthEvent.getLatitude(), spouseBirthEvent.getLongitude())).color(COLOR_LIGHT_ORANGE_ARGB));
        }
    }
}