package com.example.spotfindrentals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParkingSpaceAdapter extends RecyclerView.Adapter<ParkingSpaceAdapter.ParkingSpaceViewHolder> {

    private List<ParkingSpaceDatabase> parkingSpaces;

    public ParkingSpaceAdapter(List<ParkingSpaceDatabase> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    @NonNull
    @Override
    public ParkingSpaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parking_space, parent, false);
        return new ParkingSpaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSpaceViewHolder holder, int position) {
        ParkingSpaceDatabase space = parkingSpaces.get(position);

        holder.tvOwnerName.setText(space.getOwnerName() != null ? space.getOwnerName() : "N/A");
        holder.tvPhoneNumber.setText(space.getPhoneNumber() != null ? space.getPhoneNumber() : "N/A");
        holder.tvAlternateNumber.setText(space.getAlternateNumber() != null ? space.getAlternateNumber() : "N/A");
        holder.tvHouseNumber.setText(space.getHouseNumber() != null ? space.getHouseNumber() : "N/A");
        holder.tvStreet.setText(space.getStreet() != null ? space.getStreet() : "N/A");
        holder.tvLocality.setText(space.getLocality() != null ? space.getLocality() : "N/A");
        holder.tvCity.setText(space.getCity() != null ? space.getCity() : "N/A");
        holder.tvState.setText(space.getState() != null ? space.getState() : "N/A");
        holder.tvPin.setText(space.getPin() != null ? space.getPin() : "N/A");
        holder.tvLandmark.setText(space.getLandmark() != null ? space.getLandmark() : "N/A");
        holder.tvParkingSize.setText(space.getParkingSize() != null ? space.getParkingSize() : "N/A");
        holder.tvAvailabilityTime.setText(space.isisAvailable() ? "Available" : "Not Available");
        holder.tvCameraAvailability.setText(space.isCameraAvailability() ? "Camera Available" : "No Camera");
        holder.tvGuardAvailability.setText(space.isGuardAvailability() ? "Guard Available" : "No Guard");
        holder.tvLocation.setText(space.getLocation() != null ? space.getLocation() : "N/A");
    }

    @Override
    public int getItemCount() {
        return parkingSpaces.size();
    }

    public static class ParkingSpaceViewHolder extends RecyclerView.ViewHolder {

        TextView tvOwnerName;
        TextView tvPhoneNumber;
        TextView tvAlternateNumber;
        TextView tvHouseNumber;
        TextView tvStreet;
        TextView tvLocality;
        TextView tvCity;
        TextView tvState;
        TextView tvPin;
        TextView tvLandmark;
        TextView tvParkingSize;
        TextView tvAvailabilityTime;
        TextView tvCameraAvailability;
        TextView tvGuardAvailability;
        TextView tvLocation;

        public ParkingSpaceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOwnerName = itemView.findViewById(R.id.tvOwnerName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvAlternateNumber = itemView.findViewById(R.id.tvAlternateNumber);
            tvHouseNumber = itemView.findViewById(R.id.tvHouseNumber);
            tvStreet = itemView.findViewById(R.id.tvStreet);
            tvLocality = itemView.findViewById(R.id.tvLocality);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvState = itemView.findViewById(R.id.tvState);
            tvPin = itemView.findViewById(R.id.tvPin);
            tvLandmark = itemView.findViewById(R.id.tvLandmark);
            tvParkingSize = itemView.findViewById(R.id.tvParkingSize);
            tvAvailabilityTime = itemView.findViewById(R.id.tvAvailabilityTime);
            tvCameraAvailability = itemView.findViewById(R.id.tvCameraAvailability);
            tvGuardAvailability = itemView.findViewById(R.id.tvGuardAvailability);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}
