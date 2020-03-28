package com.example.battleships;

import java.util.ArrayList;
import java.util.Random;

import static com.example.battleships.Constants.BOARD_ROW;
import static com.example.battleships.Constants.BOARD_SIZE;
import static com.example.battleships.Constants.MAX_SHIP_SIZE;
import static com.example.battleships.Constants.MIN_SHIP_SIZE;

public class Board {

    ArrayList<Cell> cellArray;
    public ArrayList<Ship> ships;

    public Board(){
        cellArray = new ArrayList<>();
        for(int i = 0; i < BOARD_SIZE; i++){
            Cell z = new Cell(i);
            cellArray.add(z);
        }
        ships = new ArrayList<Ship>();
    }

    public void updateCells(ArrayList <Cell> cellsToUpdate, Cell.Status newStatus){
        for (Cell cell : cellsToUpdate) {
            updateSingleCell(cell.getIndex(),newStatus);
        }
    }

    private void updateCellsByIds(ArrayList<Integer> idsToUpdate, Cell.Status newStatus){
        for(int i : idsToUpdate){
            getCell(i).setStatus(newStatus);
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
    public Cell.Status getCellStatusById(int id){
        return cellArray.get(id).getStatus();
    }
    public ArrayList<Integer> getBusyCellsIds(){
        ArrayList<Integer> ids = new ArrayList<>();
        for(Cell cell: cellArray){
            if(cell.getStatus() == Cell.Status.BUSY){
                ids.add(cell.getIndex());
            }
        }
        return ids;
    }

    private ArrayList<Integer> getNotBusyIds(){
        ArrayList<Integer> possibleIds = new ArrayList<>();
        for (Cell cell : cellArray) {
            if(cell.getStatus() == Cell.Status.UNCOVERED){
                possibleIds.add(cell.getIndex());
            }
        }
        return possibleIds;
    }

    public void addRandomShip(int givenSize){
        Random random = new Random();
        boolean isHorizontal = random.nextBoolean();
        int randomFirstId;

        if(isHorizontal){
            ArrayList<Integer> availableFirstIds = getPossibleFirstIdsForHorizontal(givenSize);
            if(availableFirstIds.size() < 1){
                throw new IllegalStateException("NO POSSIBLE POSITION TO PLACE HORIZONTAL SHIP!");
            }
            randomFirstId = availableFirstIds.get(random.nextInt(availableFirstIds.size()));
        } else {
            ArrayList<Integer> availableFirstIds = getPossibleFirstIdsForVertical(givenSize);
            if(availableFirstIds.size() < 1){
                throw new IllegalStateException("NO POSSIBLE POSITION TO PLACE VERTICAL SHIP!");
            }
            randomFirstId = availableFirstIds.get(random.nextInt(availableFirstIds.size()));
        }

        //create new ship by given params and add to list
        Ship newShip = new Ship(randomFirstId, givenSize, isHorizontal);
        addShip(newShip);
    }
    
    public void addRandomShip(){
        Random random = new Random();
        int randomSize = random.nextInt(MAX_SHIP_SIZE - MIN_SHIP_SIZE) + MIN_SHIP_SIZE;

        addRandomShip(randomSize);
    }

    void addShip(Ship newShip){
        ships.add(newShip);
        setBusyCellsForShip(newShip);
    }

    public void drownShip(Ship ship){
        updateCellsByIds(ship.IDs, Cell.Status.DROWNED);
        ship.drownTheShip();
    }
    
    private ArrayList<Integer> getPossibleFirstIdsForHorizontal(int size){
        ArrayList<Integer> idsToReturn = new ArrayList<>();
        for (int notBusyId : getNotBusyIds()){
            if((notBusyId + size) % BOARD_ROW < notBusyId % BOARD_ROW){
                //to close to the right edge
                continue;
            }
            boolean noneIsBusy = true;
            for (int i = notBusyId; i < notBusyId + size; i++){
                if(getCellStatusById(i) == Cell.Status.BUSY){
                    noneIsBusy = false;
                }
            }
            if(noneIsBusy){
                idsToReturn.add(notBusyId);
            }
        }
        return idsToReturn;
    }

    private ArrayList<Integer> getPossibleFirstIdsForVertical(int size){
        ArrayList<Integer> idsToReturn = new ArrayList<>();
        for (int notBusyId : getNotBusyIds()){
            if((notBusyId + size * BOARD_ROW) >= BOARD_SIZE){
                //to close to the bottom edge
                continue;
            }
            boolean noneIsBusy = true;
            for (int i = notBusyId; i < notBusyId + size * BOARD_ROW; i += BOARD_ROW){
                if(getCellStatusById(i) == Cell.Status.BUSY){
                    noneIsBusy = false;
                }
            }
            if(noneIsBusy){
                idsToReturn.add(notBusyId);
            }
        }
        return idsToReturn;
    }

    //metoda, która zwraca bool czy wszystkei staki są DROWNED
    public boolean areAllShipsDrowned(){
        for (Ship ship : ships) {
            if(!ship.isDrowned){
                return false;
            }
        }
        return true;
    }

    public Ship getShipById(int id){
        for (Ship ship : ships) {
            if(ship.IDs.contains(id)){
                return ship;
            }
        }
        return null;
    }


    private void setBusyCellsForShip(Ship ship){
        ArrayList<Integer> idsToSetBusy = new ArrayList<>();
        ArrayList<Integer> idsToRemove = new ArrayList<>();
        int startingId = ship.getFirstId() - BOARD_ROW - 1;

        if(ship.isHorizontal) {
            for (int i = 0; i < ship.getSize() + 2; i++) {
                idsToSetBusy.add((startingId + i));
                idsToSetBusy.add((startingId + BOARD_ROW + i));
                idsToSetBusy.add((startingId + BOARD_ROW * 2 + i));
            }
        } else {
            for (int i = 0; i < ship.getSize() + 2; i++) {
                idsToSetBusy.add((startingId + i*BOARD_ROW));
                idsToSetBusy.add((startingId + i*BOARD_ROW + 1));
                idsToSetBusy.add((startingId + i*BOARD_ROW + 2));
            }
        }

        if(ship.isOnLeft()){
            for (Integer item : idsToSetBusy) {
                if(item % BOARD_ROW == BOARD_ROW - 1 || item < 0){
                    idsToRemove.add(item);
                }
            }
        }
        if(ship.isOnRight()){
            for (Integer item : idsToSetBusy) {
                if(item % BOARD_ROW == 0){
                    idsToRemove.add(item);
                }
            }
        }
        if(ship.isOnTop()){
            for (Integer item : idsToSetBusy) {
                if(item < 0){
                    idsToRemove.add(item);
                }
            }
        }
        if(ship.isOnBottom()){
            for (Integer item : idsToSetBusy) {
                if(item >= BOARD_SIZE){
                    idsToRemove.add(item);
                }
            }
        }
        idsToSetBusy.removeAll(idsToRemove);
        updateCellsByIds( idsToSetBusy, Cell.Status.BUSY);
    }

}
