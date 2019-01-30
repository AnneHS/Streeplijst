/*
Anne Hoogerduijn Strating
12441163

The product class.

 */

package com.example.anneh.streeplijst;

import java.io.Serializable;

public class Product implements Serializable {
    String name;
    float price;
    String imgPath;
    String imgName;

    // Constructor
    public Product(String productName, float productPrice, String path, String img) {
        super();
        this.name = productName;
        this.price = productPrice;
        this.imgPath = path;
        this.imgName = img;
    }

    // Getters
    public String getName() {
        return name;
    }
    public float getPrice() {
        return price;
    }
    public String getImgPath() { return imgPath; }
    public String getImgName() { return imgName; }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setImgPath(String imgPath) { this.imgPath = imgPath; }
    public void setImgName(String imgName) { this.imgName = imgName; }
}
