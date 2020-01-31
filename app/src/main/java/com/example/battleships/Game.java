package com.example.battleships;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

class Game extends MainActivity{

    private static final String TAG = "GAME CLASS";
    ArrayList<String> shiplistArray = new ArrayList<>();

    public static final int BOARD_ROW = 8;
    public static final int BOARD_SIZE = BOARD_ROW*BOARD_ROW;
    private int drownedShipsNum;


    Board board = new Board(new ArrayList());
    BoardSize size;
    ArrayList<Ship> shipList = new ArrayList<>();

    TwoPlayerGameStartingContract contract;

    ArrayList<String> shipArrayList;
    ArrayList<Integer> shipIdsToRemove;


    Game(TwoPlayerGameStartingContract contract) {
        this.contract = contract;
//        this.shipArrayList = shipArraylist;
        initBoard();
    }

    private boolean randomBool() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private Integer getRandomId() {
        return (int) (Math.random() * 64);

    }


    private void initBoard(){
        for(int i = 0; i < BOARD_SIZE; i++){
            Cell z = new Cell(i);
            board.cellArray.add(z);
        }
        Map<Integer, Integer> mapFromContract = contract.getMap();
        if(mapFromContract != null) {
            for (Integer liczba : mapFromContract.keySet()) {
                for (int i = 0; i < mapFromContract.get(liczba); i++) {
                    setBusyCells(createShip(liczba));
                }
            }
        }
    }

    private Ship createShip(Integer masztCount) {
        Ship ship = null;
//        boolean shouldRandomAgain = true;
            do {
//                for (Integer key: contract.map.keySet()) {
//
//                    for (int i = contract.getMap().get(key); i>0 ; i--) {
                        int firstRandomId = getRandomId();
                        Boolean isHorizontal = randomBool();
                        if (isHorizontal && firstRandomId % BOARD_ROW <= BOARD_ROW - masztCount || !isHorizontal && firstRandomId + BOARD_ROW * (masztCount - 1) <= BOARD_SIZE) {
//                            shouldRandomAgain = false;
                            ship = new Ship(generateArrayListForShip(masztCount, firstRandomId, isHorizontal), masztCount, isHorizontal);
                            if (isShipOnBusyCells(ship)) {
                                ship = null;
                            }
                        }
//                    }
//                }
        }while (/*shouldRandomAgain || */ship == null );


        shipList.add(ship);
        //
        ArrayList<Integer> leftover = new ArrayList<>();
        for (Cell cell  : board.cellArray) {
            if(cell.getStatus().equals(Cell.Status.UNCOVERED))
                leftover.add(cell.getIndex());
        }
        Log.d("LEFT ID ", "LEFT IDS FOR NEW SHIPS" +  leftover.toString());
        Log.d(TAG, ship.toString());
        return ship;
    }

    private boolean isShipOnBusyCells(Ship ship) {
        boolean isShipOnBusyCells = false;
        for (Integer id : ship.polesIDs) {
            if(id == 64){
                Log.d(TAG, "FATAL ERROR forbidden ID");
                break;
            }
            if(board.cellArray.get(id).getStatus().equals(Cell.Status.BUSY)) {
                isShipOnBusyCells = true;
                break;
            }
        }
        return isShipOnBusyCells;
    }

    private ArrayList<Integer> generateArrayListForShip(int length, Integer firstTile, boolean isHorizontal){
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
        Log.d(TAG,  idList.toString());
        return idList;
    }



    private void setBusyCells(Ship ship){
        for (int item : getIdsToSetBusy(ship)){
            board.cellArray.get(item).setStatus(Cell.Status.BUSY);
        }

    }

