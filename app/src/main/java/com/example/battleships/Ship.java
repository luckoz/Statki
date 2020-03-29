package com.example.battleships;

import java.util.ArrayList;

import static com.example.battleships.Constants.BOARD_ROW;
import static com.example.battleships.Constants.BOARD_SIZE;

public class Ship{
    private int length;
    public ArrayList<Integer> IDs;
    boolean isHorizontal;
    boolean isDrowned = false;

    public Ship(int firstId, int length, boolean isHorizontal) {
        this.IDs = generateIdsListByFirst(length, firstId,  isHorizontal);
        this.length = length;
        this.isHorizontal = isHorizontal;
    }

    int getSize(){
        return IDs.size();
    }

   int getFirstId(){
        return IDs.get(0);
   }

   private int getLastId(){
        return IDs.get(IDs.size() - 1);
   }

    private ArrayList<Integer> generateIdsListByFirst(int length, int firstTile, boolean isHorizontal){
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

    //method used only for logs, to see if shipsListForListView are created correctly
    @Override
    public String toString(){
        String orient;
        if(isHorizontal){
            orient = "HORIZONTAL";
        } else {
            orient = "VERTICAL";
        }
        return "length: " + length + " Orientation: " + orient + " ids: " + IDs.toString();
    }

    boolean isOnLeft(){
        return getFirstId() % BOARD_ROW == 0;
    }

    boolean isOnTop(){
        return getFirstId() < BOARD_ROW;
    }

    boolean isOnRight(){
        return getLastId() % BOARD_ROW == BOARD_ROW - 1;
    }
    boolean isOnBottom(){
        return getLastId() + BOARD_ROW > BOARD_SIZE - 1;
    }

    void drownTheShip(){
        isDrowned = true;
    }
}
