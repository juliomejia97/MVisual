package com.example.pixelmanipulation.model;

import java.util.ArrayList;

public class ImageMHD {

    private int W;
    private int H;
    private ArrayList<ArrayList<Integer>> depths;

    public ImageMHD(int pW, int pH){
        this.W = pW;
        this.H = pH;
        this.depths = new ArrayList<ArrayList<Integer>>();
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

    public ArrayList<ArrayList<Integer>> getDepths() {
        return depths;
    }

    public void setDepths(ArrayList<ArrayList<Integer>> depths) {
        this.depths = depths;
    }
}
