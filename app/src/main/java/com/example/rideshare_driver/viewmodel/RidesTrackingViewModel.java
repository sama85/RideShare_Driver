package com.example.rideshare_driver.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.rideshare_driver.models.Ride;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RidesTrackingViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Ride>> rides = new MutableLiveData<>(new ArrayList<>());
    private DatabaseReference ridesRef;
    private MutableLiveData<List<String>> ridesId = new MutableLiveData<>(new ArrayList<>());
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    public MutableLiveData<List<Ride>> getRides() {
        return rides;
    }
    public MutableLiveData<List<String>> getRideIds() {
        return ridesId;
    }
    public RidesTrackingViewModel(@NonNull Application application) {
        super(application);
        ridesRef = FirebaseDatabase.getInstance("https://rideshareapp-authentication-default-rtdb.europe-west1.firebasedatabase.app/").getReference("rides");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        fetchRides();
    }

    public void fetchRides() {
        ridesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Ride> ridesList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Ride ride = dataSnapshot.getValue(Ride.class);
                        //search ride in ids
                        if (ride != null && ride.getDriverId().equals(firebaseUser.getUid())) {
                            ridesList.add(ride);
                        }
                    }
                    rides.setValue(ridesList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FirebaseData", "onCancelled", error.toException());
            }
        });
    }

}
