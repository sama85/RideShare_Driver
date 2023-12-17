package com.example.rideshare_driver.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.rideshare_driver.repository.Repository;
import com.example.rideshare_driver.room.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpViewModel extends AndroidViewModel {
    private Repository repository;
    FirebaseAuth mAuth;
    public MutableLiveData<String> signUpEmail = new MutableLiveData<>();
    public SignUpViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(String email, String password, String name, String phone){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            User user = new User(email, name, phone);
                            repository.insert(user);
                            signUpEmail.postValue(email);
                        } else {
                            // If sign in fails, display a message to the user.

                        }
                    }
                });
    }
}
