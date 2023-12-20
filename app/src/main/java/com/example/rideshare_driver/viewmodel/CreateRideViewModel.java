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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CreateRideViewModel extends AndroidViewModel {
    private String[] locations = {"Ain Shams University Gate 3", "Ain Shams University Gate 4", "October City", "Maadi", "Nasr City","Haram", "Fifth Settlement", "Zayed City"};
    private String[] times = {"7:30 AM", "5:30 PM"};
    Repository repository;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference ridesRef;
    public LiveData<User> driver;
    private String rideCompositeKey = "rideCompositeKey";
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
        ride.setStatus(RideStatus.AVAILABLE.status);

        /** add ride if it doesn't exist already */

        String compositeKey = ride.getSrc()+","+ride.getDest()+","+ride.getDate()+","+ride.getTime();
        final boolean[] exists = {false};

        Query query = ridesRef.orderByChild(rideCompositeKey).equalTo(compositeKey);
        query.addListenerForSingleValueEvent(new ValueEventListener()
            {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) Toast.makeText(getApplication().getApplicationContext(), "Ride already exists!", Toast.LENGTH_SHORT).show();
                else{
                    String key = ridesRef.push().getKey();
                    ride.setPushId(key);
                    ride.setRideCompositeKey(compositeKey);
                    ridesRef.child(key).setValue(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplication().getApplicationContext(), "Ride added successfully!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public String[] getLocations() {
        return locations;
    }

    public String[] getTimes() {
        return times;
    }

    public enum RideStatus{
        AVAILABLE("available"),
        UNAVAILABLE("unavailable");
       public String status;
        private RideStatus(String status){
            this.status = status;
        }
    }
}
