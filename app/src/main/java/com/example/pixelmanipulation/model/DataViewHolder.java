package com.example.pixelmanipulation.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelmanipulation.R;

import java.util.ArrayList;

public class DataViewHolder{

    private String id;
    private String info;
    private String type;
    private ArrayList<DataViewHolder> data;

    public DataViewHolder(String pId, String pInfo, String pType) {
        this.id = pId;
        this.info = pInfo;
        this.type = pType;
        this.data = new ArrayList<DataViewHolder>();
    }

    public void addData(DataViewHolder data){
        this.data.add(data);
    }

    public ArrayList<DataViewHolder> getData(){
        return this.data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}