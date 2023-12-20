package com.example.rideshare_driver.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    //    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    private int id;
    @PrimaryKey
    @NonNull
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String phone;
    private String carNumber;

    public User(@NonNull String email, @NonNull String name, @NonNull String phone, String carNumber) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.carNumber = carNumber;
    }

    //    public int getId() {
//        return id;
//    }
    @NonNull
    public String getName() {
        return name;
    }
    @NonNull
    public String getEmail() {
        return email;
    }
    @NonNull
    public String getPhone() {
        return phone;
    }
    public String getCarNumber() {
        return carNumber;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}