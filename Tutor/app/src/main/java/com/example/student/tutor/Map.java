package com.example.student.tutor;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Student on 6/11/17.
 */

public class Map  extends FragmentActivity implements OnMapReadyCallback {
    public List<String> locationList;
    private LatLng p1 = null;
    private Address location = null;
    List<LatLng> latLngs = new ArrayList<LatLng>();
    private PolylineOptions mPolyLineOptions;

    public Map(){}

    public Map(List<String> locationList) {
        this.locationList = locationList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        String s = getIntent().getStringExtra("locationList");

        locationList = new ArrayList<String>(Arrays.asList(s.split(",")));


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Geocoder geoCoder = new Geocoder(getApplicationContext());
    }

    @Override
    public void onMapReady(GoogleMap map) {

        double longitude;
        double latitude;

        Geocoder geoCoder = new Geocoder(getApplicationContext());

        if (locationList.size() != 0) {
            for (int i = 0; i < locationList.size(); i++) {
                Marker marker;
                try {
                    System.out.println("P1 : " + locationList.get(i));

                    double[] lonlatOut = parse(locationList.get(i));

                    p1 = new LatLng(lonlatOut[0], lonlatOut[1]);
                    latLngs.add(p1);
                    marker = map.addMarker(new MarkerOptions()
                            .position(p1));

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));

                } catch (Exception ee) {
                    Context context = getApplicationContext();
                    CharSequence text = "Couldn't locate";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                mPolyLineOptions = new PolylineOptions();
                Iterable<LatLng> latLngsList = latLngs;
                mPolyLineOptions.addAll(latLngsList);
                map.addPolyline(mPolyLineOptions);

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(latLngs.size() -1),15));


            }
        }
    }


    public double[] parse(String input) {
        double [] output = new double[2];

        String[] parts= input.split("\\s+");
        String[] latSplit = parts[1].split(":");
        output[0] = Double.parseDouble(latSplit[1]);
        String[] longSplit = parts[2].split(":");
        output[1] = Double.parseDouble(longSplit[1]);

        return output;
    }
}
