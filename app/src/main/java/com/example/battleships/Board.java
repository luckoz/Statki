package com.example.battleships;

import java.util.ArrayList;
import java.util.Random;

import static com.example.battleships.Game.BOARD_ROW;


public class Board {

    ArrayList<Cell> cellArray;

    private ArrayList<Ship> ships;

    public Board(ArrayList cellArray){
        this.cellArray = cellArray;
        ships = new ArrayList<>();
    }


    public void updateCells(ArrayList <Cell> cellsToUpdate, Cell.Status newStatus){
        for (Cell cell:cellsToUpdate) {
            updateSingleCell(cell.getIndex(),newStatus);
        }
    }

    public void updateSingleCell(int id, Cell.Status newStatus){
        cellArray.get(id).setStatus(newStatus);
    }

    public ArrayList<Cell> getCellArrayByIds(ArrayList<Integer> idList){
        ArrayList<Cell> cellList = new ArrayList<>();
        for (Integer id:idList) {
            cellList.add(getCell(id));
        }
        return cellList;
    }

    public Cell getCell(int id){
        return cellArray.get(id);
    }

    public ArrayList<Integer> getNotBusyIds(){
        ArrayList<Integer> possibleIds = new ArrayList<>();
        for (Cell cell : cellArray) {
            if(cell.getStatus() == Cell.Status.UNCOVERED){
                possibleIds.add(cell.getIndex());
            }
        }
        return possibleIds;
    }
    
    public void addRandomShip(){
        Random random = new Random();
        boolean isHorizontal = random.nextBoolean();
        int randomSize = random.nextInt(4) + 1;
        
        //
        
        Ship newShip = new Ship()
        ships.add()
    }
    
    //
    private ArrayList<Integer> getPossibleFirstIds(boolean isHorizontal, int size){
        ArrayList<Integer> idsToReturn = new ArrayList<>();
        for (int notBusyId : getNotBusyIds()){
            if(isHorizontal){
                if((notBusyId+size)%BOARD_ROW = 0/*czy nie jestem za bliko prawej/dołu*/){

                }
                for (int i = notBusyId; i > size; i++){
                    if(getCell(i).getStatus() == Cell.Status.BUSY){
                        break;
                    }
                   idsToReturn.add(notBusyId);
                }

            }
        }
    }

    public void addShip(Ship newShip){
        ships.add(newShip);
    }
}
