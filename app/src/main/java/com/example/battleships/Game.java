package com.example.battleships;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

//*Trages, Game nie może rozszerzać aktywności!!! Co te diwe klasy mają wspólnego ze sobą???*
//*class Game extends MainActivity{*
class Game {

    private static final String TAG = "GAME CLASS";

    public static final int BOARD_ROW = 8;
    public static final int BOARD_SIZE = BOARD_ROW*BOARD_ROW;
    private int drownedShipsNum;


    Board board = new Board(new ArrayList());
    BoardSize size;

    TwoPlayerGameStartingContract contract;

    ArrayList<String> shipArrayList;
    ArrayList<Integer> shipIdsToRemove;


    Game(TwoPlayerGameStartingContract contract) {
        this.contract = contract;
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
            for (Integer number : mapFromContract.keySet()) {
                for (int i = 0; i < mapFromContract.get(number); i++) {
                    board.addRandomShip(number);
                }
            }
        }
    }

    //Po zaimplementowaniu metod w klasie Board, to będzie względnie do usunięcia, choć logika stąd się przyda w innym miejscu
    public void updateCellStatusOnClicked(Integer cellIndex){

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
//                   drownedShipsNum++;
//                   if(drownedShipsNum == shipList.size()){
//                       end();
//                   }

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


    //ZADANIE Do przeniesienia do klasy Board!
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
//    @Override
//    public void end() {
//        winnerPlayerName = "Player " + currentPlayer;
//        super.end();
//    }


}

