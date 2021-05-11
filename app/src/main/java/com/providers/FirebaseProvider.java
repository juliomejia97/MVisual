package com.providers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pixelmanipulation.UploadImageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FirebaseProvider {

    private static final String PATH = "Series/";
    private static StorageReference mStorageRef;
    private static FirebaseProvider firebaseInstance;

    private FirebaseProvider(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public static FirebaseProvider getInstance() {
        if (firebaseInstance == null) {
            firebaseInstance = new FirebaseProvider();
        }
        return firebaseInstance;
    }

    public void loadImage(String mhdName, String rawName, Context context){
        try {
            final File localFile = File.createTempFile(mhdName, ".mhd", context.getFilesDir());
            StorageReference imageRef = mStorageRef.child(PATH + mhdName + ".mhd");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            loadRawImage(rawName, localFile.getAbsolutePath().toString(), context);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("Firebase", "Error encontrando el archivo");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRawImage(String rawImage, String pathMhd, Context context){
        try {
            final File localFile = File.createTempFile(rawImage, ".raw", context.getFilesDir());
            StorageReference imageRef = mStorageRef.child(PATH + rawImage + ".raw");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Intent intent = new Intent(context, UploadImageActivity.class);
                            intent.putExtra("mhd", pathMhd);
                            intent.putExtra("raw", localFile.getAbsoluteFile().toString());
                            context.startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("Firebase", "Error encontrando el archivo");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
