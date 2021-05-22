package com.example.pixelmanipulation.canva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pixelmanipulation.R;

import java.io.OutputStream;

public class DrawOnBitmapActivity extends AppCompatActivity {

    DrawableImageView choosenImageView;
    Bitmap bmp;
    Bitmap alteredBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canva_draw_on_bitmap);
        choosenImageView = findViewById(R.id.ChoosenImageView);
        Intent intent = getIntent();
        byte[] byteArray = intent.getByteArrayExtra("BitmapImage");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        alteredBitmap = Bitmap.createBitmap(bmp.getWidth(),
                bmp.getHeight(), bmp.getConfig());

        choosenImageView.setNewImage(alteredBitmap, bmp);


    }
}