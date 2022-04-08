package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.familymapclient.data.DataCache;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {
    private DataCache data;
    private ArrayList<Event> lifeEvents;
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
        father = data.getPersonByID(selectedPerson.getFatherID());
        mother = data.getPersonByID(selectedPerson.getMotherID());
        spouse = data.getPersonByID(selectedPerson.getSpouseID());
        child = data.getChildById(selectedPerson.getPersonID());
        lifeEvents = data.getLifeEventsByPersonID(personID);
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 0;
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
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}