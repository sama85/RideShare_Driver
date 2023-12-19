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

import com.example.rideshare_driver.adapters.RequestsListAdapter;
import com.example.rideshare_driver.databinding.FragmentProfileBinding;
import com.example.rideshare_driver.databinding.FragmentRequestsBinding;
import com.example.rideshare_driver.models.Ride;
import com.example.rideshare_driver.room.User;
import com.example.rideshare_driver.viewmodel.ProfileViewModel;
import com.example.rideshare_driver.viewmodel.RequestsViewModel;

import java.util.List;

public class RequestsFragment extends Fragment {
    private FragmentRequestsBinding binding;
    private RequestsViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentRequestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RequestsViewModel.class);
        RequestsListAdapter  adapter = new RequestsListAdapter();
        binding.requestsList.setAdapter(adapter);
        viewModel.getRideIds().observe(requireActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> ids) {
                if(!ids.isEmpty()){
                    Log.i("tracking", String.valueOf(ids.size()));
                    viewModel.fetchRides(ids);
                }
            }
        });
        viewModel.getRides().observe(getViewLifecycleOwner(), new Observer<List<Ride>>() {
            @Override
            public void onChanged(List<Ride> rides) {
                if(rides != null) {
                    adapter.updateRides(rides);
                }
            }
        });
    }
}
