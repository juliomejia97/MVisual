package com.example.pixelmanipulation;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.providers.FirebaseProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Home extends AppCompatActivity {

    private static final int ALMACENAMIENTO_EXTERNO = 3;
    private static final int IMAGE_PICKER_REQUEST = 4;
    private static final int FILE_PICKER_REQUEST = 5;
    private static boolean accessAlm = false;
    private TextView title_home1,title_home2,title_home3;
    private LinearLayout llPatients, llStudies, llSeries, llProcessed;
    private TableLayout tlHomeMenu;
    private ImageView top_bar_home;
    private FloatingActionButton btnFloating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title_home1 = findViewById(R.id.title_home1);
        title_home2 = findViewById(R.id.title_home2);
        title_home3 = findViewById(R.id.title_home3);
        llPatients = findViewById(R.id.llPatients);
        llStudies = findViewById(R.id.llStudies);
        llSeries = findViewById(R.id.llSeries);
        llProcessed = findViewById(R.id.llProcesed);
        tlHomeMenu = findViewById(R.id.tlHomeMenu);
        top_bar_home =findViewById(R.id.top_bar_home);
        btnFloating = findViewById(R.id.btnFloating);

        title_home1.bringToFront();
        title_home2.bringToFront();
        title_home3.bringToFront();

        title_home1.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);

        title_home2.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);
        title_home3.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);

        ObjectAnimator topBarAnimation = ObjectAnimator.ofFloat(top_bar_home, "translationY", -852f);
        topBarAnimation.setDuration(1000);



        llPatients.setOnClickListener(view -> {
            hideAnimations(topBarAnimation,"Patients");
            Runnable r = () -> {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("Type", "Pacientes");
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
                startActivity(intent);
            };

            Handler h = new Handler();
            h.postDelayed(r, 850);

        });


        llStudies.setOnClickListener(view -> {

                hideAnimations(topBarAnimation, "Studies");
            Runnable r = () -> {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.putExtra("Type", "Estudios");
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
                startActivity(intent);
            };

            Handler h = new Handler();
            h.postDelayed(r, 850);

        });

        llSeries.setOnClickListener(view -> {
            hideAnimations(topBarAnimation, "Series");
            Runnable r = () -> {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("Type", "Series");
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
                startActivity(intent);
            };

            Handler h = new Handler();
            h.postDelayed(r, 850);
        });
        llProcessed.setOnClickListener(view -> {
            hideAnimations(topBarAnimation, "Processed");
            llPatients.animate()
                    .scaleYBy(-0.5f)
                    .scaleXBy(-0.5f)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(1000);

            tlHomeMenu.animate()
                    .alpha(0f)
                    .setDuration(1800)
                    .setListener(null);

        });

        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessAlm = requestPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE, "Permission to Access Gallery", ALMACENAMIENTO_EXTERNO);
                if(accessAlm){
                    usePermissionApplication();
                }
            }
        });
    }


    private void hideAnimations(ObjectAnimator topBarAnimation,String folder) {
        topBarAnimation.start();
        btnFloating.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(null);

        tlHomeMenu.animate()
                .scaleYBy(-0.58f)
                .scaleXBy(-0.58f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500);

        switch(folder) {
            case "Patients":
                tlHomeMenu.animate().translationY(-1060f).setDuration(850);
                tlHomeMenu.animate().translationX(-210f).setDuration(850);

                llStudies.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llSeries.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);

                llProcessed.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                break;
            case "Studies":
                tlHomeMenu.animate().translationY(-1060f).setDuration(850);
                tlHomeMenu.animate().translationX(-415f).setDuration(850);
                llPatients.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llSeries.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);

                llProcessed.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                break;
            case "Series":
                tlHomeMenu.animate().translationY(-1280f).setDuration(850);
                tlHomeMenu.animate().translationX(-210f).setDuration(850);
                llPatients.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llStudies.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llProcessed.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                break;
            case "Processed":
                tlHomeMenu.animate().translationY(-1280f).setDuration(1500);
                tlHomeMenu.animate().translationX(-415f).setDuration(1500);
                llPatients.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llStudies.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llSeries.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                break;
            default:
        }
    }

    private boolean requestPermission(Activity context, String permit, String justification, int id){
        if(ContextCompat.checkSelfPermission(context, permit) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permit)){
                Toast.makeText(getBaseContext(), justification, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permit}, id);
            return false;
        } else {
            return true;
        }
    }

    private void usePermissionApplication(){
        Intent appIntent = new Intent(Intent.ACTION_GET_CONTENT);
        appIntent.setType("*/*");
        startActivityForResult(appIntent, FILE_PICKER_REQUEST);
    }

    private void usePermissionImage(){
        Intent pictureIntent = new Intent(Intent.ACTION_PICK);
        pictureIntent.setType("image/*");
        startActivityForResult(pictureIntent, IMAGE_PICKER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){

            case FILE_PICKER_REQUEST: {
                if(resultCode == RESULT_OK){
                    final Uri fileUri = data.getData();
                    String mhdPath = getPathFromUri(Home.this, fileUri);
                    String mhdName = getFileName(fileUri);

                    insertIntoInternalStorage(mhdName, mhdPath);
                    String rawName = mhdName.replace(".mhd", ".raw");
                    String rawPath = mhdPath.replace(mhdName, rawName);
                    insertIntoInternalStorage(rawName, rawPath);
                    Log.i("mhd", "mhdnme " + mhdName);
                    Log.i("mhd", "mhdnme " + rawName);
                    Intent intent = new Intent(getBaseContext(), UploadImageActivity.class);
                    intent.putExtra("mhd", getFilesDir() + "/" + mhdName);
                    intent.putExtra("raw", getFilesDir() + "/" + rawName);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){

            case ALMACENAMIENTO_EXTERNO: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    usePermissionApplication();
                } else {
                    Toast.makeText(getBaseContext(), "Access denied to image gallery", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public String getPathFromUri(Context context, Uri uri){
        String realPath;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            realPath = getRealPathFromURI_BelowAPI11(context, uri);
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            realPath = getRealPathFromURI_API11to18(context, uri);
        }
        // SDK > 19 (Android 4.4) and up
        else {
            realPath = getRealPathFromURI_API19(context, uri);
        }
        return realPath;
    }

    public String getFileName(Uri uri){
        String result = null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try{
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }

        if(result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void insertIntoInternalStorage(String name, String path){

        try {
            FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);
            File file = new File(path);
            byte[] bytes = getBytesFromFile(file);
            Log.i("Files", "Bytes: " + bytes.length);
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytesFromFile(File file) throws IOException{

        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        is.read(buffer);
        is.close();
        return buffer;
    }

    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        }
        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = 0;
        String result = "";
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return result;
    }

    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}