package com.example.adminreservationmanagementapp.reservations;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminreservationmanagementapp.R;
import com.example.adminreservationmanagementapp.databinding.ConfirmedReservationItemBinding;
import com.example.adminreservationmanagementapp.databinding.PendingReservationItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ConfirmedViewHolder> {
    private List<Reservation> reservationList;

    // Constructor
    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    // Creation - Creates a new ViewHolder obj for each item in the RecycleView
    @NonNull
    @Override
    public ConfirmedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // viewType: item's type
        // Inflate the layout for each item and return a new ViewHolder obj
        ConfirmedReservationItemBinding binding = ConfirmedReservationItemBinding.inflate(
                // LayoutInflater.from({loading the corresponding resource id of layout}, {parent layout}, {need to cover a root layout?})
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ConfirmedViewHolder(binding);
    }

    // Updating
    @Override
    public void onBindViewHolder(@NonNull ConfirmedViewHolder holder, int position) {
        Reservation item = reservationList.get(position);
        holder.bind(item);  // Bind the data to the ViewHolder obj for each item in the RecycleView (List data -> item layout)
    }

    // Returns the total number of items in the data set
    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    // ViewHolder for Confirmed
    public static class ConfirmedViewHolder extends RecyclerView.ViewHolder {
        private ConfirmedReservationItemBinding binding;

        // ViewHolder Constructor
        public ConfirmedViewHolder(ConfirmedReservationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Reservation item) {
            binding.textDate.setText(item.getDate());
            binding.textTime.setText(item.getTime());
            binding.textGuest.setText(String.valueOf(item.getGuestCount()));
            binding.textBookingNo.setText(item.getBookingNo());
        }
    }
}
