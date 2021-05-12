package com.example.pixelmanipulation.canva;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelmanipulation.R;
import com.example.pixelmanipulation.canva.Interface.ToolsListener;
import com.example.pixelmanipulation.adapters.ToolsAdapter;
import com.example.pixelmanipulation.canva.common.Common;
import com.example.pixelmanipulation.model.ToolsItem;
import com.example.pixelmanipulation.canva.widget.PaintView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.providers.CpPluginsProvider;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;

public class CanvaImageView  extends AppCompatActivity implements ToolsListener {

    private CpPluginsProvider provider;
    private PaintView mPaintView;
    private int colorBackground,colorBrush;
    private int brushSize, eraserSize;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canva_image_view);
        provider = CpPluginsProvider.getInstance();
        mPaintView = findViewById(R.id.paint_view);
        Intent intent = getIntent();
        Log.i("Canva", "onCreate");
        byte[] byteArray = intent.getByteArrayExtra("BitmapImage");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        BitmapDrawable background = new BitmapDrawable(getResources(), bmp);
        initTools();
        mPaintView.setBackground(background);
    }

    private void initTools() {

        colorBackground= Color.WHITE;
        colorBrush=Color.BLACK;

        eraserSize=brushSize=12;
        //mPaintView=findViewById(R.id.paint_view);
        RecyclerView recyclerView = findViewById(R.id.recycle_view_tools);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL,false));
        ToolsAdapter toolsAdapter = new ToolsAdapter(loadTools(),this);
        recyclerView.setAdapter(toolsAdapter);
    }

    private List<ToolsItem> loadTools() {

        List<ToolsItem> result = new ArrayList<>();

        result.add(new ToolsItem(R.drawable.ic_baseline_brush_24, Common.BRUSH));
        result.add(new ToolsItem(R.drawable.eraser24,Common.ERASER));
        result.add(new ToolsItem(R.drawable.ic_baseline_palette_24,Common.COLORS));
        result.add(new ToolsItem(R.drawable.ic_baseline_undo_24,Common.RETURN));


        return result;
    }

    public void finishPaint (View view){

    }


    public void shareApp(View view) {
    }

    public void showFiles(View view) {
    }

    public void saveFile(View view) {

    }

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
            case Common.BACKGROUND:
                updateColor(name);
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
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(color)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                        if(name.equals(Common.BACKGROUND)){
                            colorBackground=lastSelectedColor;
                            mPaintView.setColorBackground(colorBackground);
                        }else {
                            colorBrush=lastSelectedColor;
                            mPaintView.setBrushColor(colorBrush);
                        }
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        }).build()
                .show();
    }

    private void showDialogSize(final boolean isEraser) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_canva_layout_dialog,null,false);

        final TextView toolsSelected = view.findViewById(R.id.status_tools_selected);
        final TextView statusSize = view.findViewById(R.id.status_size);
        final ImageView ivTools = view.findViewById(R.id.iv_tools);
        SeekBar seekBar=view.findViewById(R.id.seekbar_size);
        seekBar.setMax(99);
        if(isEraser){
            toolsSelected.setText("Eraser Size");
            ivTools.setImageResource(R.drawable.eraser24);
            statusSize.setText("Selected Size: "+eraserSize);
        }else{
            toolsSelected.setText("Brush Size");
            ivTools.setImageResource(R.drawable.ic_baseline_brush_24);
            statusSize.setText("Selected Size: "+brushSize);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(isEraser){
                    eraserSize=i+1;
                    statusSize.setText("Selected Size: "+eraserSize);
                    mPaintView.setSizeEraser(eraserSize);
                }else{
                    brushSize=i+1;
                    statusSize.setText("Selected Size: "+brushSize);
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

    public void changeBackground(int W, int H){
        int[] buffer = new int[W * H];
        for(int i = 0; i < buffer.length; i++){
            int color = (0 & 0xff) << 24 | (0 & 0xff) << 16 | (0 & 0xff) << 8 | (0 & 0xff);
            buffer[i] = color;
        }

        Bitmap imgBitmap = Bitmap.createBitmap(buffer, W, H, Bitmap.Config.ARGB_8888);
        BitmapDrawable background = new BitmapDrawable(getResources(), imgBitmap);
        mPaintView.setBackground(background);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmapIntent = mPaintView.getBitmap();
        bitmapIntent.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        JSONObject data = provider.createJSON(bitmapIntent.getHeight(), bitmapIntent.getWidth(), byteArray, byteArray);
        provider.sendPOSTRequestCpPlugins(CanvaImageView.this, data);
    }

}