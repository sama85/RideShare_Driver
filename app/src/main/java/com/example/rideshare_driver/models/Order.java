package com.example.rideshare_driver.models;

public class Order {
    private String riderName;
    private String pushId;

    public Order(String riderName, String pushId) {
        this.riderName = riderName;
        this.pushId = pushId;
    }

    public void Order() {}

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
