package com.example.adminreservationmanagementapp.reservations;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminreservationmanagementapp.databinding.ConfirmedReservationItemBinding;
import com.example.adminreservationmanagementapp.databinding.PendingReservationItemBinding;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PENDING = 0, VIEW_TYPE_CONFIRMED = 1;
    private List<Reservation> reservationList;
    private OnItemClickListener listener;

    // Constructor
    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    // On Click Listeners
    public interface OnItemClickListener {
        void onOptionClick(Reservation res);
        void onAttendedClick(Reservation res);
        void onUnattendedClick(Reservation res);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return reservationList.get(position).getViewType();
    }

    // Creation - Create a new ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // viewType: item's type
        // Inflate the layout file which created for RecycleView
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_PENDING) {
            PendingReservationItemBinding binding = PendingReservationItemBinding.inflate(inflater, parent, false);
            return new PendingViewHolder(binding);
        } else if (viewType == VIEW_TYPE_CONFIRMED) {
            ConfirmedReservationItemBinding binding = ConfirmedReservationItemBinding.inflate(inflater, parent, false);
            return new ConfirmedViewHolder(binding);
        }
        return null;
    }

    // Updating
    // Get data from the list and bind it to the specific RecycleView item (List data -> item layout)
    // Set different data and methods related to clicks on particular items of the RecycleView
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Reservation item = reservationList.get(position);
        if (holder instanceof PendingViewHolder) {
            ((PendingViewHolder) holder).bind(item);
        } else {
            ((ConfirmedViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return reservationList.size();  // List  the length of the RecycleView
    }

    // ViewHolder for Pending
    class PendingViewHolder extends RecyclerView.ViewHolder {
        private PendingReservationItemBinding binding;

        // ViewHolder Constructor
        PendingViewHolder(PendingReservationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Reservation reservation) {
            binding.textDate.setText(reservation.getDate());
            binding.textTime.setText(reservation.getTime());
            binding.textGuest.setText(reservation.getGuestCount());
            binding.textBookingNo.setText(reservation.getBookingNo());
        }
    }

    // ViewHolder for Confirmed
    class ConfirmedViewHolder extends RecyclerView.ViewHolder {
        private ConfirmedReservationItemBinding binding;

        // ViewHolder Constructor
        public ConfirmedViewHolder(ConfirmedReservationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Reservation reservation) {
            binding.textDate.setText(reservation.getDate());
            binding.textTime.setText(reservation.getTime());
            binding.textGuest.setText(reservation.getGuestCount());
            binding.textBookingNo.setText(reservation.getBookingNo());
        }
    }
}
