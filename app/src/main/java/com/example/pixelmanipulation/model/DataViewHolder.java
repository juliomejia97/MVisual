package com.example.pixelmanipulation.model;

import android.provider.ContactsContract;
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
    private String parentId;

    public DataViewHolder(String pInfo, String pType) {
        this.info = pInfo;
        this.type = pType;
    }

    public DataViewHolder(){

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

    public String getParentId() { return parentId; }

    public void setParentId(String parentId) { this.parentId = parentId; }
}