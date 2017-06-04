package com.example.administrator.mygps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mygps.Interfaces.FirebaseUserHlper;
import com.example.administrator.mygps.Type.User;
import com.example.administrator.mygps.Utility.UtilFirebase;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import java.util.HashSet;
import java.util.Set;

public class LogIn extends AppCompatActivity implements View.OnClickListener{

    AutoCompleteTextView email;
    static Context context;
    EditText password;
    Button register,sinOut,loggin;
    LoginButton loginFacebookButton;
    public  final  static FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    public final static String USER_IMAIL = "userImail";
    SharedPreferences prefs;
    private CallbackManager callbackManager;
    SharedPreferences.Editor editor;
    public static String userName;
    AuthCredential authCredential;
    private static Set<String> emailList = new HashSet<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        context = this;
        callbackManager = CallbackManager.Factory.create();
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sinOut = (Button)findViewById(R.id.email_sign_out_button);
        loggin = (Button)findViewById(R.id.email_loggin_button);
        loginFacebookButton = (LoginButton) findViewById(R.id.login_button);
        loginFacebookButton.setReadPermissions("email", "public_profile");
        //loginFacebookButton.setFragment(this);
        loginFacebookButton.registerCallback(callbackManager,mCallback);
        UtilFirebase.setContext(this);
        prefs = this.getSharedPreferences("SHARED_PREFS_FILE", Context.MODE_PRIVATE);
        editor = prefs.edit();
        sinOut.setOnClickListener(this);
        loggin.setOnClickListener(this);
    }
    public static void logOut(Context context)
    {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(context,WelcomeScreen.class);
        context.startActivity(intent);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.email_loggin_button:
            {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "loggin isSuccessful", Toast.LENGTH_LONG).show();
                                    UtilFirebase.getUser(task.getResult().getUser().getUid(), new FirebaseUserHlper() {
                                        @Override
                                        public void getUser(User user) {
                                            if(user!=null)
                                            User.UserInit(user);
                                            Intent intent = new Intent(context,HomeScreen.class);
                                            context.startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                break;
            }
        }
    }
    private void saveToPref(Set<String> emailList)
    {
        editor.putStringSet(USER_IMAIL,emailList);
        editor.commit();
    }
    private Set<String> getFromPref()
    {
        emailList = prefs.getStringSet(USER_IMAIL,null);
        return emailList;
    }
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>()
    {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            handleFacebookAccessToken(accessToken);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };
    private void handleFacebookAccessToken(AccessToken token)
    {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(firebaseAuth.getCurrentUser().getProviders().get(0).equals("facebook.com")) {
                                    // Id of the provider (ex: google.com)
                                    final UserInfo profile = firebaseAuth.getCurrentUser().getProviderData().get(0);
                                    UtilFirebase.getUser(task.getResult().getUser().getUid(), new FirebaseUserHlper() {
                                        @Override
                                        public void getUser(User user) {
                                            if(user!=null)
                                            {
                                                User.UserInit(user);
                                                Intent intent = new Intent(LogIn.this,HomeScreen.class);
                                                startActivity(intent);
                                            }else
                                            {
                                                User.getUserInstance().setPhotoUrl(profile.getPhotoUrl().toString());
                                                User.getUserInstance().setUid(profile.getUid());
                                                User.getUserInstance().setCarTape(1);
                                                User.getUserInstance().setDisplayName(profile.getDisplayName());
                                                User.getUserInstance().setEmail(profile.getEmail());
                                                User.getUserInstance().setGuest(false);
                                                UtilFirebase.SaveUser(User.getUserInstance());
                                                Intent intent = new Intent(LogIn.this,HomeScreen.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });

                            }else
                            {
                                UtilFirebase.getUser(firebaseAuth.getCurrentUser().getUid(), new FirebaseUserHlper() {
                                    @Override
                                    public void getUser(User user) {
                                        if(user!=null)
                                        {
                                            User.UserInit(user);
                                            Intent intent = new Intent(LogIn.this,HomeScreen.class);
                                            startActivity(intent);
                                        }else
                                        {}
                                    }
                                });
                            }
                        }

                        // ...
                    }
                });
    }

}
