package com.example.pixelmanipulation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.datastore.generated.model.AmplifyModelProvider;


public class ProcessedImagesActivity extends AppCompatActivity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processed_images);
        AmplifyModelProvider.getInstance(getApplicationContext());
    }

}