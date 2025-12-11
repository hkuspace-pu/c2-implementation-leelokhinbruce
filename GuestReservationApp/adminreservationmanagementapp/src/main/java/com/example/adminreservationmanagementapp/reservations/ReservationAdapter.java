package com.example.adminreservationmanagementapp.reservations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminreservationmanagementapp.databinding.ConfirmedReservationItemBinding;
import com.example.adminreservationmanagementapp.databinding.PendingReservationItemBinding;
import com.example.restaurant_reservation_lib.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_PENDING = 0, TYPE_CONFIRMED = 1;
    private List<Reservation> reservationList;

    // Constructor
    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public int getItemViewType(int position) {
        return reservationList.get(position).getStatus().equals("Pending") ? TYPE_PENDING : TYPE_CONFIRMED;
    }

    // Creation - Creates a new ViewHolder obj for each item in the RecycleView
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // viewType: item's type
        // Inflate the layout for each item and return a new ViewHolder obj
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // LayoutInflater.from({loading the corresponding resource id of layout}, {parent layout}, {need to cover a root layout?})
        if (viewType == TYPE_PENDING) {
            PendingReservationItemBinding binding = PendingReservationItemBinding.inflate(
                    inflater, parent, false);
            return new PendingViewHolder(binding);
        } else {
            ConfirmedReservationItemBinding binding = ConfirmedReservationItemBinding.inflate(
                    inflater, parent, false);
            return new ConfirmedViewHolder(binding);
        }
    }

    // Updating
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Reservation reservationItem = reservationList.get(position);

        // Bind the data to the ViewHolder obj for each item in the RecycleView (List data -> item layout)
        if (holder instanceof PendingViewHolder) {
            ((PendingViewHolder) holder).bind(reservationItem);
        } else if (holder instanceof ConfirmedViewHolder) {
            ((ConfirmedViewHolder) holder).bind(reservationItem);
        }
    }

    // Returns the total number of items in the data set
    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    // ViewHolder for Pending
    public static class PendingViewHolder extends RecyclerView.ViewHolder {
        private PendingReservationItemBinding binding;

        // ViewHolder Constructor
        public PendingViewHolder(PendingReservationItemBinding binding) {
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
