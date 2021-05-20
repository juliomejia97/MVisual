package com.example.pixelmanipulation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelmanipulation.canva.CanvaImageView;
import com.example.pixelmanipulation.model.ImageMHD;
import com.providers.CpPluginsProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class UploadImageActivity extends AppCompatActivity {

    private CpPluginsProvider provider;
    private ImageView image;
    private Bitmap imgBitmap;
    private ImageView btnProcess;
    private SeekBar sbWindow, sbLevel, sbDepth;
    private TextView tvImage, tvWindow, tvLevel, tvDepth;
    private TextView lblWindow, lblLevel, lblDepth;
    private LinearLayout llWindow, llLevel, llDepth;
    private ImageMHD imageMHD;
    private String mhdName, rawName, imageId;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        provider = CpPluginsProvider.getInstance();
        image = findViewById(R.id.imgView);
        tvImage = findViewById(R.id.tvImageName);
        tvWindow = findViewById(R.id.tvProgressW);
        tvLevel = findViewById(R.id.tvProgressL);
        tvDepth = findViewById(R.id.tvProgressDepth);
        lblWindow = findViewById(R.id.textView3);
        lblLevel = findViewById(R.id.textView4);
        lblDepth = findViewById(R.id.txt_depth);
        llWindow = findViewById(R.id.llWindow);
        llLevel = findViewById(R.id.lllevel);
        llDepth = findViewById(R.id.llDepth);
        btnProcess = findViewById(R.id.btnProcess);
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

        lblWindow.setVisibility(View.INVISIBLE);
        lblLevel.setVisibility(View.INVISIBLE);
        lblDepth.setVisibility(View.INVISIBLE);
        tvImage.setVisibility(View.INVISIBLE);
        llDepth.setVisibility(View.INVISIBLE);
        llWindow.setVisibility(View.INVISIBLE);
        llLevel.setVisibility(View.INVISIBLE);
        btnProcess.setVisibility(View.INVISIBLE);

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("¿Seguro que desea continuar? Si continua y regresa perderá su proceso.")
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
                                intent.putExtra("ImageName", mhdName);
                                intent.putExtra("imageId", imageId);
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

    @Override
    protected void onStart() {
        super.onStart();
        mhdName = getIntent().getStringExtra("mhd");
        rawName = getIntent().getStringExtra("raw");
        imageId = getIntent().getStringExtra("imageId");
        new GenerateImage().execute(mhdName, rawName);
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
        int[] pixels = new int[W * H];
        Bitmap imgWL;
        imgWL = imgBitmap.copy(Bitmap.Config.ARGB_8888, true);
        imgWL.getPixels(pixels, 0, W, 0, 0, W, H);

        for(int i = 0; i < pixels.length; i++){
            int indColor = (pixels[i] >> 16) & 0xff;
            double slope = getSlope(indColor);
            if(slope > 255){
                slope = 255;
            } else if(slope < 0) {
                slope = 0;
            }
            pixels[i] = Color.argb(255, (int) slope, (int) slope, (int) slope);
        }

        imgWL.setPixels(pixels, 0, W, 0, 0, W, H);
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
        return (m * color) + b;
    }

    public void showSeekBars(){

        mhdName = mhdName.replace(getFilesDir().toString() + "/", "");
        mhdName = mhdName.replace(".mhd", "");
        mhdName = mhdName.replaceAll("[0-9]","");

        sbWindow.setMax(255);
        sbWindow.setProgress(255);
        sbLevel.setMax(255);
        sbLevel.setProgress(128);
        sbDepth.setMax(imageMHD.getDepths().size() - 1);
        sbDepth.setProgress(0);
        tvWindow.setText("" + sbWindow.getProgress());
        tvLevel.setText("" + sbLevel.getProgress());
        tvDepth.setText("" + sbDepth.getProgress());
        tvImage.setText("" + mhdName);

        lblWindow.setVisibility(View.VISIBLE);
        lblLevel.setVisibility(View.VISIBLE);
        lblDepth.setVisibility(View.VISIBLE);
        tvImage.setVisibility(View.VISIBLE);
        llWindow.setVisibility(View.VISIBLE);
        llLevel.setVisibility(View.VISIBLE);
        llDepth.setVisibility(View.VISIBLE);
        btnProcess.setVisibility(View.VISIBLE);
    }

    public void deleteTemporalFiles(String mhd, String raw){
        File mhdFile = new File(mhd);
        File rawFile = new File(raw);
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
            pDialog = ProgressDialog.show(UploadImageActivity.this, "Generando Imagen...", "Por favor espere", true,false);
        }

        @Override
        protected Void doInBackground(String... strings) {
            
            imageMHD = convertMHD(strings[0], strings[1]);

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
}