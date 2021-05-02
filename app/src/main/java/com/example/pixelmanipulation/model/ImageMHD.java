package com.example.pixelmanipulation.model;

import java.util.ArrayList;

public class ImageMHD {

    private int W;
    private int H;
    private ArrayList<int[]> depths;

    public ImageMHD(int pW, int pH){
        this.W = pW;
        this.H = pH;
        this.depths = new ArrayList<int[]>();
    }

    public int getW() {
        return W;
    }

    public void setW(int w) {
        W = w;
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public void addBuffer(int[] buffer){ this.depths.add(buffer); }

    public ArrayList<int[]> getDepths() {
        return depths;
    }

    public void setDepths(ArrayList<int[]> depths) {
        this.depths = depths;
    }
}
