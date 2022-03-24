package com.example.familymapclient.fragment;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familymapclient.R;
import com.example.familymapclient.data.DataCache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import Model.Event;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback  {
    private GoogleMap map;
    private DataCache data;
    private MapViewModel mViewModel;

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
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;// Add a marker in Sydney and move the camera

        map.setOnMapLoadedCallback(this);

        LatLng sydney= new LatLng(-34,  151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));

        setEventsMarkersForFirstLanding();
    }

    @Override
    public void onMapLoaded() {

    }

    private void setEventsMarkersForFirstLanding(){
        for (Event event: data.getUserEvents()){
            LatLng eventPoint = new LatLng(event.getLatitude(), event.getLongitude());
            map.addMarker(new MarkerOptions().position(eventPoint).icon(BitmapDescriptorFactory.defaultMarker
                    (BitmapDescriptorFactory.HUE_CYAN)));
            //I need to change it to BitmapDescriptorFactory.
            if (data.getEventTypeColor().get(event.getEventType())== null){
                data.getEventTypeColor().put(event.getEventType(), data.getColorNum());
                if (data.getColorNum()+1 == DataCache.getMaxColorNum()){
                    data.setColorNum(DataCache.getMinColorNum());
                }
                data.setColorNum(data.getColorNum()+1);
            }
        }
    }
}