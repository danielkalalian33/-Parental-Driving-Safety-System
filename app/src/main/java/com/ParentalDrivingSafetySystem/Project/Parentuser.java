package com.ParentalDrivingSafetySystem.Project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Parentuser {

    FirebaseDatabase firebaseDatabase;

    DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference();

    private String firstname;
    private String lastname;
    private String mail;
    private String mobile;
    private String age;
    private String password;;

    public String getLastname() {
        return lastname;
    }
    public String getMail() {
        return mail;
    }
    public String getMobile() {
        return mobile;
    }
    public String getAge() {
        return age;
    }
    public String getFirstname() {
       return firstname;
   }
    public String getPassword() {
        return password;
    }
    public String getUserid() {
        return userid;
    }
    public Integer getUsertype() {
        return usertype;
    }
    public String getId() {
        return id;
    }
    private String userid;
    private String id;
    private Integer usertype;


    public Parentuser(String firstname, String lastname, String mail, String mobile, String age, String password,  String userid, String id,Integer usertype) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.mobile= mobile;
        this.age = age;
        this.password = password;
        this.userid=userid;
        this.id=id;
        this.usertype = usertype;


    }

    public Parentuser() {
    }

    void writeNewUser(String firstname, String lastname, String mail, String mobile, String age, String password, String userid,Integer usertype) {

        Parentuser user = new Parentuser(firstname, lastname, mail, mobile, age, password,id,userid, 1);
        mDatabase2.child("users").child(userid).setValue(user);
    }



}
