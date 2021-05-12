package com.example.pixelmanipulation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.ByteBuffer;


public class ProcessedImageActivity extends AppCompatActivity {

    private ImageView ivProcessed;
    private Button btnContinuar;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processed_images);
        ivProcessed = findViewById(R.id.ivProcessed);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Processed Activity", "Continuar a guardar");
            }
        });

    }

    @Override
    protected void onStart() {
        //https://stackoverflow.com/questions/7620401/how-to-convert-image-file-data-in-a-byte-array-to-a-bitmap
        super.onStart();
        byte[] buffer = getIntent().getByteArrayExtra("Buffer");
        Bitmap bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        ivProcessed.setImageBitmap(bmp);
    }
}