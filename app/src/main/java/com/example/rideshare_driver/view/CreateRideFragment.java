package com.example.rideshare_driver.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rideshare_driver.R;
import com.example.rideshare_driver.databinding.ActivitySignUpBinding;
import com.example.rideshare_driver.databinding.FragmentCreateRideBinding;
import com.example.rideshare_driver.room.User;
import com.example.rideshare_driver.viewmodel.CreateRideViewModel;
import com.example.rideshare_driver.viewmodel.ProfileViewModel;

import java.util.Calendar;

public class CreateRideFragment extends Fragment {

    CreateRideViewModel viewModel;
    FragmentCreateRideBinding binding;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayAdapter<String> timesAdapter;
    private DatePickerDialog datePickerDialog;
    private String src;
    private  String dest;
    private String date;
    private String time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentCreateRideBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CreateRideViewModel.class);
        itemsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.drop_down_menu_item, viewModel.getLocations());
        timesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.drop_down_menu_item, viewModel.getTimes());
        binding.autoCompSource.setAdapter(itemsAdapter);
        binding.autoCompDest.setAdapter(itemsAdapter);
        binding.autoCompTime.setAdapter(timesAdapter);
        binding.dateBtn.setText(getTodaysDate());

        initDatePicker();
        binding.autoCompSource.setOnItemClickListener((adapterView, view1, i, l) -> {
            src = adapterView.getItemAtPosition(i).toString();
        });
        binding.autoCompDest.setOnItemClickListener((adapterView, view1, i, l) -> {
            dest = adapterView.getItemAtPosition(i).toString();
        });
        binding.autoCompTime.setOnItemClickListener((adapterView, view1, i, l) -> {
            time = adapterView.getItemAtPosition(i).toString();
        });
        binding.dateBtn.setOnClickListener(view1 -> datePickerDialog.show());
        binding.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cost, capacity;
                cost = binding.costEt.getText().toString();
                capacity = binding.numPassengersEt.getText().toString();

                if(cost.isEmpty()|| capacity.isEmpty() || src.isEmpty() || dest.isEmpty() || time.isEmpty()
                        ||  Integer.valueOf(capacity) < 1){
                    if((!capacity.isEmpty() && Integer.valueOf(capacity) > 0))
                        Toast.makeText(requireActivity(), "Please complete all fields!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(requireActivity(), "Please enter positive capacity!", Toast.LENGTH_SHORT).show();
                }
                else {
                    viewModel.driver.observe(requireActivity(), new Observer<User>() {
                        @Override
                        public void onChanged(User driver) {
                            if (driver != null) {
                                viewModel.createRide(src, dest, date, time, cost, capacity, driver);
                            }
                        }
                    });
                }

            }
        });
    }
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                date = makeDateString(day, month, year);
                binding.dateBtn.setText(date);

            }
        };
        date = getTodaysDate();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        this.datePickerDialog = new DatePickerDialog(requireActivity(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        return month + "/" + day + "/" + year;
    }
}
