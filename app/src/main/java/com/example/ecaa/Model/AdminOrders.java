package com.example.ecaa.Model;

public class AdminOrders
{   private String email,name,phone,address,city,state,date,time,totalAmount;

    public AdminOrders() {
    }

    public AdminOrders(String email,String name, String phone, String address, String city, String state, String date, String time, String totalAmount)
    {
        this.email=email;
        this.name=name;
        this.phone=phone;
        this.address=address;
        this.city=city;
        this.state=state;
        this.date=date;
        this.time=time;
        this.totalAmount=totalAmount;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
