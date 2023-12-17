package com.example.rideshare_driver.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rideshare_driver.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rideshare_driver.R;
import com.example.rideshare_driver.databinding.FragmentProfileBinding;

import com.example.rideshare_driver.room.User;
import com.example.rideshare_driver.viewmodel.ProfileViewModel;
import com.example.rideshare_driver.room.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEditMode(false);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null)
                    updateProfileData(user);
            }
        });

        viewModel.isEditMode.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean editMode) {
                setEditMode(editMode);
            }
        });
        binding.btnSignout.setOnClickListener(v -> {
            viewModel.handle_signout();
            navigateToSignIn();
        });

        binding.editBtn.setOnClickListener(v -> {
            viewModel.handle_edit_click();
        });

        binding.saveBtn.setOnClickListener(v -> {
            viewModel.handle_save_click();
        });

        // Handle cancel action
        binding.cancelBtn.setOnClickListener(v -> {
            viewModel.handle_cancel_click();
        });
    }

    // Method to toggle between display and edit modes
    private void setEditMode(boolean enabled) {
        if (enabled) {
            binding.nameEt.setVisibility(View.VISIBLE);
            binding.emailEt.setVisibility(View.VISIBLE);
            binding.phoneEt.setVisibility(View.VISIBLE);
            binding.name.setVisibility(View.GONE);
            binding.email.setVisibility(View.GONE);
            binding.phone.setVisibility(View.GONE);
            binding.editBtn.setVisibility(View.GONE);
            binding.saveBtn.setVisibility(View.VISIBLE);
            binding.cancelBtn.setVisibility(View.VISIBLE);
        } else {
            binding.nameEt.setVisibility(View.GONE);
            binding.emailEt.setVisibility(View.GONE);
            binding.phoneEt.setVisibility(View.GONE);
            binding.name.setVisibility(View.VISIBLE);
            binding.email.setVisibility(View.VISIBLE);
            binding.phone.setVisibility(View.VISIBLE);
            binding.editBtn.setVisibility(View.VISIBLE);
            binding.saveBtn.setVisibility(View.GONE);
            binding.cancelBtn.setVisibility(View.GONE);
        }
    }

    private void updateProfileData(User user){
        binding.name.setText(user.getName());
        binding.email.setText(user.getEmail());
        binding.phone.setText(user.getPhone());
    }
    void navigateToSignIn(){
        Intent intent  = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

}