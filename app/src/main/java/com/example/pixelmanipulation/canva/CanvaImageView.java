package com.example.pixelmanipulation.canva;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelmanipulation.Home;
import com.example.pixelmanipulation.ProcessedImageActivity;
import com.example.pixelmanipulation.ProcessingMethodActivity;
import com.example.pixelmanipulation.R;
import com.example.pixelmanipulation.canva.Interface.ToolsListener;
import com.example.pixelmanipulation.adapters.ToolsAdapter;
import com.example.pixelmanipulation.canva.common.Common;
import com.example.pixelmanipulation.model.ToolsItem;
import com.example.pixelmanipulation.canva.widget.PaintView;

import com.providers.CpPluginsProvider;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import petrov.kristiyan.colorpicker.ColorPicker;

import static androidx.recyclerview.widget.RecyclerView.*;

public class CanvaImageView  extends AppCompatActivity implements ToolsListener {

    private CpPluginsProvider provider;
    private PaintView mPaintView;
    private ImageView previous, btnProcess;
    private TextView  imgName;
    private int colorBackground, colorBrush;
    private int brushSize, eraserSize;
    private String imageId;
    private byte[] originalByteArray;
    private Bitmap originalBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canva_image_view);
        provider = CpPluginsProvider.getInstance();
        mPaintView = findViewById(R.id.paint_view);
        previous = findViewById(R.id.previousCanva);
        btnProcess = findViewById(R.id.btnProcessCanva);
        imgName = findViewById(R.id.tvImageNameCanva);
        Intent intent = getIntent();
        originalByteArray = intent.getByteArrayExtra("BitmapImage");
        originalBmp = BitmapFactory.decodeByteArray(originalByteArray, 0, originalByteArray.length);
        BitmapDrawable background = new BitmapDrawable(getResources(), originalBmp);
        initTools();
        mPaintView.setBackground(background);
        imgName.setText(intent.getStringExtra("ImageName"));
        imageId = intent.getStringExtra("imageId");

        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CanvaImageView.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        btnProcess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processImage();
            }
        });

    }

    private void initTools() {

        colorBackground= Color.WHITE;
        colorBrush=Color.RED;

        eraserSize=brushSize=5;
        RecyclerView recyclerView = findViewById(R.id.recycle_view_tools);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL,false));
        ToolsAdapter toolsAdapter = new ToolsAdapter(loadTools(),this);
        recyclerView.setAdapter(toolsAdapter);
    }
    private List<ToolsItem> loadTools() {
        List<ToolsItem> result = new ArrayList<>();
        result.add(new ToolsItem(R.drawable.ic_baseline_brush_24, Common.BRUSH));
        result.add(new ToolsItem(R.drawable.eraser_icon,Common.ERASER));
        result.add(new ToolsItem(R.drawable.ic_baseline_palette_24,Common.COLORS));
        result.add(new ToolsItem(R.drawable.ic_baseline_undo_24,Common.RETURN));

        return result;
    }

    private List<ToolsItem> loadOptions() {

        List<ToolsItem> result = new ArrayList<>();

        result.add(new ToolsItem(R.drawable.ic_baseline_brush_24, Common.BRUSH));
        result.add(new ToolsItem(R.drawable.eraser24,Common.ERASER));

        return result;
    }

    public void finishPaint (View view){ }

    public void shareApp(View view) { }

    public void showFiles(View view) { }

    public void saveFile(View view) { }

    @Override
    public void onSelected(String name) {
        switch (name){
            case Common.BRUSH:
                mPaintView.disableEraser();
                showDialogSize(false);
                break;
            case Common.ERASER:
                mPaintView.enableEraser();
                showDialogSize(true);
                break;
            case Common.RETURN:
                mPaintView.returnLastAction();
                break;
            case Common.COLORS:
                updateColor(name);
                break;
        }
    }

    private void updateColor(final String name) {
        int color;
        if(name.equals(Common.BACKGROUND)){
            color=colorBackground;
        }else{
            color=colorBrush;
        }
        ColorPicker colorPicker = new ColorPicker(CanvaImageView.this);
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                // put code
                if(name.equals(Common.BACKGROUND)){
                    colorBackground=color;
                    mPaintView.setColorBackground(colorBackground);
                }else {
                    colorBrush=color;
                    mPaintView.setBrushColor(colorBrush);
                }
            }

            @Override
            public void onCancel(){
                // put code
            }
        }).disableDefaultButtons(true).setColumns(5).setRoundColorButton(true).show();


    }

    private void showDialogSize(final boolean isEraser) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_canva_layout_dialog,null,false);

        final TextView toolsSelected = view.findViewById(R.id.status_tools_selected);
        final TextView statusSize = view.findViewById(R.id.status_size);
        final ImageView ivTools = view.findViewById(R.id.iv_tools);
        SeekBar seekBar=view.findViewById(R.id.seekbar_size);
        seekBar.setMax(19);
        if(isEraser){
            toolsSelected.setText("Eraser Size");
            ivTools.setImageResource(R.drawable.eraser24);
            statusSize.setText("Selected Size: "+ eraserSize);
        }else{
            toolsSelected.setText("Brush Size");
            ivTools.setImageResource(R.drawable.ic_baseline_brush_24);
            statusSize.setText("Selected Size: "+ brushSize);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(isEraser){
                    eraserSize = i + 1;
                    statusSize.setText("Selected Size: " + eraserSize);
                    mPaintView.setSizeEraser(eraserSize);
                }else{
                    brushSize=i+1;
                    statusSize.setText("Selected Size: " + brushSize);
                    mPaintView.setSizeBrush(brushSize);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(view);
        builder.show();
    }

    public void processImage(){

        int W = mPaintView.getBitmap().getWidth();
        int H = mPaintView.getBitmap().getHeight();

        int[] buffer = new int[W * H];
        for(int i = 0; i < buffer.length; i++){
            int color = (0 & 0xff) << 24 | (0 & 0xff) << 16 | (0 & 0xff) << 8 | (0 & 0xff);
            buffer[i] = color;
        }

        ByteArrayOutputStream previewStream = new ByteArrayOutputStream();
        Bitmap previewBmp = mPaintView.getBitmap();
        previewBmp.compress(Bitmap.CompressFormat.PNG, 100, previewStream);
        byte[] previewByteArray = previewStream.toByteArray();

        //Se actualiza en nuevo background con el fondo transparente
        Bitmap imgBitmap = Bitmap.createBitmap(buffer, W, H, Bitmap.Config.ARGB_8888);
        BitmapDrawable background = new BitmapDrawable(getResources(), imgBitmap);
        mPaintView.setBackground(background);

        //Se obtiene el buffer del bitmap final (Fondo transparente y los trazos)
        ByteArrayOutputStream newStream = new ByteArrayOutputStream();
        Bitmap newBitmap = mPaintView.getBitmap();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, newStream);
        byte[] newByteArray = newStream.toByteArray();

        //Se actualiza nuevamente el background a la imagen original
        BitmapDrawable back = new BitmapDrawable(getResources(), originalBmp);
        mPaintView.setBackground(back);

        JSONObject jsonObject = provider.createJSON(H,W,originalByteArray,newByteArray,"");
        //Se envia la información del Canva a la pantalla de la selección de algoritmo
        Intent intent = new Intent(CanvaImageView.this, ProcessingMethodActivity.class);
        intent.putExtra("BufferPreview", previewByteArray);
        saveJSONInternalStorage(jsonObject.toString());
        intent.putExtra("imageId", imageId);
        startActivity(intent);
    }


    private boolean saveJSONInternalStorage(String jsonString){
        String FILENAME = "storage.json";
        try {
            FileOutputStream fos = this.openFileOutput(FILENAME,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }
    }
}