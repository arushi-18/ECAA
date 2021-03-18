package com.example.ecaa.Model;

public class Users {
    String email,password,userType;
    public Users()
    {

    }

    public Users(String email, String password, String userType,String UserName) {
        this.email = email;
        this.password = password;
        this.userType = userType;
       /* this.userName=Name;*/
    }

    public String getEmail() {        return email; }

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

    /*public void setName(){ this.userName=Name;}*/

    /*public String getName(){ return userName;}*/

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
