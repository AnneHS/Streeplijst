/*
Anne Hoogerduijn Strating
12441163

User class.
 */

package com.example.anneh.streeplijst;

public class User {
    String name;
    String imgPath;
    String imgName;

    // Constructor
    public User(String userName, String path, String image) {

        this.name = userName;
        this.imgPath = path;
        this.imgName = image;
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getImgPath() { return imgPath; }
    public String getImgName() { return imgName; }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setImgPath(String imgPath) { this.imgPath = imgPath; }
    public void setImgName(String imgName) { this.imgName = imgName; }
}
