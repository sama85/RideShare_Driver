package com.example.rideshare_driver.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.rideshare_driver.models.Order;
import com.example.rideshare_driver.models.Ride;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Ride>> rides = new MutableLiveData<>(new ArrayList<>());
    private DatabaseReference ordersRef;
    private DatabaseReference ridesRef;
    private MutableLiveData<List<String>> ridesId = new MutableLiveData<>(new ArrayList<>());
    public List<Order> orders = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    public MutableLiveData<List<Ride>> getRides() {
        return rides;
    }
    public MutableLiveData<List<String>> getRideIds() {
        return ridesId;
    }
    public RequestsViewModel(@NonNull Application application) {
        super(application);
        ordersRef = FirebaseDatabase.getInstance("https://rideshareapp-authentication-default-rtdb.europe-west1.firebasedatabase.app/").getReference("orders");
        ridesRef = FirebaseDatabase.getInstance("https://rideshareapp-authentication-default-rtdb.europe-west1.firebasedatabase.app/").getReference("rides");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        fetchIds();
    }

    public void fetchIds(){
        /** add logic to:
         *      1.  get all ride IDs of this rider in a list
         *      2. loop through ids and fetch value (add value listener) for each id
         * */
        Query query = ordersRef.orderByChild("driverId").equalTo(firebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> ids = new ArrayList<>();
                    List<Order> ordersList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String id = dataSnapshot.child("rideId").getValue(String.class);
                        String riderName = dataSnapshot.child("riderName").getValue(String.class);
                        String orderId = dataSnapshot.child("pushId").getValue(String.class);
                        Order order = new Order(riderName, orderId);

                        if(id != null) {
                            ids.add(id);
                            ordersList.add(order);
                        }
                    }
                    ridesId.setValue(ids);
                    orders = ordersList;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FirebaseData", "onCancelled", error.toException());
            }
        });
    }

    public void fetchRides(List<String> ids) {
        ridesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Ride> ridesList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Ride ride = dataSnapshot.getValue(Ride.class);
                        // create request with ride and rider name and send to adapter to display
                        for(String id : ids) {
                            //search ride in ids
                            if (ride != null && dataSnapshot.getKey().equals(id)) {
                                Log.i("tracking", "ride src: " + ride.getSrc());
                                ridesList.add(ride);
                            }
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

    public void decline(Order order){
        // update order status to declined
        Log.i("request", order.getPushId());
        ordersRef.child(order.getPushId()).child("status").setValue(RequestStatus.DECLINE.status);
    }

    public void confirm(Ride ride, Order order){
        // update no. of seats left & status
        Log.i("request", ride.getPushId());
        Log.i("request", order.getPushId());
        ordersRef.child(order.getPushId()).child("status").setValue(RequestStatus.CONFIRM.status);
        ridesRef.child(ride.getPushId()).child("capacity")
                .setValue(ride.getCapacity() - 1);
    }

    private enum RequestStatus{
        CONFIRM("confirmed"),
        DECLINE("declined");

        private String status;
        RequestStatus(String status){
            this.status = status;
        }
    }
}
