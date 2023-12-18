package com.example.rideshare_driver.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rideshare_driver.models.Ride;
import com.example.rideshare_driver.repository.Repository;
import com.example.rideshare_driver.room.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRideViewModel extends AndroidViewModel {
    Repository repository;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference ridesRef;
    public LiveData<User> driver;
    public CreateRideViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        driver = repository.getUser(firebaseUser.getEmail());
        ridesRef = FirebaseDatabase.getInstance("https://rideshareapp-authentication-default-rtdb.europe-west1.firebasedatabase.app/").getReference("rides");
    }

    public void createRide(String src, String dest, String date, String time, String cost, String capacity, User driver) {
        // get driver details from room
        // create ride
        //add ride to fb
        Ride ride = new Ride(src, dest, date, time, Long.valueOf(cost), Integer.valueOf(capacity), firebaseUser.getUid());
        ride.setDriverName(driver.getName());
        ride.setDriverPhone(driver.getPhone());
        ride.setCarNumber(driver.getCarNumber());
        //add push id to ride
        String key = ridesRef.push().getKey();
        ride.setPushId(key);
        ridesRef.child(key).setValue(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplication().getApplicationContext(), "Ride added successfully!", Toast.LENGTH_LONG).show();
            }
        });
    }


}
