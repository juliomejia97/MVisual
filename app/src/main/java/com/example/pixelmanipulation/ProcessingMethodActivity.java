package com.example.pixelmanipulation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.providers.CpPluginsProvider;

public class ProcessingMethodActivity extends AppCompatActivity {

    private ImageView imageView;
    private CpPluginsProvider provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = CpPluginsProvider.getInstance();
        setContentView(R.layout.activity_processing_method);
        imageView = findViewById(R.id.ivMethod);
        byte[] buffer = getIntent().getByteArrayExtra("Buffer");
        initData(buffer);
    }

    private void initData(byte[] buffer){
        Bitmap editedImage = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        imageView.setImageBitmap(editedImage);
        //TODO: Inicializar el list view con los algoritmos
    }
}