package com.example.pixelmanipulation;

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

import java.io.OutputStream;

public class DrawOnBitmapActivity extends AppCompatActivity {

    DrawableImageView choosenImageView;
    Button saveImage;
    Bitmap bmp;
    Bitmap alteredBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_on_bitmap);
        //Inflates
        choosenImageView = findViewById(R.id.ChoosenImageView);
        saveImage =  findViewById(R.id.SavePictureButton);
        //get intent and set bitmap
        Intent intent = getIntent();
        byte[] byteArray = intent.getByteArrayExtra("BitmapImage");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        alteredBitmap = Bitmap.createBitmap(bmp.getWidth(),
                bmp.getHeight(), bmp.getConfig());

        choosenImageView.setNewImage(alteredBitmap, bmp);

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alteredBitmap != null){
                    ContentValues contentValues = new ContentValues(3);
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Draw On Me");

                    Uri imageFileUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    try {
                        OutputStream imageFileOS = getContentResolver()
                                .openOutputStream(imageFileUri);
                        alteredBitmap
                                .compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS);
                        Toast t = Toast
                                .makeText(v.getContext(), "Saved!", Toast.LENGTH_SHORT);
                        t.show();

                    } catch (Exception e) {
                        Log.v("EXCEPTION", e.getMessage());
                    }
                }
            }
        });
    }
}