package com.example.restaurant_reservation_lib.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_reservation_lib.databinding.ReservationBinding;
import com.example.restaurant_reservation_lib.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private List<Reservation> reservations = new ArrayList<>();
    private OnItemActionListener listener;

    // Track currently open item
    private ReservationViewHolder currentOpenHolder = null;
    private static final float ACTION_REVEAL_WIDTH_DP = 130f;  // 75dp width x 2 buttons

    // Creation - Creates a new ViewHolder obj for each item in the RecycleView
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder obj
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // LayoutInflater.from({loading the corresponding resource id of layout}, {parent layout}, {need to cover a root layout?})
        ReservationBinding binding = ReservationBinding.inflate(
                inflater, parent, false);

        return new ReservationViewHolder(binding);
    }

    // Updating
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation currentReservation = reservations.get(position);
        // Bind the data to the ViewHolder obj for each item in the RecycleView (List data -> item layout)
        holder.bind(currentReservation);

        holder.resetActions();

        // Click item option button
        holder.binding.imgBtnOption.setOnClickListener(viewOption -> {
            if (currentOpenHolder == holder) {
                // Close currently open item by clicking its option button
                holder.resetActions();
                currentOpenHolder = null;
            } else {
                // Close any other open items
                if (currentOpenHolder != null)
                    currentOpenHolder.resetActions();
            }
            // Open item
            holder.revealActions();
            currentOpenHolder = holder;
        });

        // Edit button
        holder.binding.cardBtnEdit.setOnClickListener(viewEdit -> {
            listener.onEdit(currentReservation);
            holder.resetActions();
            currentOpenHolder = null;
        });

        // Cancel button
        holder.binding.cardBtnCancel.setOnClickListener(viewDelete -> {
            listener.onCancel(currentReservation);
            holder.resetActions();
            currentOpenHolder = null;
        });

        // Close open actions when tapping outside any item
        holder.binding.getRoot().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                closeCurrentOpenItem();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    // Set values to the adapter
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    // Close any other open item when another is opened currently
    public void closeCurrentOpenItem() {
        if (currentOpenHolder != null) {
            currentOpenHolder.resetActions();
            currentOpenHolder = null;
        }
    }

    // View Holder
    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final ReservationBinding binding;

        public ReservationViewHolder(ReservationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bind(Reservation reservation) {
            binding.textTime.setText(reservation.getTime());
            binding.textNumOfGuest.setText(reservation.getPartySize() + " guest(s)");
            binding.textDate.setText(reservation.getDate());
        }

        // Reveal hidden buttons
        void revealActions() {
            float revealWidth = -dpToPx(ACTION_REVEAL_WIDTH_DP);
            binding.ticketItem.animate()
                    .translationX(revealWidth)
                    .setDuration(200)
                    .start();
        }

        // Close hidden buttons
        void resetActions() {
            binding.ticketItem.animate()
                    .translationX(0f)
                    .setDuration(200)
                    .start();
        }

        private float dpToPx(float dp) {
            return dp * itemView.getContext().getResources().getDisplayMetrics().density;
        }
    }

    // Item action listener
    public interface OnItemActionListener {
        void onEdit(Reservation reservation);
        void onCancel(Reservation reservation);
    }

    // Call from activity
    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }
}
