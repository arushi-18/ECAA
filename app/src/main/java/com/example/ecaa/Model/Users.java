package com.example.ecaa.Model;

public class Users {
    String email,password,userType,name,phone,address,image;
    public Users()
    {

    }

    public Users(String email, String password, String userType, String name, String phone, String address, String image) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.name=name;
        this.phone=phone;
        this.address=address;
        this.image=image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
