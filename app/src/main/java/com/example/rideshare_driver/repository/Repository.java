package com.example.rideshare_driver.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.rideshare_driver.room.User;
import com.example.rideshare_driver.room.UserDao;
import com.example.rideshare_driver.room.UserDatabase;

public class Repository {
    private UserDao userDao;
//    LiveData<User> user = new LiveData<User>() {
//    };

    public Repository(Application application){
        UserDatabase userDB = UserDatabase.getInstance(application);
        this.userDao = userDB.userDao();
        // user = userDao.getUser(RiderActivity.getUserEmail());
    }

    /** Room runs db operations that return livedata on a bg thread by default */
    public LiveData<User> getUser(String email){
        return userDao.getUser(email);
    }

    public void insert(User user){
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
        });
    }
    public void update(User user){
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userDao.update(user);
        });
    }
}