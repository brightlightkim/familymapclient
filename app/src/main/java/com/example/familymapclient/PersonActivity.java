package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.data.DataCache;
import com.example.familymapclient.fragment.MapFragment;
import com.example.familymapclient.helperModel.PersonWithRelationship;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {
    private static final String PERSON_ID_KEY = "PERSONID";
    private static final String EVENT_ID_KEY = "EVENTID";
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

        data = DataCache.getInstance();

        Intent intent = getIntent();
        String personID = intent.getStringExtra(PERSON_ID_KEY);
        selectedPerson = data.getPersonByID(personID);

        TextView firstNameView = findViewById(R.id.firstNameView);
        firstNameView.setText(selectedPerson.getFirstName());
        TextView lastNameView = findViewById(R.id.lastNameView);
        lastNameView.setText(selectedPerson.getLastName());
        TextView genderView = findViewById(R.id.genderView);
        genderView.setText(selectedPerson.getGender());

        family = data.getFamilyWithRelationship(selectedPerson);
        lifeEvents = data.getLifeEventsByPersonID(personID);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        expandableListView.setAdapter(new ExpandableListAdapter());
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int LIFE_EVENTS_POSITION = 0;
        private static final int FAMILY_POSITION = 1;
        private final BuildHelper helper = new BuildHelper();

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

            ImageView locationIconView = lifeEventView.findViewById(R.id.iconView);
            helper.makeLocationIcon(PersonActivity.this, locationIconView, desiredEvent.getEventType());

            TextView nameOfEventView = lifeEventView.findViewById(R.id.nameOfEvent);
            nameOfEventView.setText(helper.eventToString(desiredEvent));

            TextView userOfEventView = lifeEventView.findViewById(R.id.userOfEvent);
            userOfEventView.setText(helper.personNameToString(selectedPerson));

            lifeEventView.setOnClickListener(v -> {
                helper.moveToEventActivity(PersonActivity.this, EVENT_ID_KEY, desiredEvent.getEventID());
            });
        }

        private void initializeFamilyView(View familyView, int childPosition) {
            Person desiredPerson = family.get(childPosition).getPerson();

            ImageView personIconView = familyView.findViewById(R.id.iconView);
            helper.makeGenderIcon(PersonActivity.this, personIconView, desiredPerson.getGender());

            TextView nameOfPersonView = familyView.findViewById(R.id.nameOfPerson);
            nameOfPersonView.setText(helper.personNameToString(desiredPerson));

            TextView relationshipWithUserView = familyView.findViewById(R.id.relationshipWithUser);
            String relationshipWithUser = family.get(childPosition).getRelationship();
            relationshipWithUserView.setText(relationshipWithUser);

            familyView.setOnClickListener(v -> {
                helper.moveToPersonActivity(PersonActivity.this, PERSON_ID_KEY, desiredPerson.getPersonID());
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}