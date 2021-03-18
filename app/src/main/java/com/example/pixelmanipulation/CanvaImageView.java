package com.example.pixelmanipulation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelmanipulation.Interface.ToolsListener;
import com.example.pixelmanipulation.adapters.ToolsAdapter;
import com.example.pixelmanipulation.model.ToolsItem;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;

public class CanvaImageView  extends AppCompatActivity implements ToolsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canva_image_view);

        initTools();
    }

    private void initTools() {

        RecyclerView recyclerView = findViewById(R.id.recycle_view_tools);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL,false));
        ToolsAdapter toolsAdapter = new ToolsAdapter(loadTools(),this);
        recyclerView.setAdapter(toolsAdapter);
    }

    private List<ToolsItem> loadTools() {

        List<ToolsItem> result = new ArrayList<>();

        result.add(new ToolsItem(R.drawable.ic_baseline_brush_24,"brush"));
        result.add(new ToolsItem(R.drawable.eraser24,"eraser"));
        result.add(new ToolsItem(R.drawable.fill24,"background"));
        result.add(new ToolsItem(R.drawable.ic_baseline_undo_24,"return"));


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

    }
}