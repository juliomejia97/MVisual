package com.providers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pixelmanipulation.ProcessedImageActivity;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FirebaseProvider {

    private static final String STORAGE_IMAGE_PATH = "Series/";
    private static final String PATIENTS_PATH = "pacientes";
    private static final String STUDIES_PATH = "estudios";
    private static final String SERIES_PATH = "series";
    private static final String IMAGES_PATH = "imagenes";
    private static final String PROCESSED_PATH = "procesadas";

    private static ArrayList<DataViewHolder> patients;
    private static ArrayList<DataViewHolder> studies;
    private static ArrayList<DataViewHolder> series;
    private static ArrayList<DataViewHolder> images;
    private static ArrayList<DataViewHolder> processed;

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
        processed = new ArrayList<DataViewHolder>();
        getPatientsFromFirebase();
        getStudiesFromFirebase();
        getSeriesFromFirebase();
        getImagesFromFirebase();
        getProcessedFromFirebase();
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
                    newPatient.setParentId(null);
                    patients.add(newPatient);
                }
                Log.i("Firebase", "Patients Initialized");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getStudiesFromFirebase(){
        mReference = mDatabase.getReference(STUDIES_PATH);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot patient: snapshot.getChildren()){
                    for(DataSnapshot single: patient.getChildren()){
                        DataViewHolder newStudy = single.getValue(DataViewHolder.class);
                        newStudy.setId(single.getKey());
                        newStudy.setParentId(patient.getKey());
                        studies.add(newStudy);
                    }
                }
                Log.i("Firebase", "Studies Initialized");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getSeriesFromFirebase(){
        mReference = mDatabase.getReference(SERIES_PATH);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot study: snapshot.getChildren()){
                    for(DataSnapshot single: study.getChildren()){
                        DataViewHolder newSerie = single.getValue(DataViewHolder.class);
                        newSerie.setId(single.getKey());
                        newSerie.setParentId(study.getKey());
                        series.add(newSerie);
                    }
                }
                Log.i("Firebase", "Series Initialized");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getImagesFromFirebase(){
        mReference = mDatabase.getReference(IMAGES_PATH);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot serie: snapshot.getChildren()){
                    for(DataSnapshot single: serie.getChildren()){
                        DataViewHolder newImage = single.getValue(DataViewHolder.class);
                        newImage.setId(single.getKey());
                        newImage.setParentId(serie.getKey());
                        images.add(newImage);
                    }
                }
                Log.i("Firebase", "Images Initialized");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getProcessedFromFirebase() {
        mReference = mDatabase.getReference(PROCESSED_PATH);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot image: snapshot.getChildren()){
                    for(DataSnapshot single: image.getChildren()){
                        DataViewHolder newProcessed = single.getValue(DataViewHolder.class);
                        newProcessed.setId(single.getKey());
                        newProcessed.setParentId(image.getKey());
                        processed.add(newProcessed);
                    }
                }
                Log.i("Firebase", "Processed Images Initialized");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public static ArrayList<DataViewHolder> getAllProcessed() { return processed; }

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

    public static DataViewHolder getStudyPatient(String idStudy){
        for(DataViewHolder study: studies){
            if(study.getId().equalsIgnoreCase(idStudy)){
                return getPatientById(study.getParentId());
            }
        }
        return null;
    }



    public static DataViewHolder getSeriesStudy(String idSerie){
        for(DataViewHolder serie: series){
            if(serie.getId().equalsIgnoreCase(idSerie)){
                return getStudyById(serie.getParentId());
            }
        }
        return null;
    }

    public static ArrayList<DataViewHolder> getStudiesByPatient(String idPatient){
        ArrayList<DataViewHolder> patStudies = new ArrayList<DataViewHolder>();
        for(DataViewHolder study: studies){
            if(study.getParentId().equalsIgnoreCase(idPatient)){
                patStudies.add(study);
            }
        }
        return patStudies;
    }

    public static ArrayList<DataViewHolder> getSeriesByStudy(String idStudy){
        ArrayList<DataViewHolder> serStudies = new ArrayList<DataViewHolder>();
        for(DataViewHolder serie: series){
            if(serie.getParentId().equalsIgnoreCase(idStudy)){
                serStudies.add(serie);
            }
        }
        return serStudies;
    }

    public static ArrayList<DataViewHolder> getImagesBySeries(String idSerie){
        ArrayList<DataViewHolder> imgSeries = new ArrayList<DataViewHolder>();
        for(DataViewHolder image: images){
            if(image.getParentId().equalsIgnoreCase(idSerie)){
                imgSeries.add(image);
            }
        }
        return imgSeries;
    }

    public void loadImage(String mhdName, String rawName, String imageId, Context context){
        try {
            final File localFile = File.createTempFile(mhdName, ".mhd", context.getFilesDir());
            StorageReference imageRef = mStorageRef.child(STORAGE_IMAGE_PATH + mhdName + ".mhd");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            loadRawImage(rawName, imageId, localFile.getAbsolutePath().toString(), context);
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

    public void loadRawImage(String rawImage, String imageId, String pathMhd, Context context){
        try {
            ProgressDialog pDialog = ProgressDialog.show(context, "Obteniendo archivo de la base de datos...", "Por favor espere", true,false);

            final File localFile = File.createTempFile(rawImage, ".raw", context.getFilesDir());
            StorageReference imageRef = mStorageRef.child(STORAGE_IMAGE_PATH + rawImage + ".raw");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Intent intent = new Intent(context, UploadImageActivity.class);
                            intent.putExtra("mhd", pathMhd);
                            intent.putExtra("raw", localFile.getAbsoluteFile().toString());
                            intent.putExtra("imageId", imageId);
                            pDialog.dismiss();
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

    public static String createProcessed(DataViewHolder newProcessed, String idImage){
        mReference = mDatabase.getReference(PROCESSED_PATH);
        String key = mReference.push().getKey();
        mReference.child(idImage).child(key).setValue(newProcessed);
        return key;
    }

    public static void uploadProcessedImage(Bitmap processedBmp, String key, String title, Context context) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        processedBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference processedRef = mStorageRef.child("Processed/" + key + "/" + title + ".png");
        UploadTask ut = processedRef.putBytes(data);
        ut.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Processed Image", "Failed to upload processed image to storage");
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setMessage("No se pudo guardar la imagen en la base de datos.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Processed Image", "Succesfully uploaded processed image to storage");
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setMessage("La imagen se guardó en la base de datos exitosamente.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
