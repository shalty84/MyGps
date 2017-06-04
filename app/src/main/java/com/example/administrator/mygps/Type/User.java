package com.example.administrator.mygps.Type;

import android.net.Uri;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by shalty on 14/01/2017.
 */

public class User
{
    private String displayName;
    private String email;
    private String uid;
    private String photoUrl;
    private boolean guest;
    private static User userInstance;
    private int carTape;




    private User () {}
    public static void UserInit(FirebaseUser user,boolean guest)
    {
        if(user!=null){
        if (userInstance!=null) {
            userInstance.setDisplayName(user.getDisplayName());
            userInstance.setEmail(user.getEmail());
            userInstance.setUid(user.getUid());
            userInstance.setGuest(guest);
            userInstance.setPhotoUrl(user.getPhotoUrl()!=null?user.getPhotoUrl().toString():"");
        }
        else
        {
            userInstance = new User();
            userInstance.setGuest(guest);
            userInstance.setDisplayName(user.getDisplayName());
            userInstance.setEmail(user.getEmail());
            userInstance.setUid(user.getUid());
            userInstance.setPhotoUrl(user.getPhotoUrl()!=null?user.getPhotoUrl().toString():"");

        }
        }else
        {
            userInstance = new User();
            userInstance.setGuest(guest);
        }
    }
    public static void UserInit(User user)
    {
        userInstance = user;
    }
    public static User getUserInstance()
    {
        return userInstance==null?userInstance=new User():userInstance;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }
    public void logOut()
    {
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        userInstance = null;

    }

    public int getCarTape() {
        return this.carTape;
    }

    public void setCarTape(int carTape) {
        this.carTape = carTape;
    }

    public boolean isGuest() {
        return this.guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
