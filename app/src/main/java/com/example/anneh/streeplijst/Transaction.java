/*
Anne Hoogerduijn Strating
12441163

Transaction class.
 */
package com.example.anneh.streeplijst;

public class Transaction {
    int userID;
    String username;
    int productID;
    String productName;
    float price;
    int amount;
    float total;

    public Transaction(int userID, String userName, int productID, String productName, float price, int amount) {
        this.userID = userID;
        this.username = userName;
        this.productID = productID;
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
    public int getProductID() {
        return productID;
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
    public void setUserID(int userID) { this.userID = userID; }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setProductID(int productID) { this.productID = productID; }
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
