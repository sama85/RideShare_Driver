package com.example.rideshare_driver.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rideshare_driver.repository.Repository;
import com.example.rideshare_driver.room.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends AndroidViewModel{
    private Repository repository;
    private LiveData<User> user;
    public MutableLiveData<Boolean> isEditMode = new MutableLiveData<>();
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        user = repository.getUser(firebaseUser.getEmail());
        isEditMode.setValue(false);
    }
    public LiveData<User> getUser() {
        return user;
    }

    public void handle_signout(){
        mAuth.signOut();
    }
    public void handle_edit_click(){
        isEditMode.setValue(true);
    }
    public void handle_save_click(String name, String email, String phone, String carNumber){
        isEditMode.setValue(false);
        User currentUser = user.getValue();
        currentUser.setName(name);
        currentUser.setPhone(phone);
        currentUser.setCarNumber(carNumber);

        repository.update(currentUser);
    }

    public void handle_cancel_click(){
        isEditMode.setValue(false);
    }

}