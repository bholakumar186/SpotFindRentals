package com.example.spotfindrentals;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.spotfindrentals.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng selectedLocation;
    private Button btnConfirmLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        btnConfirmLocation = findViewById(R.id.btnConfirmLocation);

        btnConfirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedLocation != null) {
                    returnSelectedLocation(selectedLocation);
                } else {
                    // Handle case where no location was selected
                    Toast.makeText(MapsActivity.this, "Please select a location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(latLng -> {
            // Remove previous markers
            mMap.clear();

            // Add a marker at the selected location
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

            // Save the selected location
            selectedLocation = latLng;
        });

    }
    public void onBackPressed() {
        super.onBackPressed();
        if (selectedLocation != null) {
            returnSelectedLocation(selectedLocation);
        } else {
            super.onBackPressed();  // Call the super method to handle the default back press behavior
        }

    }
    private void returnSelectedLocation(LatLng selectedLocation) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("selected_latitude", selectedLocation.latitude);
        returnIntent.putExtra("selected_longitude", selectedLocation.longitude);
        setResult(RESULT_OK, returnIntent);
        finish();  // Close the MapActivity and return to ProfileActivity
    }
}