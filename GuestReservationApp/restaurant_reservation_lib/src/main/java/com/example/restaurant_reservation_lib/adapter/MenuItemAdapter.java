package com.example.restaurant_reservation_lib.adapter;

import android.view.LayoutInflater;
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
    private OnItemClickListener listener;  // only for staff-side

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
    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem currentMenuItem = menuItems.get(position);

        // Bind the data to the ViewHolder obj for each item in the RecycleView (List data -> item layout)
        holder.bind(currentMenuItem);

        if (isStaffSide) {
            // Show option icon button
            holder.binding.imgBtnOption.setVisibility(View.VISIBLE);
        } else {
            // Option icon button disappears
            holder.binding.imgBtnOption.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private MenuItemBinding binding;

        public MenuItemViewHolder(MenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.imgBtnOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Pass position to the item of recycle view inside on click listener
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        
                    }
                }
            });
        }

        void bind(MenuItem menuItem) {
//            binding.imgItemPhoto.setImageBitmap(menuItem.getImage());
            binding.textFoodName.setText(menuItem.getFoodName());
            binding.textPrice.setText(String.valueOf(menuItem.getPrice()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
