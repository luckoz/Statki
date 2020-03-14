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

                                board.updateCells(board.getCellArrayByIds(getIdsToSetBusy(Ship.createShip(liczba))), Cell.Status.BUSY);
                                

                }
            }
        }
    }





    //ZADANIE tę metodę nalezy usunąć i logikę uprościć
    //Zamiast powtarzać w kółko algorytm sprawdzający, czy statek nie stworzył się przypadkiem na zajętych polach,
    //Napisz metodę w klasie Board, która zwróci listę ids, które nie są jeszcze zajęte + metodę, która zwróci listę ids, na kórych można zbudować statek
    //(Nie wiem, czy precyzyjnie wyjasniłem o co chodzi, jeśłi nie dość jasno albo zbyt trudne - pomiń) :)
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





//    //ta metodą będzie zastąpiona metodą update cells w klasie Board, gdzie nowy status podamy jako argument.
//    private void setBusyCells(Ship ship){
//        for (int item : getIdsToSetBusy(ship)){
//            board.cellArray.get(item).setStatus(Cell.Status.BUSY);
//        }
//
//    }


    //ZADANIE 2: Całą tą metodę należy przenieść do klasy Board.class
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

    //Wszystkie 4 poniższe metody określają w jakis sposób obiekt klasy statek, a właściwie jego powiązanie z planszą
    //Na pewno nie mogą tutaj zostać, proponuję przenieść je wszystkie do klasy Board i zmienić nieco nazewnictwo
    // coś na kształt:
    //private boolean isShipOnLeftSide(Ship ship)

    //Druga sprawa:
    // ship.polesIDs.get(0) | ship.polesIDs.size()- te konstrukcje są  okropne, trzeba zastąpić je metodami  w klasie Ship:
    //coś w stylu getFirstId() | getSize()

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


    //Po zaimplementowaniu metod w klasie Board, to będzie względnie do usunięcia, choć logika stąd się przyda w innym miejscu
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