    private ArrayList<Integer> getIdsToSetBusy(Ship ship){
        ArrayList<Integer> idsToSetBusy = new ArrayList<>();
        ArrayList<Integer> idsToRemove = new ArrayList<>();
        int startingId = ship.polesIDs.get(0) - BOARD_ROW - 1;

        if(ship.isHorizontal) {
            for (int i = 0; i < ship.polesIDs.size() + 2; i++) {
                idsToSetBusy.add((startingId + i));
                idsToSetBusy.add((startingId + BOARD_ROW + i));
                idsToSetBusy.add((startingId + BOARD_ROW * 2 + i));
            }
        } else {
            for (int i = 0; i < ship.polesIDs.size() + 2; i++) {
                idsToSetBusy.add((startingId + i*BOARD_ROW));
                idsToSetBusy.add((startingId + i*BOARD_ROW + 1));
                idsToSetBusy.add((startingId + i*BOARD_ROW + 2));
            }
        }

        if(isOnLeft(ship)){
            for (Integer item : idsToSetBusy) {
                if(item % BOARD_ROW == BOARD_ROW - 1 || item < 0){
                    idsToRemove.add(item);
                }
            }
        }
        if(isOnRight(ship)){
            for (Integer item : idsToSetBusy) {
                if(item % BOARD_ROW == 0){
                    idsToRemove.add(item);
                }
            }
        }
        if(isOnTop(ship)){
            for (Integer item : idsToSetBusy) {
                if(item < 0){
                    idsToRemove.add(item);
                }
            }
        }
        if(isOnBottom(ship)){
            for (Integer item : idsToSetBusy) {
                if(item >= BOARD_SIZE){
                    idsToRemove.add(item);
                }
            }
        }
        idsToSetBusy.removeAll(idsToRemove);
        return idsToSetBusy;
    }

    private boolean isOnLeft(Ship ship){
        return ship.polesIDs.get(0) % BOARD_ROW == 0;
    }

    private boolean isOnTop(Ship ship){
        return ship.polesIDs.get(0) < BOARD_ROW;
    }

    private boolean isOnRight(Ship ship){
        return ship.polesIDs.get(ship.polesIDs.size() - 1) % BOARD_ROW == BOARD_ROW - 1;
    }

    private boolean isOnBottom(Ship ship){
        return ship.polesIDs.get(ship.polesIDs.size() - 1) + BOARD_ROW > BOARD_SIZE - 1;
    }



    public void updateCellStatus(Integer cellIndex){

       for(Ship ship : shipList){
           if(board.cellArray.get(cellIndex).getStatus().equals(Cell.Status.DROWNED)){
               return;
           }
           if(ship.polesIDs.contains(cellIndex)){
               board.cellArray.get(cellIndex).setStatus(Cell.Status.HIT);
               for (Integer poleId : ship.polesIDs){
                   if(!board.cellArray.get(poleId).getStatus().equals(Cell.Status.HIT)){
                       return;
                   }
               }
               for (Integer integer : ship.polesIDs) {
                   board.cellArray.get(integer).setStatus(Cell.Status.DROWNED);
                   drownedShipsNum++;
                   if(drownedShipsNum == shipList.size()){
                       end();
                   }

               }
               //drowning the ship  - upadte map for listView
               int shipSizeKey = ship.getSize();
               int currentValue = contract.getMap().get(shipSizeKey);
               contract.getMap().put(shipSizeKey, currentValue - 1);
//                   shipIdsToRemove.add(shipArrayList.indexOf("1 x " + ship.polesIDs.size()));
               return;
           }
       }
        board.cellArray.get(cellIndex).setStatus(Cell.Status.MISS);
    }


    public Ship getShipById(int id){
        for (Ship ship:shipList) {
            if(ship.polesIDs.contains(id)){
                return ship;
            }
        }
        return null;
    }

    enum BoardSize  {
        SMALL,
        MEDIUM,
        BIG
    }
//TODO: listview
    public ArrayList<String> setUpShipListView(Map<Integer, Integer> map){

        ArrayList<String> stringsToReturn = new ArrayList<>();
        for(Integer key : map.keySet()){
            stringsToReturn.add(map.get(key) + " x " + key);
        }
        return stringsToReturn;
    }
    @Override
    public void end() {
        winnerPlayerName = "Player " + currentPlayer;
        super.end();
    }


}

