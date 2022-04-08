package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.familymapclient.data.DataCache;
import com.example.familymapclient.fragment.MapFragment;
import com.example.familymapclient.helperModel.PersonWithRelationship;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {
    private DataCache data;
    private ArrayList<Event> lifeEvents;
    private ArrayList<PersonWithRelationship> family;
    private Person selectedPerson;
    private Person father;
    private Person mother;
    private Person spouse;
    private Person child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        data = DataCache.getInstance();
        //TODO: Change the personID from the Intent later
        String personID = "personID";
        selectedPerson = data.getPersonByID(personID);
        family = data.getFamilyWithRelationship(selectedPerson);
        lifeEvents = data.getLifeEventsByPersonID(personID);

        expandableListView.setAdapter(new ExpandableListAdapter());
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter{
        private static final int LIFE_EVENTS_POSITION = 0;
        private static final int FAMILY_POSITION = 1;

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    return lifeEvents.size();
                case FAMILY_POSITION:
                    return family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    titleView.setText(R.string.life_events);
                    break;
                case FAMILY_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case LIFE_EVENTS_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event, parent, false);
                    initializeLifeEventView(itemView, childPosition);
                    break;
                case FAMILY_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeLifeEventView(View lifeEventView, int childPosition) {
            Event desiredEvent = lifeEvents.get(childPosition);
            TextView nameOfEventView = lifeEventView.findViewById(R.id.nameOfEvent);
            String eventDetailText = desiredEvent.getEventType() + ": " +
                    desiredEvent.getCity() + ", " + desiredEvent.getCountry();
            nameOfEventView.setText(eventDetailText);

            TextView userOfEventView = lifeEventView.findViewById(R.id.userOfEvent);
            String personNameOfEvent = selectedPerson.getFirstName() + " " + selectedPerson.getLastName();
            userOfEventView.setText(personNameOfEvent);

            lifeEventView.setOnClickListener(v -> {
                openMapFragment();
                //TODO: Send the event Value
            });
        }

        private void initializeFamilyView(View familyView, int childPosition) {
            Person desiredPerson = family.get(childPosition).getPerson();
            TextView nameOfPersonView = familyView.findViewById(R.id.nameOfPerson);
            String personName = desiredPerson.getFirstName() + " " + desiredPerson.getLastName();
            nameOfPersonView.setText(personName);

            TextView relationshipWithUserView = familyView.findViewById(R.id.relationshipWithUser);
            String relationshipWithUser = family.get(childPosition).getRelationship();
            relationshipWithUserView.setText(relationshipWithUser);

            familyView.setOnClickListener(v -> {
                //TODO: Make a new Person Activity with the given personID
            });
        }



        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void openMapFragment(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}