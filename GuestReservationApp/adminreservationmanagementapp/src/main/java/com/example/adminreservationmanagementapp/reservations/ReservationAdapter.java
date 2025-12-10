package com.example.adminreservationmanagementapp.reservations;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminreservationmanagementapp.databinding.ConfirmedReservationItemBinding;
import com.example.adminreservationmanagementapp.databinding.PendingReservationItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PENDING = 0, VIEW_TYPE_CONFIRMED = 1;
    private List<Reservation> reservationList = new ArrayList<>();
    private OnItemClickListener listener;

    // On Click Listeners
    public interface OnItemClickListener {
        void onOptionClick(Reservation res);
        void onAttendedClick(Reservation res);
        void onUnattendedClick(Reservation res);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        } else if (holder instanceof ConfirmedViewHolder) {
            ((ConfirmedViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return reservationList.get(position).isPending() ? VIEW_TYPE_PENDING : VIEW_TYPE_CONFIRMED;
    }

    @Override
    public int getItemCount() {
        return reservationList.size();  // List  the length of the RecycleView
    }

    public void submitList(List<Reservation> newList) {
        reservationList.clear();
        reservationList.addAll(newList);
        notifyDataSetChanged();
    }

    // ViewHolder for Pending
    class PendingViewHolder extends RecyclerView.ViewHolder {
        private final PendingReservationItemBinding binding;

        // ViewHolder Constructor
        PendingViewHolder(PendingReservationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Reservation item) {
            binding.textDate.setText(item.getDate());
            binding.textTime.setText(item.getTime());
            binding.textGuest.setText(item.getGuestCount());
            binding.textBookingNo.setText(item.getBookingNo());

            binding.imgBtnOption.setOnClickListener(view -> {
                if (listener != null)
                    listener.onOptionClick(item);
            });
        }
    }

    // ViewHolder for Confirmed
    class ConfirmedViewHolder extends RecyclerView.ViewHolder {
        private final ConfirmedReservationItemBinding binding;

        // ViewHolder Constructor
        public ConfirmedViewHolder(ConfirmedReservationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Reservation item) {
            binding.textDate.setText(item.getDate());
            binding.textTime.setText(item.getTime());
            binding.textGuest.setText(item.getGuestCount());
            binding.textBookingNo.setText(item.getBookingNo());

            binding.btnUnattended.setOnClickListener(view -> {
                if (listener != null) listener.onUnattendedClick(item);
            });

            binding.btnAttended.setOnClickListener(view -> {
                if (listener != null) listener.onAttendedClick(item);
            });
        }
    }
}
