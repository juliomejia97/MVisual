package com.example.pixelmanipulation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.example.pixelmanipulation.canva.CanvaImageView;
import com.example.pixelmanipulation.model.ImageMHD;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadImageActivity extends AppCompatActivity {

    private ImageView image;
    private Bitmap imgBitmap;
    private Button btnSelect, btnProcess;
    private SeekBar sbWindow, sbLevel, sbDepth;
    private TextView tvWindow, tvLevel, tvDepth;
    private LinearLayout llWindow, llLevel, llDepth;
    private ImageMHD imageMHD;
    private static final int ALMACENAMIENTO_EXTERNO = 3;
    private static final int IMAGE_PICKER_REQUEST = 4;
    private static final int FILE_PICKER_REQUEST = 5;
    private static boolean accessAlm = false;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        image = findViewById(R.id.imgView);
        tvWindow = findViewById(R.id.tvProgressW);
        tvLevel = findViewById(R.id.tvProgressL);
        tvDepth = findViewById(R.id.tvProgressDepth);
        llWindow = findViewById(R.id.llWindow);
        llLevel = findViewById(R.id.lllevel);
        llDepth = findViewById(R.id.llDepth);
        btnSelect = findViewById(R.id.btnSeleccionar);
        btnProcess = findViewById(R.id.btnProcesar);
        sbWindow = findViewById(R.id.sbWindow);
        sbLevel = findViewById(R.id.sbLevel);
        sbDepth = findViewById(R.id.sbDepth);


        sbWindow.setMax(255);
        sbWindow.setProgress(255);
        sbLevel.setMax(255);
        sbLevel.setProgress(128);
        tvWindow.setText("" + sbWindow.getProgress());
        tvLevel.setText("" + sbLevel.getProgress());

        image.setDrawingCacheEnabled(true);

        llDepth.setVisibility(View.INVISIBLE);
        llWindow.setVisibility(View.INVISIBLE);
        llLevel.setVisibility(View.INVISIBLE);
        btnProcess.setVisibility(View.INVISIBLE);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessAlm = requestPermission(UploadImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, "Permission to Access Gallery", ALMACENAMIENTO_EXTERNO);
                if(accessAlm){
                    usePermissionApplication();
                }
            }
        });

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("¿Seguro que desea continuar?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                Bitmap bitmapIntent = image.getDrawingCache();
                                bitmapIntent.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                Intent intent = new Intent(view.getContext(), CanvaImageView.class);
                                intent.putExtra("BitmapImage", byteArray);
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
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        sbWindow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateWindow();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sbLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateLevel();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sbDepth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateDepth();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

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
            case IMAGE_PICKER_REQUEST: {
                if(resultCode == RESULT_OK){
                    try{
                        final Uri imageUri = data.getData();
                        final InputStream is = getContentResolver().openInputStream(imageUri);
                        imgBitmap = BitmapFactory.decodeStream(is);
                        image.setImageBitmap(imgBitmap);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            }

            case FILE_PICKER_REQUEST: {
                if(resultCode == RESULT_OK){
                    final Uri fileUri = data.getData();
                    String mhdPath = getPathFromUri(UploadImageActivity.this, fileUri);
                    String mhdName = getFileName(fileUri);

                    insertIntoInternalStorage(mhdName, mhdPath);
                    String rawName = mhdName.replace(".mhd", ".raw");
                    String rawPath = mhdPath.replace(mhdName, rawName);
                    insertIntoInternalStorage(rawName, rawPath);
                    Log.i("mhd", "mhdnme " + mhdName);
                    Log.i("mhd", "mhdnme " + rawName);
                    //
                    new GenerateImage().execute(mhdName, rawName);
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
                    usePermissionImage();
                } else {
                    Toast.makeText(getBaseContext(), "Access denied to image gallery", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public void updateWindow() {

        tvWindow.setText("" + sbWindow.getProgress());
        new Thread(new Runnable() {
            @Override
            public void run() {
                image.post(new Runnable() {
                    @Override
                    public void run() {
                        wlAlgorithm();
                    }
                });
            }
        }).start();
    }

    public void updateLevel() {
        tvLevel.setText("" + sbLevel.getProgress());
        new Thread(new Runnable() {
            @Override
            public void run() {
                image.post(new Runnable() {
                    @Override
                    public void run() {
                        wlAlgorithm();
                    }
                });
            }
        }).start();
    }

    public void updateDepth(){
        sbWindow.setProgress(255);
        sbLevel.setProgress(128);
        tvDepth.setText("" + sbDepth.getProgress());
        tvWindow.setText("" + sbWindow.getProgress());
        tvLevel.setText("" + sbLevel.getProgress());
        new Thread(new Runnable() {
            @Override
            public void run() {
                image.post(new Runnable() {
                    @Override
                    public void run() {
                        getBuffer(sbDepth.getProgress());
                    }
                });
            }
        }).start();
    }

    public void getBuffer(int bufferIndex) {

        //Se obtiene el buffer del indice bufferIndex
        int[] buffer = imageMHD.getDepths().get(bufferIndex);
        for(int i = 0; i < buffer.length; i++){
            int color = (255 & 0xff) << 24 | (buffer[i] & 0xff) << 16 | (buffer[i] & 0xff) << 8 | (buffer[i] & 0xff);
            buffer[i] = color;
        }

        imgBitmap = Bitmap.createBitmap(buffer, imageMHD.getW(), imageMHD.getH(), Bitmap.Config.ARGB_8888);
        image.setImageBitmap(imgBitmap);
    }

    public void wlAlgorithm(){

        int W = imgBitmap.getWidth();
        int H = imgBitmap.getHeight();
        Bitmap imgWL;
        imgWL = imgBitmap.copy(Bitmap.Config.ARGB_8888, true);

        for(int i = 0; i < W; i++){
            for(int j = 0; j < H; j++){
                int color = imgWL.getPixel(i, j);
                int indColor = (color >> 16) & 0xff;
                double slope = getSlope(indColor);
                if(slope > 255){
                    slope = 255;
                } else if(slope < 0) {
                    slope = 0;
                }
                int defColor = Color.argb(255, (int) slope, (int) slope, (int) slope);
                imgWL.setPixel(i, j, defColor);
            }
        }

        /*int[] buffer = imageMHD.getDepths().get(sbDepth.getProgress());
        for(int i = 0; i < buffer.length; i++){
            double slope = getSlope(buffer[i]);
            if(slope > 255){
                slope = 255;
            } else if(slope < 0) {
                slope = 0;
            }
            int color = (255 & 0xff) << 24 | ((int) slope & 0xff) << 16 | ((int) slope & 0xff) << 8 | ((int) slope & 0xff);
            buffer[i] = color;
        }

        imgBitmap = Bitmap.createBitmap(buffer, imageMHD.getW(), imageMHD.getH(), Bitmap.Config.ARGB_8888);*/
        image.setImageBitmap(imgWL);
    }

    public double getSlope(int color){

        //Points
        double x1 = sbLevel.getProgress() - (sbWindow.getProgress() / 2);
        double y1 = 0;
        double x2 = sbLevel.getProgress() + (sbWindow.getProgress() / 2);
        double y2 = 255;
        //Get the slope of the curve
        double m = (y2 - y1) / (x2 - x1);
        //Get the Y-axis interception of the curve => Y = mX + b  =>  b = Y - mX
        double b = y2 - (m * x2);
        return m * color + b;
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

    public void showSeekBars(){
        sbWindow.setMax(255);
        sbWindow.setProgress(255);
        sbLevel.setMax(255);
        sbLevel.setProgress(128);
        sbDepth.setMax(imageMHD.getDepths().size() - 1);
        sbDepth.setProgress(0);
        tvWindow.setText("" + sbWindow.getProgress());
        tvLevel.setText("" + sbLevel.getProgress());
        tvDepth.setText("" + sbDepth.getProgress());

        llWindow.setVisibility(View.VISIBLE);
        llLevel.setVisibility(View.VISIBLE);
        llDepth.setVisibility(View.VISIBLE);
        btnProcess.setVisibility(View.VISIBLE);
    }

    public void deleteTemporalFiles(String mhd, String raw){
        File mhdFile = new File(getFilesDir() + "/" + mhd);
        File rawFile = new File(getFilesDir() + "/" + raw);
        if(mhdFile.exists()){
            mhdFile.delete();
        }else{

        }
        if(rawFile.exists()){
            rawFile.delete();
        }
    }

    public native ImageMHD convertMHD(String mhdFile, String rawFile);
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    //----------------------------------------------------ASYNCCLASS
    private class GenerateImage extends AsyncTask<String, Void, Void>{

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getBaseContext(), "Generando Imagen...", "Por favor espere", true,false);
        }

        @Override
        protected Void doInBackground(String... strings) {
            
            imageMHD = convertMHD(getFilesDir() + "/" + strings[0], getFilesDir() + "/" + strings[1]);

            deleteTemporalFiles(strings[0], strings[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            getBuffer(0);
            showSeekBars();
        }
    }


    //----------------------------------------------------UTILS METHODS TO OBTAIN FILE PATHS

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