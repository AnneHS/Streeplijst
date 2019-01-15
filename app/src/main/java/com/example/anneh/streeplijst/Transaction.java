package com.example.anneh.streeplijst;

public class Transaction {
    int userID;
    String username;
    String productName;
    float price;
    int amount;
    float total;

    public Transaction(int userID, String userName, String productName, float price, int amount) {
        this.userID = userID;
        this.username = userName;
        this.productName = productName;
        this.price = price;
        this.amount = amount;
        this.total = price * amount;
    }

    // Getters
    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getProductName() {
        return productName;
    }

    public float getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public float getTotal() {
        return total;
    }

    // Setters
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
