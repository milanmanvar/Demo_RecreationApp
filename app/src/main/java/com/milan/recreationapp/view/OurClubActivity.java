package com.milan.recreationapp.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.adapter.ClubLocationListAdapter;
import com.milan.recreationapp.model.ClubModel_New;

import java.util.ArrayList;

/**
 * Created by utsav.k on 04-04-2016.
 */
public class OurClubActivity extends BaseActivity {
    // Google Map
    private GoogleMap googleMap;
    private ArrayList<ClubModel_New> clubModels;
    private SharedPreferences appPreferences;
    private ListView listLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ourclub);
        appPreferences = ((ReCreationApplication) getApplication()).sharedPreferences;
//        clubModels = Utils.parseClubListXML(appPreferences.getString("clublist", ""));
        setUpActionBar("Our Clubs");
        findViewById(R.id.actionbar_layout_iv_myclass).setVisibility(View.GONE);
        clubModels = (((ReCreationApplication) getApplication()).getDatabase().getClubList());
        listLocations = (ListView) findViewById(R.id.listLocation);
        listLocations.setAdapter(new ClubLocationListAdapter(this, clubModels));
        try {
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    // create marker
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    for (ClubModel_New c : clubModels) {
                        MarkerOptions marker = new MarkerOptions().position(new LatLng(c.getLat(), c.getLng())).title(c.getName());
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                        builder.include(marker.getPosition());
                        googleMap.addMarker(marker);
                    }
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                    googleMap.animateCamera(cu);
                }
            });

        }
    }

    public void changeLocation(double lat, double lng, String name) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void mapZoomOut(){

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f));
    }


}
