package com.example.rideshare_driver.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rideshare_driver.R;

import static androidx.navigation.ui.NavigationUI.setupWithNavController;

import android.content.Intent;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import com.example.rideshare_driver.databinding.ActivityDriverBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverActivity extends AppCompatActivity {
    ActivityDriverBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    NavHost navHost;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        checkAuth();
        navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_frame);
        navController = navHost.getNavController();
        setupWithNavController(binding.bottomNavBar, navController);
    }
    void checkAuth(){
        if (user == null)
            navigateToSignIn();
    }
    void navigateToSignIn(){
        Intent intent  = new Intent(DriverActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
