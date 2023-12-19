package com.example.rideshare_driver.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideshare_driver.databinding.RequestItemBinding;
import com.example.rideshare_driver.models.Order;
import com.example.rideshare_driver.models.Ride;

import java.util.List;

public class RequestsListAdapter extends  RecyclerView.Adapter<RequestsListAdapter.RideViewHolder>{
    List<Ride> rides;
    List<Order> orders;
    OnRequestClickListener listener;

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestItemBinding binding = RequestItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RideViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride rideItem = rides.get(position);
        Order order = orders.get(position);
        holder.bind(rideItem, order);
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public void updateRides(List<Ride> rides, List<Order> orders) {
        this.rides = rides;
        this.orders = orders;
        Log.i("requests", "rides" + String.valueOf(rides.size()));
        if(rides.size() > 0) Log.i("requests", rides.get(0).getPushId());
        Log.i("requests", "orders" + String.valueOf(orders.size()));
        // to redraw recycler view
        notifyDataSetChanged();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {
        RequestItemBinding binding;

        public RideViewHolder(RequestItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ride rideItem, Order order) {
            binding.riderName.setText(order.getRiderName());
            binding.source.setText(rideItem.getSrc());
            binding.destination.setText(rideItem.getDest());
            binding.date.setText(rideItem.getDate());
            binding.time.setText(rideItem.getTime());
            binding.capacityValue.setText(String.valueOf(rideItem.getCapacity()));
            binding.costValue.setText(String.valueOf(rideItem.getCost()));
            binding.confirmBtn.setOnClickListener(view -> {
                if(listener != null)
                    listener.onConfirmClick(rideItem, order);
            });
            binding.declineBtn.setOnClickListener(view -> {
                if(listener != null)
                    listener.onDeclineClick(rideItem, order);
            });
        }

    }
    public interface OnRequestClickListener {
        void onConfirmClick(Ride ride, Order order);
        void onDeclineClick(Ride ride, Order order);
    }
    public void setOnRequestClickListener(OnRequestClickListener listener){
        this.listener = listener;
    }
}
