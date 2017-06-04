package com.example.administrator.mygps;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.mygps.Interfaces.FirebaseImegHlper;
import com.example.administrator.mygps.Type.User;
import com.example.administrator.mygps.Utility.CircleTransform;
import com.example.administrator.mygps.Utility.UtilFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;


import rx.functions.Action1;

import static com.example.administrator.mygps.LogIn.firebaseAuth;

public class SignUp extends AppCompatActivity {

    public static Uri userImg;
    private int carTape;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void pickImg(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.radio_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            RxImagePicker.with(SignUp.this).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
                                @Override
                                public void call(Uri uri) {
                                    User.getUserInstance().setPhotoUrl(uri.toString());
                                    Picasso.with(SignUp.this).load(uri).transform(new CircleTransform()).rotate(90f).into((ImageView) findViewById(R.id.user_img_pik));
                                }
                            });
                        }else if(which==1)
                        {
                            RxImagePicker.with(SignUp.this).requestImage(Sources.GALLERY).subscribe(new Action1<Uri>() {
                                @Override
                                public void call(Uri uri) {
                                    User.getUserInstance().setPhotoUrl(uri.toString());
                                    Picasso.with(SignUp.this).load(uri).transform(new CircleTransform()).rotate(90f).into((ImageView) findViewById(R.id.user_img_pik));
                                }
                            });
                        }
                    }
                }).show();
       builder.create();
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.privateCar:
                if (checked)
                   carTape = 1;
                    break;
            case R.id.suvCar:
                if (checked)
                    carTape = 2;
                    break;
            case R.id.ImprovedSUV:
                if (checked)
                    carTape = 3;
                    break;
        }


    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    public void SignUp(View view) {

        EditText email = (EditText)findViewById(R.id.emailSigUp);
        final EditText fullName = (EditText)findViewById(R.id.fulName);
        EditText password1 = (EditText)findViewById(R.id.password);
        EditText password2 = (EditText)findViewById(R.id.ConfirmPassword);

        if(password1.getText().toString().equals(password2.getText().toString())) {
            if (email.length() > 0 && password1.length() > 0 && password2.length() > 0 && fullName.length() > 0) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString())
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {

                             if (task.getException()==null)
                             {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(SignUp.this, "loggin isSuccessful", Toast.LENGTH_LONG).show();
                                    User.UserInit(task.getResult().getUser(), false);
                                    User.getUserInstance().setDisplayName(fullName.getText().toString());
                                    User.getUserInstance().setCarTape(carTape);
                                    User.getUserInstance().setUid(task.getResult().getUser().getUid());
                                    UtilFirebase.SaveUser(User.getUserInstance());
                                    UtilFirebase.saveUserImg(User.getUserInstance().getUid(),
                                            User.getUserInstance().getEmail(),
                                            (ImageView) findViewById(R.id.user_img_pik), new FirebaseImegHlper() {
                                                @Override
                                                public void getUserImg(Uri userImege) {
                                                    User.getUserInstance().setPhotoUrl(userImege!=null?userImege.toString():"");
                                                    UtilFirebase.SaveUserImgString(userImege.toString(),User.getUserInstance());
                                                    Intent intent = new Intent(SignUp.this, HomeScreen.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });


                                    Log.d("user", "GETUSERIMAGECOL BEK1");
                                } else
                                    {

                                        Toast.makeText(SignUp.this, "The username or password does not meet the requirements", Toast.LENGTH_LONG).show();
                                    }
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            }
                        });

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("מלא בבקשה את השדות החסרים")
                        .setPositiveButton("אשר", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                            }
                        }).show();
                // Create the AlertDialog object and return it
                builder.create();
            }
        }else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("הססמאות אינם תואמות")
                        .setPositiveButton("אשר", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
                builder.create();
            }


    }
}
