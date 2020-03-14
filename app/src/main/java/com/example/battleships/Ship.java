package com.example.battleships;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import static com.example.battleships.Game.BOARD_ROW;
import static com.example.battleships.Game.BOARD_SIZE;

public class Ship{
    int length;
    ArrayList<Integer> polesIDs;
    @NonNull Boolean isHorizontal;
    static ArrayList<Ship> shipList;

    public Ship(ArrayList<Integer> polesIDs, int length, boolean isHorizontal){
        this.polesIDs = polesIDs;
        this.length = length;
        this.isHorizontal = isHorizontal;
    }

    public Ship(int length, boolean isHorizontal){
        this.length = length;
        this.isHorizontal = isHorizontal;
    }

    public int getSize(){
        return polesIDs.size();
    }

    public static   ArrayList<Integer> generateArrayList(int length, Integer firstTile, boolean isHorizontal){
        ArrayList<Integer> idList = new ArrayList<>();
        idList.clear();
        if (isHorizontal) {
            for (int i = 0; i < length; i++) {
                idList.add(firstTile + i);
            }
        } else {
            for (int i = 0; i < length; i++) {
                idList.add(firstTile + i * BOARD_ROW);
            }
        }

        return idList;
    }
    //skoro ta metoda tworzy obiekt klasy Ship, to w powinna się znajdowaćw klasie Ship.class
    //nie mniej, możesz to na razie zostawić
    public static Ship createShipWithGivenSize(Integer masztCount) {
        Random random = new Random();
        Ship ship = null;
        do {
            int firstRandomId = random.;
            Boolean isHorizontal = random.nextBoolean();
            if (isHorizontal && firstRandomId % BOARD_ROW <= BOARD_ROW - masztCount || !isHorizontal && firstRandomId + BOARD_ROW * (masztCount - 1) <= BOARD_SIZE) {
//                            shouldRandomAgain = false;
                ship = new Ship(Ship.generateArrayList(masztCount, firstRandomId, isHorizontal), masztCount, isHorizontal);
                if (isShipOnBusyCells(ship)) {
                    ship = null;
                }
            }
        }while (/*shouldRandomAgain || */ship == null );
        shipList.add(ship);
        return ship;
    }









    //method used only for logs, to see if ships are created correctly
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
