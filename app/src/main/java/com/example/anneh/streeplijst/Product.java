package com.example.anneh.streeplijst;

import java.io.Serializable;

public class Product implements Serializable {
    String name;
    float price;

    // Constructor
    public Product(String productName, float productPrice) {
        super();
        this.name = productName;
        this.price = productPrice;
    }

    // Getters
    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(float price) {
        this.price = price;
    }
}
