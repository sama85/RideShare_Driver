package com.example.rideshare_driver.models;

public class Order {
    private String riderName;
    private String pushId;
    private String paymentMethod;
    private String status;

    public Order(String riderName, String pushId, String paymentMethod, String status) {
        this.riderName = riderName;
        this.pushId = pushId;
        this.paymentMethod = paymentMethod;
        this.status = status;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
