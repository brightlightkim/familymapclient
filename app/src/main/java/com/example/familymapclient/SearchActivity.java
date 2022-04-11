package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.familymapclient.data.DataCache;

import java.util.ArrayList;

import Model.Person;
import Model.Event;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_TYPE = 0;
    private static final int EVENT_TYPE = 1;
    private DataCache data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        data = DataCache.getInstance();
        EditText searchText = findViewById(R.id.searchViewText);

        ImageView icon = findViewById(R.id.search_view_icon);
        BuildHelper helper = new BuildHelper();
        helper.makeSearchIcon(this, icon);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Person> people = data.findPeopleWithText(s.toString());
                ArrayList<Event> events = data.findEventsWithText(s.toString());
                SearchViewAdapter adapter = new SearchViewAdapter(people, events);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private class SearchViewAdapter extends RecyclerView.Adapter<searchViewHolder> {
        private final ArrayList<Person> people;
        private final ArrayList<Event> events;

        public SearchViewAdapter(ArrayList<Person> people, ArrayList<Event> events) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_TYPE : EVENT_TYPE;
        }

        @NonNull
        @Override
        public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == PERSON_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event, parent, false);
            }

            return new searchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull searchViewHolder holder, int position) {
            if (position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class searchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final BuildHelper helper = new BuildHelper();
        private final DataCache data = DataCache.getInstance();

        private final int viewType;
        private final ImageView iconView;
        private final TextView upperText;
        private TextView lowerText;


        private Person person;
        private Event event;

        public searchViewHolder(@NonNull View view, int viewType) {
            super(view);
            this.viewType = viewType;
            person = null;
            event = null;

            itemView.setOnClickListener(this);
            iconView = itemView.findViewById(R.id.iconView);
            if (viewType == PERSON_TYPE) {
                upperText = itemView.findViewById(R.id.nameOfPerson);
            } else {
                upperText = itemView.findViewById(R.id.nameOfEvent);
                lowerText = itemView.findViewById(R.id.userOfEvent);
            }
        }

        public void bind(Person person) {
            this.person = person;
            helper.makeGenderIcon(SearchActivity.this, iconView, person.getGender());
            upperText.setText(helper.personNameToString(person));
        }

        public void bind(Event event) {
            this.event = event;
            helper.makeLocationIcon(SearchActivity.this, iconView, event.getEventType());
            upperText.setText(helper.eventToString(event));
            lowerText.setText(helper.personNameToString(data.getPersonByID(event.getPersonID())));
        }

        @Override
        public void onClick(View v) {
            if (viewType == PERSON_TYPE) {
                helper.moveToPersonActivity(SearchActivity.this, DataCache.getPersonIdKey(), person.getPersonID());
            } else {
                helper.moveToEventActivity(SearchActivity.this, DataCache.getEventIdKey(), event.getEventID());
            }
        }
    }
}