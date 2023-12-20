package com.example.rideshare_driver.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rideshare_driver.databinding.FragmentProfileBinding;

import com.example.rideshare_driver.room.User;
import com.example.rideshare_driver.viewmodel.ProfileViewModel;

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
                    displayProfileData(user);
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
            String name, email, phone, carNumber;
            name = binding.nameEt.getText().toString();
            email = binding.emailEt.getText().toString();
            phone = binding.phone.getText().toString();
            carNumber = binding.carNumberEt.getText().toString();
            if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || carNumber.isEmpty())
                Toast.makeText(requireActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show();
            else viewModel.handle_save_click(name, email, phone, carNumber);
        });

        // Handle cancel action
        binding.cancelBtn.setOnClickListener(v -> {
            viewModel.handle_cancel_click();
        });
    }

    // Method to toggle between display and edit modes
    private void setEditMode(boolean enabled) {
        if (enabled) {
            binding.emailEt.setEnabled(false);
            binding.nameEt.setVisibility(View.VISIBLE);
            binding.emailEt.setVisibility(View.VISIBLE);
            binding.phoneEt.setVisibility(View.VISIBLE);
            binding.carNumberEt.setVisibility(View.VISIBLE);
            binding.name.setVisibility(View.GONE);
            binding.email.setVisibility(View.GONE);
            binding.phone.setVisibility(View.GONE);
            binding.carNumber.setVisibility(View.GONE);
            binding.editBtn.setVisibility(View.GONE);
            binding.saveBtn.setVisibility(View.VISIBLE);
            binding.cancelBtn.setVisibility(View.VISIBLE);
        } else {
            binding.nameEt.setVisibility(View.GONE);
            binding.emailEt.setVisibility(View.GONE);
            binding.phoneEt.setVisibility(View.GONE);
            binding.carNumberEt.setVisibility(View.GONE);
            binding.name.setVisibility(View.VISIBLE);
            binding.email.setVisibility(View.VISIBLE);
            binding.phone.setVisibility(View.VISIBLE);
            binding.carNumber.setVisibility(View.VISIBLE);
            binding.editBtn.setVisibility(View.VISIBLE);
            binding.saveBtn.setVisibility(View.GONE);
            binding.cancelBtn.setVisibility(View.GONE);
        }
    }

    private void displayProfileData(User user){
        binding.name.setText(user.getName());
        binding.email.setText(user.getEmail());
        binding.phone.setText(user.getPhone());
        binding.carNumber.setText(user.getCarNumber());

        binding.nameEt.setText(user.getName());
        binding.emailEt.setText(user.getEmail());
        binding.phoneEt.setText(user.getPhone());
        binding.carNumberEt.setText(user.getCarNumber());
    }
    void navigateToSignIn(){
        Intent intent  = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

}