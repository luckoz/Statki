package com.example.battleships;


import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Ship{
    int length;
    ArrayList<Integer> polesIDs;
    @NonNull Boolean isHorizontal;

    public Ship(ArrayList<Integer> polesIDs, int length, boolean isHorizontal){
        this.polesIDs = polesIDs;
        this.length = length;
        this.isHorizontal = isHorizontal;

    }

    public Ship(int length, boolean isHorizontal){
        this.length = length;
        this.isHorizontal = isHorizontal;
    }


    @Override
    public String toString(){
        String orient;
        if(isHorizontal){
            orient = "HORIZONTAL";
        } else {
            orient = "VERTICAL";
        }
        return "length: " + length + " Orientation: " + orient + " ids: " + polesIDs.toString();
    }

}
