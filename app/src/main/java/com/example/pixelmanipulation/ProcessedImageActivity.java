package com.example.pixelmanipulation;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelmanipulation.canva.CanvaImageView;
import com.example.pixelmanipulation.model.DataViewHolder;
import com.providers.FirebaseProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;


public class ProcessedImageActivity extends AppCompatActivity {

    private FirebaseProvider provider;
    private ImageView ivProcessed, ivPrevious;
    private Button btnNube, btnDispositivo;
    private String imageId, arrival;
    private Bitmap processedBmp;
    private TextView title;
    private boolean savedFile;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processed_images);
        provider = FirebaseProvider.getInstance();
        ivProcessed = findViewById(R.id.ivProcessed);
        ivProcessed = findViewById(R.id.ivProcessed);
        ivPrevious = findViewById(R.id.previousProcessed);
        title = findViewById(R.id.tvProcessedImage);
        btnNube = findViewById(R.id.btnNube);
        btnDispositivo = findViewById(R.id.btnDispositivo);
        savedFile = false;

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrival.equalsIgnoreCase("ProcessedList")){
                    Intent intent = new Intent(ProcessedImageActivity.this, CategoryListActivity.class);
                    intent.putExtra("Type", "Procesadas");
                    startActivity(intent);
                    finish();
                } else if(arrival.equalsIgnoreCase("CpPlugins")){
                    Log.i("Processed", "Retornar desde CpPlugins");
                    if(!savedFile){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
                        builder.setMessage("¿Seguro que desea regresar? Se recomienda guardar la imagen primero ya que se perderá el procesamiento realizado.")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(ProcessedImageActivity.this, Home.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Intent intent = new Intent(ProcessedImageActivity.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        btnNube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savedFile = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(ProcessedImageActivity.this);
                builder.setTitle("Digite el nombre de la imagen a guardar");
                final EditText input = new EditText(ProcessedImageActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key = provider.createProcessed(new DataViewHolder(input.getText().toString(), "procesadas"), imageId);
                        provider.uploadProcessedImage(processedBmp, key, input.getText().toString(), ProcessedImageActivity.this);
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        btnDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savedFile = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(ProcessedImageActivity.this);
                builder.setTitle("Digite el nombre de la imagen a guardar");
                final EditText input = new EditText(ProcessedImageActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveImage(processedBmp, input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    protected void onStart() {
        //https://stackoverflow.com/questions/7620401/how-to-convert-image-file-data-in-a-byte-array-to-a-bitmap
        super.onStart();
        imageId = getIntent().getStringExtra("imageId");
        arrival = getIntent().getStringExtra("arrival");
        byte[] buffer = getIntent().getByteArrayExtra("Buffer");
        title.setText(getIntent().getStringExtra("title"));
        processedBmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        ivProcessed.setImageBitmap(processedBmp);

        if(arrival.equalsIgnoreCase("CpPlugins")){
            Log.i("Processed", "Arrived from CpPlugins");
        } else if (arrival.equalsIgnoreCase("ProcessedList")){
            Log.i("Processed", "Arrived from List View");
            btnNube.setVisibility(View.INVISIBLE);
        }
    }

    private void saveImage(Bitmap bitmap, String title) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues(title);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Mvisual");
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, this.getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    this.getContentResolver().update(uri, values, null, null);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProcessedImageActivity.this);
                    builder.setMessage("La imagen se guardó en el dispositivo exitosamente.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProcessedImageActivity.this);
                    builder.setMessage("No se pudo guardar la imagen en el dispositivo.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/');

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = title + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProcessedImageActivity.this);
                builder.setMessage("La imagen se guardó en el dispositivo exitosamente.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProcessedImageActivity.this);
                builder.setMessage("No se pudo guardar la imagen en el dispositivo.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private ContentValues contentValues(String title) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}