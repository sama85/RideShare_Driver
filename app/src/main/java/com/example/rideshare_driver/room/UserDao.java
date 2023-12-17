package com.example.rideshare_driver.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);
    @Insert
    void delete(User user);
    @Update
    void update(User user);
    @Query("DELETE FROM user")
    void deleteAll();
    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();
    @Query("SELECT * FROM user WHERE email = :email")
    LiveData<User> getUser(String email);
    @Query("SELECT * FROM user WHERE email = :email")
    User getUserData(String email);
}