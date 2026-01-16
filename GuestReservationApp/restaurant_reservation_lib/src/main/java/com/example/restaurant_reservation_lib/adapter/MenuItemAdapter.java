package com.example.restaurant_reservation_lib.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_reservation_lib.databinding.MenuItemBinding;
import com.example.restaurant_reservation_lib.entity.MenuItem;

import java.util.ArrayList;
import java.util.List;
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder>{
    private final boolean isStaffSide;
    private List<MenuItem> menuItems = new ArrayList<>();
    private OnItemActionListener listener;  // only for staff-side

    // Track currently open item
    private MenuItemViewHolder currentOpenHolder = null;
    private static final float ACTION_REVEAL_WIDTH_DP = 130f;  // 65dp width x 2 buttons

    public MenuItemAdapter(boolean isStaffSide) {
        this.isStaffSide = isStaffSide;
    }

    // Creation - Creates a new ViewHolder obj for each item in the RecycleView
    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder obj
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // LayoutInflater.from({loading the corresponding resource id of layout}, {parent layout}, {need to cover a root layout?})
        MenuItemBinding binding = MenuItemBinding.inflate(
                inflater, parent, false);

        return new MenuItemViewHolder(binding);
    }

    // Updating
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem currentMenuItem = menuItems.get(position);
        // Bind the data to the ViewHolder obj for each item in the RecycleView (List data -> item layout)
        holder.bind(currentMenuItem);

        holder.resetActions();

        if (isStaffSide) {
            // Show option icon button
            holder.binding.imgBtnOption.setVisibility(View.VISIBLE);

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
                listener.onEdit(currentMenuItem);
                holder.resetActions();
                currentOpenHolder = null;
            });

            // Delete button
            holder.binding.cardBtnDelete.setOnClickListener(viewDelete -> {
                listener.onDelete(currentMenuItem);
                holder.resetActions();
                currentOpenHolder = null;
            });
        } else {
            // All buttons disappears
            holder.binding.imgBtnOption.setVisibility(View.GONE);
            holder.binding.cardBtnEdit.setVisibility(View.GONE);
            holder.binding.cardBtnDelete.setVisibility(View.GONE);
        }

        holder.binding.getRoot().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                closeCurrentOpenItem();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    // Close any other open item when another is opened currently
    public void closeCurrentOpenItem() {
        if (currentOpenHolder != null) {
            currentOpenHolder.resetActions();
            currentOpenHolder = null;
        }
    }

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private final MenuItemBinding binding;

        public MenuItemViewHolder(MenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Bind data
        void bind(MenuItem menuItem) {
//            binding.imgItemPhoto.setImageBitmap(menuItem.getImage());
            binding.textFoodName.setText(menuItem.getFoodName());
            binding.textPrice.setText(String.format("$%.2f", menuItem.getPrice()));
        }

        // Reveal hidden buttons
        void revealActions() {
            float revealWidth = -dpToPx(ACTION_REVEAL_WIDTH_DP);
            binding.menuItem.animate()
                    .translationX(revealWidth)
                    .setDuration(200)
                    .start();
        }

        // Close hidden buttons
        void resetActions() {
            binding.menuItem.animate()
                    .translationX(0f)
                    .setDuration(200)
                    .start();
        }

        private float dpToPx(float dp) {
            return dp * itemView.getContext().getResources().getDisplayMetrics().density;
        }
    }

    public interface OnItemActionListener {
        void onEdit(MenuItem menuItem);
        void onDelete(MenuItem menuItem);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

}