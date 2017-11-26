package com.example.a2017_09_13.sapi_news_2017.model;

/**
 * Created by 2017-09-13 on 11/10/2017.
 */

public class User {

    String firstname;
    String lastname;
    String emailaddress;
    String password;
    String telephone;

    public User(String userId, String email) {
    }

    public User(String firstname, String lastname, String emailaddress, String password, String telephone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailaddress = emailaddress;
        this.password = password;
        this.telephone = telephone;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
