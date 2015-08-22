package com.example.mauricioarce.bankmap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private static final String PARSE_CLASS = "Bank";
    private final List<Agency> bnbAgencies = new ArrayList<>();
    private final List<Agency> unionAgencies = new ArrayList<>();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Agency> actualBank;
    private TextView textBank;
    private TextView textAgency;
    private int indexAgency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        textBank = (TextView) findViewById(R.id.text_bank_name);
        textAgency = (TextView) findViewById(R.id.text_location);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        setAgencies();
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void nextBank(View view) {
        if (mMap != null) {
            mMap.clear();
            indexAgency = 0;
            if (actualBank.equals(bnbAgencies)) {
                actualBank = unionAgencies;
                textBank.setText("UNION");
                animateToAgency(indexAgency);
            } else {
                actualBank = bnbAgencies;
                textBank.setText("BNB");
                animateToAgency(indexAgency);
            }
            setUpActualBank(actualBank);
        }
    }

    public void nextAgency(View view) {
        if (mMap != null) {
            if (indexAgency < actualBank.size() - 1) {
                indexAgency++;
            } else {
                indexAgency = 0;
            }
            animateToAgency(indexAgency);
        }
    }

    public void prevAgency(View view) {
        if (mMap != null) {
            if (indexAgency > 0) {
                indexAgency--;
            } else {
                indexAgency = actualBank.size() - 1;
            }
            animateToAgency(indexAgency);
        }

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        setUpActualBank(bnbAgencies);
    }

    private void setUpActualBank(List<Agency> bank) {
        for (Agency agency : bank) {
            mMap.addMarker(new MarkerOptions().position(agency.getLocation()).title(agency.getBankName()));
        }
        animateToAgency(indexAgency);
    }

    private void setAgencies() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);
        List<ParseObject> objects;
        try {
            objects = query.find();
            setAgencies(objects);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setAgencies(List<ParseObject> agencies) {
        Agency currentAgency;
        LatLng currentLocation;
        for (ParseObject agency : agencies) {
            currentLocation = new LatLng(agency.getParseGeoPoint("location").getLatitude(),
                                         agency.getParseGeoPoint("location").getLongitude());
            currentAgency = new Agency(agency.getString("Agency"), agency.getString("name"), currentLocation);
            if (currentAgency.getBankName().equals("BNB")) {
                bnbAgencies.add(currentAgency);
            } else {
                unionAgencies.add(currentAgency);
            }
        }
        actualBank = bnbAgencies;
        textBank.setText(actualBank.get(indexAgency).getBankName());
        textAgency.setText(actualBank.get(indexAgency).getName());
    }

    private void animateToAgency(int index) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actualBank.get(index).getLocation(), 17f));
        textAgency.setText(actualBank.get(index).getName());
    }
}
