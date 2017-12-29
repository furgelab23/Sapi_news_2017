package com.example.a2017_09_13.sapi_news_2017.model;

/**
 * Created by 2017-09-13 on 11/10/2017.
 */

public class User {

    String firstName;
    String lastName;
    String emailAddress;
    //    String password;
    String phoneNumber;

    public User() {
    }

    public User(String firstname, String lastname, String emailaddress, String telephone) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.emailAddress = emailaddress;
        //this.password = password;
        this.phoneNumber = telephone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
