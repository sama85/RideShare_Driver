package com.example.rideshare_driver.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideshare_driver.R;
import com.example.rideshare_driver.databinding.RideTrackingItemBinding;
import com.example.rideshare_driver.models.Ride;

import java.util.List;

public class RidesTrackingAdapter extends RecyclerView.Adapter<RidesTrackingAdapter.RideViewHolder> {
    List<Ride> rides;
    OnTrackingClickListener listener;

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RideTrackingItemBinding binding = RideTrackingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RideViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride rideItem = rides.get(position);
        holder.bind(rideItem);
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public void updateRides(List<Ride> rides) {
        this.rides = rides;
        // to redraw recycler view
        notifyDataSetChanged();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {
        RideTrackingItemBinding binding;

        public RideViewHolder(RideTrackingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ride rideItem) {
            binding.source.setText(rideItem.getSrc());
            binding.destination.setText(rideItem.getDest());
            binding.date.setText(rideItem.getDate());
            binding.time.setText(rideItem.getTime());
            binding.costValue.setText(String.valueOf(rideItem.getCost()));
            binding.statusValue.setText(rideItem.getStatus());
            binding.carNoValue.setText(rideItem.getCarNumber());
            binding.cancelBtn.setOnClickListener(view -> {
                if(listener != null)
                    listener.onItemClick(rideItem);
            });
            if(rideItem.getStatus().equals("cancelled"))
                binding.cancelBtn.setVisibility(View.GONE);
            else
                binding.cancelBtn.setVisibility(View.VISIBLE);

            if(rideItem.getStatus().equals("available"))
                binding.statusValue.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.green));
            else if(rideItem.getStatus().equals("unavailable"))
                binding.statusValue.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.red));
            else
                binding.statusValue.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.black));
        }

    }
    public interface OnTrackingClickListener{
        void onItemClick(Ride ride);
    }
    public void setOnTrackingClickListener(OnTrackingClickListener listener){
        this.listener = listener;
    }
}
