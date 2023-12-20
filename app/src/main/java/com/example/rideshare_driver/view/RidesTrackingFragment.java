package com.example.rideshare_driver.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rideshare_driver.adapters.RidesTrackingAdapter;
import com.example.rideshare_driver.databinding.FragmentRidesTrackingBinding;
import com.example.rideshare_driver.models.Ride;
import com.example.rideshare_driver.viewmodel.RidesTrackingViewModel;

import java.util.List;

public class RidesTrackingFragment extends Fragment {
    FragmentRidesTrackingBinding binding;
    RidesTrackingViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentRidesTrackingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RidesTrackingViewModel.class);
        RidesTrackingAdapter adapter = new RidesTrackingAdapter();
        RidesTrackingAdapter.OnTrackingClickListener listener = new RidesTrackingAdapter.OnTrackingClickListener() {
            @Override
            public void onItemClick(Ride ride) {
                viewModel.cancelRide(ride);
            }
        };
        adapter.setOnTrackingClickListener(listener);
        binding.ridesTrackingList.setAdapter(adapter);
        viewModel.getRides().observe(getViewLifecycleOwner(), new Observer<List<Ride>>() {
            @Override
            public void onChanged(List<Ride> rides) {
                if(rides != null) {
                    Log.i("tracking", String.valueOf(rides.size()));
                    adapter.updateRides(rides);
                }
            }
        });
    }
}
