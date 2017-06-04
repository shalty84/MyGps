package com.example.administrator.mygps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.mygps.R;
import com.example.administrator.mygps.Type.User;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    }

    public void SignIn(View view) {

        Intent intent = new Intent(this,LogIn.class);
        startActivity(intent);
    }

    public void ContinueAsAGuest(View view) {

        User.UserInit(null,true);
        Intent intent = new Intent(this,HomeScreen.class);
        startActivity(intent);
    }

    public void register(View view) {

        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
}
