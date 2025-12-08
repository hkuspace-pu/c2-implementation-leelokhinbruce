package com.example.adminreservationmanagementapp.reservations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminreservationmanagementapp.databinding.PendingReservationItemBinding;

import java.util.List;

public class PendingReservationAdapter extends RecyclerView.Adapter<PendingReservationAdapter.ReservationViewHolder> {
    private List<Reservation> pendingReservationList;
    private OnItemClickListener listener;

    // Constructor
    public PendingReservationAdapter(List<Reservation> pendingReservationList) {
        this.pendingReservationList = pendingReservationList;
    }

    public interface OnItemClickListener {
        void onOptionClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Creation - Create a new ViewHolder
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // viewType: item's type
        // Inflate the layout file which created for RecycleView
        PendingReservationItemBinding binding = PendingReservationItemBinding.inflate(
                // parent: Parent layout of RecyclerView
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ReservationViewHolder(binding);
    }

    // Updating
    // Get data from the list and bind it to the specific RecycleView item (List data -> item layout)
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation item = pendingReservationList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return pendingReservationList.size();  // List display size
    }

    class ReservationViewHolder extends RecyclerView.ViewHolder {
        private PendingReservationItemBinding binding;

        // ViewHolder Constructor
        ReservationViewHolder(PendingReservationItemBinding binding) {
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
