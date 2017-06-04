package com.example.administrator.mygps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.administrator.mygps.Interfaces.FirebaseUserHlper;
import com.example.administrator.mygps.Type.User;
import com.example.administrator.mygps.Utility.UtilFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    public  final  static FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        facbookHsh s = new facbookHsh();

        if (firebaseAuth.getCurrentUser()!=null)
        {
            Log.d("user", "onCreate: SplashScreen"+firebaseAuth.getCurrentUser().getEmail());
            UtilFirebase.getUser(firebaseAuth.getCurrentUser().getUid(), new FirebaseUserHlper() {
                @Override
                public void getUser(User user) {

                    User.UserInit(user);
                    Intent intent = new Intent(SplashScreen.this,HomeScreen.class);
                    startActivity(intent);
                }
            });

        }
        else
        {
            Intent intent = new Intent(this,WelcomeScreen.class);
            startActivity(intent);
        }


    }

    public void signIn(View view) {


    }
}
