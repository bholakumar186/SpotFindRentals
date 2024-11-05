package com.example.spotfindrentals;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RentParkingActivity extends AppCompatActivity {

    private TextInputEditText tietOwnerName, tietPhoneNumber, tietAlternateNumber, tietHouseNumber, tietStreet,
            tietLocality, tietCity, tietState, tietPin, tietLandmark, tietParkingSize ;
    private CheckBox cbCameraAvailability, cbGuardAvailability, tietAvailabilityTime;
    private TextView tvLocation;
    private Button btnSubmit, btnLocation;

    private double selectedLatitude, selectedLongitude;

    private ParkingSpaceRepository parkingSpaceRepository;
    private DatabaseReference mDatabase;

    private static final int REQUEST_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rent_parking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("ParkingSpaces");

        // Initialize UI elements
        tietOwnerName = findViewById(R.id.tietOwnerName);
        tietPhoneNumber = findViewById(R.id.tietPhoneNumber);
        tietAlternateNumber = findViewById(R.id.tietAlternateNumber);
        tietHouseNumber = findViewById(R.id.tietHouseNumber);
        tietStreet = findViewById(R.id.tietStreet);
        tietLocality = findViewById(R.id.tietLocality);
        tietCity = findViewById(R.id.tietCity);
        tietState = findViewById(R.id.tietState);
        tietPin = findViewById(R.id.tietPin);
        tietLandmark = findViewById(R.id.tietLandmark);
        tietParkingSize = findViewById(R.id.tietParkingSize);
        tietAvailabilityTime = findViewById(R.id.tietAvailabilityTime);
        cbCameraAvailability = findViewById(R.id.cbCameraAvailability);
        cbGuardAvailability = findViewById(R.id.cbGuardAvailability);
        tvLocation = findViewById(R.id.tvLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnLocation = findViewById(R.id.btnLocation);

        parkingSpaceRepository = new ParkingSpaceRepository();

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RentParkingActivity.this, MapsActivity.class); // Example MapActivity
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitParkingSpace();
            }
        });
    }
    private void submitParkingSpace() {
        // Retrieve inputs
        String ownerName = tietOwnerName.getText().toString().trim();
        String phoneNumber = tietPhoneNumber.getText().toString().trim();
        String alternateNumber = tietAlternateNumber.getText().toString().trim();
        String houseNumber = tietHouseNumber.getText().toString().trim();
        String street = tietStreet.getText().toString().trim();
        String locality = tietLocality.getText().toString().trim();
        String city = tietCity.getText().toString().trim();
        String state = tietState.getText().toString().trim();
        String pin = tietPin.getText().toString().trim();
        String landmark = tietLandmark.getText().toString().trim();
        String parkingSize = tietParkingSize.getText().toString().trim();
        boolean availabilityTime = tietAvailabilityTime.isChecked();
        boolean cameraAvailability = cbCameraAvailability.isChecked();
        boolean guardAvailability = cbGuardAvailability.isChecked();
        String location = tvLocation.getText().toString().trim();

        if (TextUtils.isEmpty(ownerName) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(houseNumber)
                || TextUtils.isEmpty(street) || TextUtils.isEmpty(locality) || TextUtils.isEmpty(city)
                || TextUtils.isEmpty(state) || TextUtils.isEmpty(pin) || TextUtils.isEmpty(parkingSize)
                ||  TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        ParkingSpaceDatabase parkingSpace = new ParkingSpaceDatabase(ownerName, phoneNumber, alternateNumber, houseNumber,
                street, locality, city, state, pin, landmark, parkingSize, availabilityTime, cameraAvailability,
                guardAvailability, location);

        // Use ParkingSpaceRepository to save data to Firebase
        parkingSpaceRepository.saveParkingSpace(parkingSpace, new ParkingSpaceRepository.RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(RentParkingActivity.this, message, Toast.LENGTH_SHORT).show();
                String bookingMessage = "Your booking was successful!";
                showLocalNotification(bookingMessage);
                finish(); // Close the activity
            }

            public void onFailure(String message) {
                Toast.makeText(RentParkingActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK && data!= null) {
            // Retrieve location data from the map activity
            selectedLatitude = data.getDoubleExtra("selected_latitude", 0);
            selectedLongitude = data.getDoubleExtra("selected_longitude", 0);// Example, ensure "location" is set correctly in MapActivity
            if (selectedLatitude != 0 && selectedLongitude != 0) {
                tvLocation.setText("Selected Location: " + selectedLatitude + ", " + selectedLongitude);
            } else {
                Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showLocalNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "default_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Booking Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new Notification.Builder(this, channelId)
                .setContentTitle("Booking Confirmation")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(0, notification);
    }

}