package com.ParentalDrivingSafetySystem.Project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Driveruser {

    FirebaseDatabase firebaseDatabase;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();


    public String getFirstname() {
        return firstname;
    }


    private String firstname;
    private String lastname;
    private String mail;
    private String mobile;
    private String age;
    private String password;
    private String emergency;
    private String userid;
    private String id;
    private Integer usertype;
    private Integer accumelativerate;

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

    public String getEmergency() {
        return emergency;
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



    public Integer getAccumelativerate() {
        return accumelativerate;
    }

    public void setAccumelativerate(Integer accumelativerate) {
        this.accumelativerate = accumelativerate;
    }



    public Driveruser(String firstname, String lastname, String mail, String mobile, String age, String password, String emergency, String userid, String id, Integer usertype, Integer accumelativerate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.mobile= mobile;
        this.age = age;
        this.password = password;
        this.emergency = emergency;
        this.userid=userid;
        this.id=id;
        this.usertype = usertype;
        this.accumelativerate = accumelativerate;

    }

    public Driveruser() {
    }

    void writeNewUser(String firstname, String lastname, String mail, String mobile, String age, String password, String emergency, String userid,Integer usertype,Integer accumelativerate) {

        Driveruser user = new Driveruser(firstname, lastname, mail, mobile, age, password, emergency,id,userid, 0,0);
        mDatabase.child("users").child(userid).setValue(user);

    }



}
