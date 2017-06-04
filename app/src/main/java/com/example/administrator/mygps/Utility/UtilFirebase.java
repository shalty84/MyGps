package com.example.administrator.mygps.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.example.administrator.mygps.Interfaces.FirebaseHlper;
import com.example.administrator.mygps.Interfaces.FirebaseImegHlper;
import com.example.administrator.mygps.Interfaces.FirebaseUserHlper;
import com.example.administrator.mygps.Type.Track;
import com.example.administrator.mygps.Type.User;
import com.firebase.client.Config;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by Administrator on 27/11/2016.
 */

public class UtilFirebase {


    public static Context _context;
    private static FirebaseHlper _FirebaseHlper;
    private static FirebaseUserHlper _FirebaseUserHlper;
    private static ArrayList<Track> track = new ArrayList<>();



    public static void setContext(Context context)
    {
        _context = context;
    }
    public static void setListener(Object listener)
    {
        _FirebaseHlper = (FirebaseHlper) listener;
        _FirebaseUserHlper = (FirebaseUserHlper)listener;
    }
    public static void SaveUserTrack(Track track, String userUid)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tracks/"+userUid);
        myRef.child(userUid).push();
        DatabaseReference newPostRef = myRef.push();
        if(track!=null)
        newPostRef.setValue(track.convertTrack(track));

    }
    public static void SaveUser(User user)
    {
        Log.d("utilfierbace", user.getUid());
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.push();
        Log.d("user id", user.getUid());
        mDatabase.child(user.getUid()).setValue(user);

    }
    public static void SaveUserImgString(String  imgeUri,User user)
    {
        Log.d("utilfierbace", user.getUid());
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.push();
        Log.d("user id", user.getUid());
        mDatabase.child(user.getUid()).child("photoUrl").setValue(imgeUri);

    }
    public static void getUser(String userUid,FirebaseUserHlper FirebaseUserHlper) {

        _FirebaseUserHlper = FirebaseUserHlper;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(userUid).addListenerForSingleValueEvent
                (new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d("user onDataChange", dataSnapshot.toString());
                        if(dataSnapshot.exists()) {
                            User retrievedUser = dataSnapshot.getValue(User.class);
                            _FirebaseUserHlper.getUser(retrievedUser);

                        }else
                            {
                                _FirebaseUserHlper.getUser(null);
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    public static void saveUserImg(String userId, String imegName, ImageView imageView, final FirebaseImegHlper firebaseImegHlper)
    {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ImagesRef = storage.getReference().child(userId+"/"+imegName+".jpg");

        UploadTask uploadTask = ImagesRef.putBytes(imegToByteArray(imageView));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                firebaseImegHlper.getUserImg(downloadUrl);

            }
        });

    }
    private static byte[] imegToByteArray(ImageView imageView)
    {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }



    public static void SaveSherdTrack(Track track,String uaerName)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sherdTrack");

        DatabaseReference newPostRef = myRef.push();
        newPostRef.setValue(track);



    }
    public static void UpdateTrack(Track track)
    {

    }
    public static void getUserTrack(String userUid,FirebaseHlper FirebaseHlper)
    {
        _FirebaseHlper = FirebaseHlper;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("tracks/"+userUid);
        Log.d("postSnapshot", "myRef: "+myRef);

        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!track.isEmpty())
                track.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    Log.d("postSnapshot", "onDataChange: "+postSnapshot);
                    track.add(postSnapshot.getValue(Track.class).unConvertTrack(postSnapshot.getValue(Track.class)));
                }

                _FirebaseHlper.getTrakList(track);
                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
