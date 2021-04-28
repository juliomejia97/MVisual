package com.example.pixelmanipulation.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelmanipulation.R;

public class DataViewHolder{

    private String info;
    private String type;

    public DataViewHolder(String pInfo, String pType) {
        this.info = pInfo;
        this.type = pType;
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