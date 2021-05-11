package com.providers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pixelmanipulation.UploadImageActivity;
import com.example.pixelmanipulation.model.DataViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FirebaseProvider {

    private static final String STORAGE_IMAGE_PATH = "Series/";
    private static final String PATIENTS_PATH = "pacientes";
    private static final String STUDIES_PATH = "estudios";
    private static final String SERIES_PATH = "series";
    private static final String IMAGES_PATH = "imagenes";

    private static ArrayList<DataViewHolder> patients;
    private static ArrayList<DataViewHolder> studies;
    private static ArrayList<DataViewHolder> series;
    private static ArrayList<DataViewHolder> images;

    private static StorageReference mStorageRef;
    private static FirebaseProvider firebaseInstance;
    private static FirebaseDatabase mDatabase;
    private static DatabaseReference mReference;

    private FirebaseProvider(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance();
        patients = new ArrayList<DataViewHolder>();
        studies = new ArrayList<DataViewHolder>();
        series = new ArrayList<DataViewHolder>();
        images = new ArrayList<DataViewHolder>();
        getPatientsFromFirebase();
        getStudiesFromFirebase();
        getSeriesFromFirebase();
        getImagesFromFirebase();
    }

    public static FirebaseProvider getInstance() {
        if (firebaseInstance == null) {
            firebaseInstance = new FirebaseProvider();
        }
        return firebaseInstance;
    }

    public static void getPatientsFromFirebase() {
        mReference = mDatabase.getReference(PATIENTS_PATH);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot single: snapshot.getChildren()){
                    DataViewHolder newPatient = single.getValue(DataViewHolder.class);
                    newPatient.setId(single.getKey());
                    newPatient.setParentId("");
                    patients.add(newPatient);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getStudiesFromFirebase(){

    }

    public static void getSeriesFromFirebase(){

    }

    public static void getImagesFromFirebase(){

    }

    public static ArrayList<DataViewHolder> getAllPatients(){
        return patients;
    }

    public static ArrayList<DataViewHolder> getAllStudies(){
        return studies;
    }

    public static ArrayList<DataViewHolder> getAllSeries(){
        return series;
    }

    public static DataViewHolder getPatientById(String id){
        for(DataViewHolder patient: patients){
            if(patient.getId().equalsIgnoreCase(id)){
                return patient;
            }
        }
        return null;
    }

    public static DataViewHolder getStudyById(String id){
        for(DataViewHolder study: studies){
            if(study.getId().equalsIgnoreCase(id)){
                return study;
            }
        }
        return null;
    }

    public static DataViewHolder getSeriesById(String id){
        for(DataViewHolder serie: series){
            if(serie.getId().equalsIgnoreCase(id)){
                return serie;
            }
        }
        return null;
    }

    public static DataViewHolder getStudyPatient(String id){
        /*for(DataViewHolder patient: patients){
            for(DataViewHolder study: patient.getData()){
                if(study.getId().equalsIgnoreCase(id)){
                    return patient;
                }
            }
        }*/
        return null;
    }



    public static DataViewHolder getSeriesStudy(String id){
        /*for(DataViewHolder study: studies){
            for(DataViewHolder serie: study.getData()){
                if(serie.getId().equalsIgnoreCase(id)){
                    return study;
                }
            }
        }*/
        return null;
    }

    public static ArrayList<DataViewHolder> getStudiesByPatient(String id){
        for(DataViewHolder patient: patients){
            if(patient.getId().equalsIgnoreCase(id)){
                //return patient.getData();
            }
        }
        return null;
    }

    public static ArrayList<DataViewHolder> getSeriesByStudy(String id){
        for(DataViewHolder study: studies){
            if(study.getId().equalsIgnoreCase(id)){
                //return study.getData();
            }
        }
        return null;
    }

    public static ArrayList<DataViewHolder> getImagesBySeries(String id){
        for(DataViewHolder serie: series){
            if(serie.getId().equalsIgnoreCase(id)){
                //return serie.getData();
            }
        }
        return null;
    }

    public void loadImage(String mhdName, String rawName, Context context){
        try {
            final File localFile = File.createTempFile(mhdName, ".mhd", context.getFilesDir());
            StorageReference imageRef = mStorageRef.child(STORAGE_IMAGE_PATH + mhdName + ".mhd");
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
            StorageReference imageRef = mStorageRef.child(STORAGE_IMAGE_PATH + rawImage + ".raw");
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

    public static void createPatient(DataViewHolder newPatient){
        mReference = mDatabase.getReference(PATIENTS_PATH);
        String key = mReference.push().getKey();
        mReference.child(key).setValue(newPatient);
    }

    public static void createStudies(DataViewHolder newStudy, String idPatient){
        mReference = mDatabase.getReference(STUDIES_PATH);
        String key = mReference.push().getKey();
        mReference.child(idPatient).child(key).setValue(newStudy);
    }

    public static void createSeries(DataViewHolder newSeries, String idStudy){
        mReference = mDatabase.getReference(SERIES_PATH);
        String key = mReference.push().getKey();
        mReference.child(idStudy).child(key).setValue(newSeries);
    }

    public static void createImages(DataViewHolder newImage, String idSeries){
        mReference = mDatabase.getReference(IMAGES_PATH);
        String key = mReference.push().getKey();
        mReference.child(idSeries).child(key).setValue(newImage);
    }


}
