package com.example.rideshare_driver.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideshare_driver.databinding.RequestItemBinding;
import com.example.rideshare_driver.models.Ride;

import java.util.List;

public class RequestsListAdapter extends  RecyclerView.Adapter<RequestsListAdapter.RideViewHolder>{
    List<Ride> rides;
    List<String> riders;
    OnItemClickListener listener;

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestItemBinding binding = RequestItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RideViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride rideItem = rides.get(position);
        String rider = riders.get(position);
        holder.bind(rideItem, rider);
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public void updateRides(List<Ride> rides, List<String> riders) {
        this.rides = rides;
        this.riders = riders;
        // to redraw recycler view
        notifyDataSetChanged();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {
        RequestItemBinding binding;

        public RideViewHolder(RequestItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ride rideItem, String rider) {
            binding.riderName.setText(rider);
            binding.source.setText(rideItem.getSrc());
            binding.destination.setText(rideItem.getDest());
            binding.date.setText(rideItem.getDate());
            binding.time.setText(rideItem.getTime());
            binding.costValue.setText(String.valueOf(rideItem.getCost()));
//            binding.bookBtn.setOnClickListener(view -> {
//                if(listener != null)
//                    listener.onItemClick(rideItem);
//            });
        }

    }
    public interface OnItemClickListener{
        void onItemClick(Ride ride);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
