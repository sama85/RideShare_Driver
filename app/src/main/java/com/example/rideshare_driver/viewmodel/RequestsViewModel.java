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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
                        String orderStatus = dataSnapshot.child("status").getValue(String.class);
                        String paymentMethod = dataSnapshot.child("paymentMethod").getValue(String.class);
                        Order order = new Order(riderName, orderId, paymentMethod, orderStatus);

                        // fetch only pending requests
                        if(id != null && orderStatus.equals(RequestStatus.PENDING.status)) {
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
                            if (ride != null && dataSnapshot.getKey().equals(id)
                            && !ride.getStatus().equals("cancelled")) {
                                int idx = ridesId.getValue().indexOf(id);
                                Order order = orders.get(idx);

                                // add only non-expired requests to display
                                if(!checkExpired(ride, order))
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

    public void decline(Ride ride, Order order){
        // update order status to declined
        ordersRef.child(order.getPushId()).child("status").setValue(RequestStatus.DECLINE.status);
        // remove this ride from rides list
        //removeFromRequests(ride, order);
    }

    public void confirm(Ride ride, Order order){
        // update no. of seats left & order status
        ordersRef.child(order.getPushId()).child("status").setValue(RequestStatus.CONFIRM.status);
        removeFromRequests(ride, order);
        ridesRef.child(ride.getPushId()).child("capacity")
                .setValue(ride.getCapacity() - 1);
        // update ride status if fully booked to be unavailable
        if(ride.getCapacity() == 1){
            ridesRef.child(ride.getPushId()).child("status")
                    .setValue(CreateRideViewModel.RideStatus.UNAVAILABLE.status);
        }


    }

    private void removeFromRequests(Ride ride, Order order) {
        List<Ride> ridesList = rides.getValue();
        List<String> idsList = ridesId.getValue();

        ridesList.remove(ride);
        idsList.remove(ride.getPushId());
        ridesId.setValue(idsList);
    }

    private boolean checkExpired(Ride ride, Order order) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("h:mm a");

            LocalDate date2 = LocalDate.parse(ride.getDate(), formatter);
            LocalTime time2;

            LocalDateTime deadlineDateTime;

            // Get the current date and time for comparison with request deadline
            LocalDateTime currentDateTime = LocalDateTime.now();
            if(ride.getTime().contains("PM")){
                time2 = LocalTime.parse("4:30 PM", timeFormatter);
                deadlineDateTime = LocalDateTime.of(date2, time2);
            }
            else{
                time2 = LocalTime.parse("11:30 PM", timeFormatter);
                deadlineDateTime = LocalDateTime.of(date2, time2);
            }
            if(currentDateTime.isAfter(deadlineDateTime))
               return true;

            else return false;
    }

    private enum RequestStatus{
        CONFIRM("confirmed"),
        DECLINE("declined"),
        PENDING("pending");

        private String status;
        RequestStatus(String status){
            this.status = status;
        }
    }
}
