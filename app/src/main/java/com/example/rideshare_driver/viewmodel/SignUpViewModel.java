package com.example.rideshare_driver.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.rideshare_driver.repository.Repository;
import com.example.rideshare_driver.room.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpViewModel extends AndroidViewModel {
    private Repository repository;
    FirebaseAuth mAuth;
    public MutableLiveData<String> signUpEmail = new MutableLiveData<>();
    public SignUpViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(String email, String password, String name, String phone, String carNumber) {
        String regex = "^[a-zA-Z0-9_.+-]+@eng\\.asu\\.edu\\.eg$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches() == false)
            Toast.makeText(getApplication().getApplicationContext(), "Please enter valid domain email: .eng.asu.edu.eg ", Toast.LENGTH_LONG).show();

            else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                User user = new User(email, name, phone, carNumber);
                                repository.insert(user);
                                signUpEmail.postValue(email);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getApplication(), "Sign Up Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }
}
