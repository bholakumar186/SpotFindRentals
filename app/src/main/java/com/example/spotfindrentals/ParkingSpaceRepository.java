package com.example.spotfindrentals;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParkingSpaceRepository {
    private DatabaseReference mDatabase;

    public ParkingSpaceRepository() {
        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("ParkingSpaces");
    }

    public void saveParkingSpace(ParkingSpaceDatabase parkingSpace, final RepositoryCallback callback) {
        mDatabase.push().setValue(parkingSpace).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess("Parking space submitted successfully");
                } else {
                    callback.onFailure("Failed to submit parking space");
                }
            }
        });
    }

    // Retrieve nearby parking spaces
    public void getNearbyParkingSpaces(final LatLng userLocation, final double radiusInMeters, NearbyParkingCallback callback) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ParkingSpaceDatabase> nearbyParkingSpaces = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ParkingSpaceDatabase space = snapshot.getValue(ParkingSpaceDatabase.class);
                    if (space != null) {
                        // Extract latitude and longitude from the location field
                        String locationString = space.getLocation();
                        // Remove the prefix if it exists
                        if (locationString.startsWith("Selected Location: ")) {
                            locationString = locationString.substring("Selected Location: ".length()).trim();
                        }

                        String[] latLngStrings = locationString.split(",");
                        if (latLngStrings.length == 2) {
                            try {
                                double latitude = Double.parseDouble(latLngStrings[0].trim());
                                double longitude = Double.parseDouble(latLngStrings[1].trim());
                                LatLng parkingLocation = new LatLng(latitude, longitude);

                                float[] results = new float[1];
                                Location.distanceBetween(
                                        userLocation.latitude, userLocation.longitude,
                                        parkingLocation.latitude, parkingLocation.longitude,
                                        results
                                );

                                float distanceInMeters = results[0];
                                Log.d("DistanceDebug", "Parking Space: " + space.getLocation() + ", Distance: " + distanceInMeters);

                                // Check if the distance is within the specified radius
                                if (distanceInMeters <= radiusInMeters) {
                                    nearbyParkingSpaces.add(space);
                                }
                            } catch (NumberFormatException e) {
                                Log.e("ParsingError", "Error parsing location string: " + locationString, e);
                            }
                        } else {
                            Log.e("ParsingError", "Invalid location format: " + locationString);
                        }
                    }
                }

                if (!nearbyParkingSpaces.isEmpty()) {
                    callback.onNearbyParkingFound(nearbyParkingSpaces);
                } else {
                    callback.onNoNearbyParkingFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to read value.", error.toException());
                callback.onError(error.toException());
            }
        });
    }

    // Callback interface for nearby parking spaces
    public interface NearbyParkingCallback {
        void onNearbyParkingFound(List<ParkingSpaceDatabase> nearbyParkingSpaces);
        void onNoNearbyParkingFound();
        void onError(Exception e);
    }

    // Callback interface to handle success or failure in the database operations
    public interface RepositoryCallback {
        void onSuccess(String message);
        void onFailure(String message);
    }
}
