package com.example.familymapclient.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
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
import com.joanzapata.iconify.widget.IconButton;

import Model.Event;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback  {
    private GoogleMap map;
    private DataCache data;
    private MapViewModel mViewModel;
    private GoogleMap.OnMarkerClickListener listener;
    private TextView textView;
    private IconButton icon;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        data = DataCache.getInstance();
        listener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Event event = (Event) marker.getTag();
                //TODO: Display it in the Map Fragment of the Event Info


                //TODO: Draw Lines among 1. Parents 2. Life Cycle 3. Spouse
                //TODO: Life Cycle: get person's events in order and draw lines
                //TODO: Draw birth events of fathers and mothers
                //TODO: Draw a line between the event and the spouse' birth event.

                //TODO: It will be changed according to the Settings events
                //TODO: Create a setting activity.
                //TODO: Filters by gender and filters by father or mother side

                //First Thing is to check the setting then do it.
                return false;
            }
        };
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

        LatLng sydney= new LatLng(-34,  151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    @Override
    public void onMapLoaded() {

    }

    private void setMapIcon(IconButton icon){

    }

    private void drawLine

    private void setEventsMarkersForFirstLanding(){
        for (Event event: data.getUserEvents()){
            LatLng eventPoint = new LatLng(event.getLatitude(), event.getLongitude());
            float color;

            if (data.getEventTypeColor().get(event.getEventType())== null){
                color = data.getColors()[data.getColorNum()];
                data.getEventTypeColor().put(event.getEventType(), color);

                if (data.getColorNum()+1 == DataCache.getMaxColorNum()){
                    data.setColorNum(DataCache.getMinColorNum()); //set to minimum number.
                }
                data.setColorNum(data.getColorNum()+1);
            } // Set until 0-9
            else { //after setting everything
                color = data.getEventTypeColor().get(event.getEventType());
            }

            Marker marker = map.addMarker(new MarkerOptions().position(eventPoint).icon(BitmapDescriptorFactory.defaultMarker
                    (color)));

            marker.setTag(event);

            listener.onMarkerClick(marker);
            //add onClickEventListener >> Event && Life Journey && get Parents >> connect them.
        }

        map.setOnMarkerClickListener(listener);
    }

    public GoogleMap.OnMarkerClickListener getListener() {
        return listener;
    }

}