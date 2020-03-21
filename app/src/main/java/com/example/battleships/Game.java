package com.example.battleships;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Handler;

import static com.example.battleships.Cell.Status.*;

class Game {

    private static final String TAG = "GAME CLASS";


    private Board board;
    private int drownedShipsNum;
    BoardSize size;

    public TwoPlayerGameStartingContract contract;

    ArrayList<String> shipArrayList;
    ArrayList<Integer> shipIdsToRemove;

    Game(TwoPlayerGameStartingContract contract) {
        this.contract = contract;
        initBoard();
    }

    private void initBoard(){
        board = new Board();
        Map<Integer, Integer> mapFromContract = contract.getMap();
        if(mapFromContract != null) {
            for (int shipSize : mapFromContract.keySet()) {
                for (int i = 0; i < mapFromContract.get(shipSize); i++) {
                    board.addRandomShip(shipSize);
                }
            }
        }
    }

    //TODO Zadanie: spróbuj się zająć tą metodą, żeby spełniała swoje poprzednie funkcje, tylko popraw ją by pasowała do aktualnej implementacji
    public void updateCellStatusOnClicked(Integer cellIndex){

        switch (board.getCellStatusById(cellIndex)){
            case BUSY:
                Ship shipOnClickedCell = getShipById(cellIndex);
                if(shipOnClickedCell == null){
                    board.updateSingleCell(cellIndex, MISS);
                } else {
                    board.updateSingleCell(cellIndex, HIT);
                    board.drownShip()
                }

            case UNCOVERED:
                board.updateSingleCell(cellIndex, MISS);

            case DROWNED:
            case MISS:
            case HIT:
                return;
        }

        //neie iterować po wszystkich statkach, tylko wziąc
       for(Ship ship : board.ships){
           if(board.getCellStatusById(cellIndex).equals(DROWNED)){
               return;
           }
           if(ship.IDs.contains(cellIndex)){
               board.updateSingleCell(cellIndex, HIT);
               for (Integer Id : ship.IDs){
                   if(!board.getCellStatusById(Id).equals(HIT)){
                       return;
                   }
               }
               for (int id : ship.IDs) {
                   board.updateSingleCell(id, DROWNED);
               }




               //drowning the ship  - upadte map for listView
//               int shipSizeKey = ship.getSize();
//               int currentValue = contract.getMap().get(shipSizeKey);
//               contract.getMap().put(shipSizeKey, currentValue - 1);
////                   shipIdsToRemove.add(shipArrayList.indexOf("1 x " + ship.polesIDs.size()));
//               return;
           }
       }
        board.updateSingleCell(cellIndex, MISS);
    }


    //ZADANIE Do przeniesienia do klasy Board!
    public Ship getShipById(int id){
        for (Ship ship:board.ships) {
            if(ship.IDs.contains(id)){
                return ship;
            }
        }
        return null;
    }

    public Cell.Status getCellStatusById(int id){
        return board.getCellStatusById(id);
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

